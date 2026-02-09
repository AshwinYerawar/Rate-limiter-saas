package com.yourcompany.ratelimiter.engine;

import com.yourcompany.ratelimiter.engine.policy.RateLimitPolicy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RateLimiterDispatcher {

    private final List<RateLimiterEngine> engines;

    public RateLimiterDispatcher(List<RateLimiterEngine> engines) {
        this.engines = engines;
    }

    public RateLimitResult execute(RateLimitRequest request) {

        RateLimitPolicy policy = request.getPolicy();

        return engines.stream()
                .filter(e -> e.getAlgorithm() == policy.getAlgorithm())
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No engine for algorithm " + policy.getAlgorithm()
                ))
                .check(request);
    }
}