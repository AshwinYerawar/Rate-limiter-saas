package com.yourcompany.ratelimiter.api.controller;

import com.yourcompany.ratelimiter.api.dto.RateLimitCheckRequest;
import com.yourcompany.ratelimiter.api.dto.RateLimitCheckResponse;
import com.yourcompany.ratelimiter.engine.policy.PolicyContext;
import com.yourcompany.ratelimiter.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/rate-limit")
public class RateLimitController {

    private final RateLimitService rateLimitService;

    public RateLimitController(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/check")
    public RateLimitCheckResponse check(
            @Valid @RequestBody RateLimitCheckRequest request,
            HttpServletRequest httpRequest) {

        String tenantId = (String) httpRequest.getAttribute("tenantId");
        String apiKey = (String) httpRequest.getAttribute("apiKey");

        var context = new PolicyContext(
                tenantId,
                apiKey,
                request.getEndpoint(),
                request.getHttpMethod()
        );

        var result = rateLimitService.check(context);

        return new RateLimitCheckResponse(
                result.isAllowed(),
                result.getLimit(),
                result.getRemaining(),
                result.getResetAt(),
                result.getAlgorithm()
        );
    }
}