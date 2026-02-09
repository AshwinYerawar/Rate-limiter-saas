package com.yourcompany.ratelimiter.security;

import com.yourcompany.ratelimiter.cache.CacheKeys;
import com.yourcompany.ratelimiter.persistence.repository.ApiKeyRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
public class ApiKeyStore {

    private final ApiKeyRepository repository;
    private final StringRedisTemplate redisTemplate;

    public ApiKeyStore(ApiKeyRepository repository,
                       StringRedisTemplate redisTemplate) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
    }

    public Optional<String> resolveTenant(String apiKey) {

        String cacheKey = CacheKeys.apiKey(apiKey);

        // 1 Try cache
        String cachedTenant = redisTemplate.opsForValue().get(cacheKey);
        if (cachedTenant != null) {
            return Optional.of(cachedTenant);
        }

        // 2 Fallback to DB
        var tenantOpt = repository.findByApiKeyAndActiveTrue(apiKey)
                .map(k -> k.getTenant().getId().toString());

        // 3 Cache result
        tenantOpt.ifPresent(tenant ->
                redisTemplate.opsForValue()
                        .set(cacheKey, tenant, Duration.ofMinutes(10))
        );

        return tenantOpt;
    }
}