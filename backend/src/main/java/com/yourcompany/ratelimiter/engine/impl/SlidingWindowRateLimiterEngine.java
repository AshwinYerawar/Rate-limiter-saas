package com.yourcompany.ratelimiter.engine.impl;

import com.yourcompany.ratelimiter.engine.RateLimitAlgorithm;
import com.yourcompany.ratelimiter.engine.RateLimitRequest;
import com.yourcompany.ratelimiter.engine.RateLimitResult;
import com.yourcompany.ratelimiter.engine.RateLimiterEngine;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class SlidingWindowRateLimiterEngine implements RateLimiterEngine {

    private final StringRedisTemplate redisTemplate;

    public SlidingWindowRateLimiterEngine(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.SLIDING_WINDOW;
    }

    @Override
    public RateLimitResult check(RateLimitRequest request) {

        var policy = request.getPolicy();
        long limit = policy.getLimit();
        long windowSeconds = policy.getWindowSeconds();

        String key = request.getRateLimitKey();

        long nowMillis = Instant.now().toEpochMilli();
        long windowStart = nowMillis - (windowSeconds * 1000);

        // 1 Remove expired requests
        redisTemplate.opsForZSet()
                .removeRangeByScore(key, 0, windowStart);

        // 2 Count requests in window
        Long count = redisTemplate.opsForZSet().zCard(key);
        if (count == null) count = 0L;

        // 3 Block if limit exceeded
        if (count >= limit) {
            Instant resetAt = Instant.ofEpochMilli(windowStart + (windowSeconds * 1000));
            return RateLimitResult.blocked(limit, resetAt, RateLimitAlgorithm.SLIDING_WINDOW);
        }

        // 4 Add current request
        redisTemplate.opsForZSet().add(
                key,
                UUID.randomUUID().toString(),
                nowMillis
        );

        // Optional safety TTL (avoid orphan keys)
        redisTemplate.expire(key, windowSeconds, java.util.concurrent.TimeUnit.SECONDS);

        Instant resetAt = Instant.ofEpochMilli(nowMillis + (windowSeconds * 1000));

        return RateLimitResult.allowed(
                limit,
                limit - count - 1,
                resetAt, RateLimitAlgorithm.SLIDING_WINDOW
        );
    }
}