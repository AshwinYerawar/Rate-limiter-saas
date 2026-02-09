package com.yourcompany.ratelimiter.engine.policy.impl;

import com.yourcompany.ratelimiter.engine.RateLimitAlgorithm;
import com.yourcompany.ratelimiter.engine.policy.PolicyContext;
import com.yourcompany.ratelimiter.engine.policy.PolicyResolver;
import com.yourcompany.ratelimiter.engine.policy.RateLimitPolicy;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class InMemoryPolicyResolver implements PolicyResolver {

    private final Map<String, RateLimitPolicy> policies = Map.of(
            "tenant1", new RateLimitPolicy(
                    "policy-basic",
                    RateLimitAlgorithm.FIXED_WINDOW,
                    5,
                    60
            )
    );

    @Override
    public RateLimitPolicy resolve(PolicyContext context) {
        return policies.get(context.getTenantId());
    }
}