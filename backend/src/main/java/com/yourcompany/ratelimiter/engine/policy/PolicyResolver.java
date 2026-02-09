package com.yourcompany.ratelimiter.engine.policy;

public interface PolicyResolver {

    RateLimitPolicy resolve(PolicyContext context);
}