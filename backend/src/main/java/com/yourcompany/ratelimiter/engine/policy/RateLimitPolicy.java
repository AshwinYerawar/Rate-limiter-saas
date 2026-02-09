package com.yourcompany.ratelimiter.engine.policy;

import com.yourcompany.ratelimiter.engine.RateLimitAlgorithm;

public class RateLimitPolicy {

    private final String policyId;
    private final RateLimitAlgorithm algorithm;
    private final long limit;
    private final long windowSeconds;

    public RateLimitPolicy(String policyId,
                           RateLimitAlgorithm algorithm,
                           long limit,
                           long windowSeconds) {
        this.policyId = policyId;
        this.algorithm = algorithm;
        this.limit = limit;
        this.windowSeconds = windowSeconds;
    }

    public String getPolicyId() {
        return policyId;
    }

    public RateLimitAlgorithm getAlgorithm() {
        return algorithm;
    }

    public long getLimit() {
        return limit;
    }

    public long getWindowSeconds() {
        return windowSeconds;
    }
}