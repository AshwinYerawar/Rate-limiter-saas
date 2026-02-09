package com.yourcompany.ratelimiter.service;

import com.yourcompany.ratelimiter.cache.CacheKeys;
import com.yourcompany.ratelimiter.engine.RateLimitRequest;
import com.yourcompany.ratelimiter.engine.RateLimitResult;
import com.yourcompany.ratelimiter.engine.RateLimiterDispatcher;
import com.yourcompany.ratelimiter.engine.policy.PolicyContext;
import com.yourcompany.ratelimiter.engine.policy.PolicyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RateLimitService {

    private static final Logger log =
            LoggerFactory.getLogger(RateLimitService.class);

    private final PolicyResolver policyResolver;
    private final RateLimiterDispatcher dispatcher;

    public RateLimitService(
            PolicyResolver policyResolver,
            RateLimiterDispatcher dispatcher
    ) {
        this.policyResolver = policyResolver;
        this.dispatcher = dispatcher;
    }

    public RateLimitResult check(PolicyContext context) {

        // 1️⃣ Resolve policy (endpoint > tenant)
        var policy = policyResolver.resolve(context);

        log.info(
                "🚦 Policy resolved | algo={} limit={} window={}",
                policy.getAlgorithm(),
                policy.getLimit(),
                policy.getWindowSeconds()
        );

        // 2️⃣ Build CORRECT rate-limit key
        String rateLimitKey = CacheKeys.rateLimitKey(
                context.getTenantId(),
                context.getApiKey(),
                context.getEndpoint(),
                context.getHttpMethod()
        );

        log.info("🔑 Rate limit key = {}", rateLimitKey);

        // 3️⃣ Create request
        var request = new RateLimitRequest(
                context.getTenantId(),
                rateLimitKey,
                policy
        );

        // 4️⃣ Execute algorithm
        return dispatcher.execute(request);
    }
}