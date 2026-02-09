package com.yourcompany.ratelimiter.engine;

public enum RateLimitAlgorithm {

    FIXED_WINDOW,
    SLIDING_WINDOW,
    TOKEN_BUCKET,
    LEAKY_BUCKET
}