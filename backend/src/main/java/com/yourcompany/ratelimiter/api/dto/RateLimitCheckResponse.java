package com.yourcompany.ratelimiter.api.dto;

import com.yourcompany.ratelimiter.engine.RateLimitAlgorithm;

import java.time.Instant;

public class RateLimitCheckResponse {

    private final boolean allowed;
    private final long limit;
    private final long remaining;
    private final Instant resetAt;
    private final RateLimitAlgorithm algorithm;

    public RateLimitCheckResponse(boolean allowed,
                                  long limit,
                                  long remaining,
                                  Instant resetAt, RateLimitAlgorithm algorithm) {
        this.allowed = allowed;
        this.limit = limit;
        this.remaining = remaining;
        this.resetAt = resetAt;
        this.algorithm = algorithm;
    }

    public RateLimitAlgorithm getAlgorithm() {
        return algorithm;
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
}