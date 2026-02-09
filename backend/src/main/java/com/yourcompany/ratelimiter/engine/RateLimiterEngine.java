package com.yourcompany.ratelimiter.engine;

public interface RateLimiterEngine {

    RateLimitAlgorithm getAlgorithm();

    RateLimitResult check(RateLimitRequest request);
}