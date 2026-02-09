package com.yourcompany.ratelimiter.engine;

import java.time.Instant;

public class RateLimitResult {

    private final boolean allowed;
    private final long limit;
    private final long remaining;
    private final Instant resetAt;
    private final RateLimitAlgorithm algorithm;

    private RateLimitResult(boolean allowed,
                            long limit,
                            long remaining,
                            Instant resetAt, RateLimitAlgorithm algorithm) {
        this.allowed = allowed;
        this.limit = limit;
        this.remaining = remaining;
        this.resetAt = resetAt;
        this.algorithm = algorithm;
    }

    public static RateLimitResult allowed(long limit,
                                          long remaining,
                                          Instant resetAt, RateLimitAlgorithm algorithm) {
        return new RateLimitResult(true, limit, remaining, resetAt, algorithm);
    }

    public static RateLimitResult blocked(long limit,
                                          Instant resetAt, RateLimitAlgorithm algorithm) {
        return new RateLimitResult(false, limit, 0, resetAt, algorithm);
    }

    public boolean isAllowed() {
        return allowed;
    }

    public long getLimit() {
        return limit;
    }

    public long getRemaining() {
        return remaining;
    }

    public Instant getResetAt() {
        return resetAt;
    }

    public RateLimitAlgorithm getAlgorithm() {
        return algorithm;
    }
}