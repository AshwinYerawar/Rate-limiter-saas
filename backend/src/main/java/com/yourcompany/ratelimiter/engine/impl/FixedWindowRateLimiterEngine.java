package com.yourcompany.ratelimiter.engine.impl;

import com.yourcompany.ratelimiter.engine.RateLimitAlgorithm;
import com.yourcompany.ratelimiter.engine.RateLimitRequest;
import com.yourcompany.ratelimiter.engine.RateLimitResult;
import com.yourcompany.ratelimiter.engine.RateLimiterEngine;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Component
public class FixedWindowRateLimiterEngine implements RateLimiterEngine {

    private final StringRedisTemplate redisTemplate;

    // For now, keep limits simple & explicit
    // (later these come from Policy)
//    private static final long LIMIT = 5;          // max requests
//    private static final long WINDOW_SECONDS = 60; // per 60 seconds

    public FixedWindowRateLimiterEngine(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.FIXED_WINDOW;
    }

    @Override
    public RateLimitResult check(RateLimitRequest request) {

        var policy = request.getPolicy();
        long limit = policy.getLimit();
        long windowSeconds = policy.getWindowSeconds();

        String key = request.getRateLimitKey();

        Long current = redisTemplate.opsForValue().increment(key);

        if (current == null) {
            return RateLimitResult.allowed(
                    limit,
                    limit - 1,
                    Instant.now().plusSeconds(windowSeconds), RateLimitAlgorithm.FIXED_WINDOW
            );
        }

        if (current == 1) {
            redisTemplate.expire(key, windowSeconds, TimeUnit.SECONDS);
        }

        Instant resetAt = Instant.now().plusSeconds(windowSeconds);

        if (current <= limit) {
            return RateLimitResult.allowed(
                    limit,
                    limit - current,
                    resetAt, RateLimitAlgorithm.FIXED_WINDOW
            );
        }

        return RateLimitResult.blocked(limit, resetAt, RateLimitAlgorithm.FIXED_WINDOW);
    }
}