package com.yourcompany.ratelimiter.engine.policy.impl;

import com.yourcompany.ratelimiter.cache.CacheKeys;
import com.yourcompany.ratelimiter.engine.RateLimitAlgorithm;
import com.yourcompany.ratelimiter.engine.policy.PolicyContext;
import com.yourcompany.ratelimiter.engine.policy.PolicyResolver;
import com.yourcompany.ratelimiter.engine.policy.RateLimitPolicy;
import com.yourcompany.ratelimiter.persistence.entity.RateLimitEndpointPolicy;
import com.yourcompany.ratelimiter.persistence.repository.RateLimitEndpointPolicyRepository;
import com.yourcompany.ratelimiter.persistence.repository.RateLimitPolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@Primary
public class DbPolicyResolver implements PolicyResolver {

    private static final Logger log =
            LoggerFactory.getLogger(DbPolicyResolver.class);

    private final RateLimitPolicyRepository tenantRepo;
    private final RateLimitEndpointPolicyRepository endpointRepo;
    private final StringRedisTemplate redis;

    public DbPolicyResolver(
            RateLimitPolicyRepository tenantRepo,
            RateLimitEndpointPolicyRepository endpointRepo,
            StringRedisTemplate redis
    ) {
        this.tenantRepo = tenantRepo;
        this.endpointRepo = endpointRepo;
        this.redis = redis;
    }

    @Override
    public RateLimitPolicy resolve(PolicyContext context) {

        // Normalize (VERY IMPORTANT)
        String endpoint = context.getEndpoint().stripTrailing();
        String method = context.getHttpMethod().toUpperCase();
        String tenantId = context.getTenantId();

        log.info(
                "Resolving policy | tenantId={} endpoint='{}' method={}",
                tenantId, endpoint, method
        );

        /* ==================================================
           1️⃣ Endpoint-level policy (highest priority)
           ================================================== */
        String endpointKey = CacheKeys.endpointPolicy(
                tenantId, endpoint, method
        );

        String cachedEndpoint = redis.opsForValue().get(endpointKey);
        if (cachedEndpoint != null) {
            RateLimitPolicy p = deserialize(cachedEndpoint);
            log.warn(
                    "🔥 USING ENDPOINT POLICY (REDIS) 🔥 algo={} limit={} window={}",
                    p.getAlgorithm(), p.getLimit(), p.getWindowSeconds()
            );
            return p;
        }

        var endpointPolicyOpt =
                endpointRepo.findByTenantIdAndEndpointAndHttpMethod(
                        UUID.fromString(tenantId),
                        endpoint,
                        method
                );

        if (endpointPolicyOpt.isPresent()) {
            RateLimitPolicy policy = toPolicy(endpointPolicyOpt.get());

            redis.opsForValue()
                    .set(endpointKey, serialize(policy), Duration.ofMinutes(10));

            log.warn(
                    "🔥 USING ENDPOINT POLICY (DB) 🔥 algo={} limit={} window={}",
                    policy.getAlgorithm(), policy.getLimit(), policy.getWindowSeconds()
            );

            return policy;
        }

        log.info("No endpoint override found, falling back to tenant policy");

        /* ==================================================
           2️⃣ Tenant-level fallback
           ================================================== */
        String tenantKey = CacheKeys.policy(tenantId);

        String cachedTenant = redis.opsForValue().get(tenantKey);
        if (cachedTenant != null) {
            RateLimitPolicy p = deserialize(cachedTenant);
            log.warn(
                    "⚠️ USING TENANT POLICY (REDIS) ⚠️ algo={} limit={} window={}",
                    p.getAlgorithm(), p.getLimit(), p.getWindowSeconds()
            );
            return p;
        }

        var tenantPolicy = tenantRepo
                .findByTenantId(UUID.fromString(tenantId))
                .orElseThrow(() -> new IllegalStateException(
                        "No tenant policy configured for tenant " + tenantId
                ));

        RateLimitPolicy policy = new RateLimitPolicy(
                tenantPolicy.getId().toString(),
                RateLimitAlgorithm.valueOf(tenantPolicy.getAlgorithm()),
                tenantPolicy.getLimit(),
                tenantPolicy.getWindowSeconds()
        );

        redis.opsForValue()
                .set(tenantKey, serialize(policy), Duration.ofMinutes(10));

        log.warn(
                "⚠️ USING TENANT POLICY (DB) ⚠️ algo={} limit={} window={}",
                policy.getAlgorithm(), policy.getLimit(), policy.getWindowSeconds()
        );

        return policy;
    }

    /* ==================================================
       Helpers
       ================================================== */

    private String serialize(RateLimitPolicy p) {
        return String.join("|",
                p.getPolicyId(),
                p.getAlgorithm().name(),
                String.valueOf(p.getLimit()),
                String.valueOf(p.getWindowSeconds())
        );
    }

    private RateLimitPolicy deserialize(String value) {
        String[] p = value.split("\\|");
        return new RateLimitPolicy(
                p[0],
                RateLimitAlgorithm.valueOf(p[1]),
                Long.parseLong(p[2]),
                Long.parseLong(p[3])
        );
    }

    private RateLimitPolicy toPolicy(RateLimitEndpointPolicy e) {
        return new RateLimitPolicy(
                e.getId().toString(),
                RateLimitAlgorithm.valueOf(e.getAlgorithm()),
                e.getLimit(),
                e.getWindowSeconds()
        );
    }
}