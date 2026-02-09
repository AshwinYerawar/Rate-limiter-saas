package com.yourcompany.ratelimiter.api.dto;

import jakarta.validation.constraints.NotBlank;

public class RateLimitCheckRequest {

    @NotBlank
    private String endpoint;

    @NotBlank
    private String httpMethod;

    public String getEndpoint() {
        return endpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }
}