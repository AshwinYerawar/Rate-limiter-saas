package com.yourcompany.ratelimiter.engine;

import com.yourcompany.ratelimiter.engine.policy.RateLimitPolicy;

public class RateLimitRequest {


    private final String tenantId;
    private final String rateLimitKey;
    private final RateLimitPolicy policy;


    public RateLimitRequest(String tenantId,
                            String rateLimitKey,
                            RateLimitPolicy policy) {
        this.tenantId = tenantId;
        this.rateLimitKey = rateLimitKey;
        this.policy = policy;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getRateLimitKey() {
        return rateLimitKey;
    }

    public RateLimitPolicy getPolicy() {
        return policy;
    }
}