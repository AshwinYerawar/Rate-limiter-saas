package com.yourcompany.ratelimiter.engine.impl;

import com.yourcompany.ratelimiter.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class TokenBucketRateLimiterEngine implements RateLimiterEngine {

    private final StringRedisTemplate redis;
    private final DefaultRedisScript<List> script;

    public TokenBucketRateLimiterEngine(StringRedisTemplate redis) {
        this.redis = redis;
        this.script = new DefaultRedisScript<>();
        this.script.setLocation(
                new ClassPathResource("redis/token_bucket.lua")
        );
        this.script.setResultType(List.class);
    }

    @Override
    public RateLimitAlgorithm getAlgorithm() {
        return RateLimitAlgorithm.TOKEN_BUCKET;
    }

    @Override
    public RateLimitResult check(RateLimitRequest request) {

        var policy = request.getPolicy();

        long capacity = policy.getLimit();
        double refillRate =
                (double) policy.getLimit() / policy.getWindowSeconds();

        long now = Instant.now().getEpochSecond();

        List<Long> result = redis.execute(
                script,
                List.of(request.getRateLimitKey()),
                String.valueOf(capacity),
                String.valueOf(refillRate),
                String.valueOf(now)
        );

        boolean allowed = result.get(0) == 1;
        long remaining = result.get(1);

        Instant resetAt = Instant.now()
                .plusSeconds(
                        (long) Math.ceil(1 / refillRate)
                );

        return allowed
                ? RateLimitResult.allowed(capacity, remaining, resetAt, RateLimitAlgorithm.TOKEN_BUCKET)
                : RateLimitResult.blocked(capacity, resetAt, RateLimitAlgorithm.TOKEN_BUCKET);
    }
}