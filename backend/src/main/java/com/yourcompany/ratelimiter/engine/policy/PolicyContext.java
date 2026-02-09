package com.yourcompany.ratelimiter.engine.policy;

public class PolicyContext {

    private final String tenantId;
    private final String apiKey;
    private final String endpoint;
    private final String httpMethod;

    public PolicyContext(String tenantId,
                         String apiKey,
                         String endpoint,
                         String httpMethod) {
        this.tenantId = tenantId;
        this.apiKey = apiKey;
        this.endpoint = endpoint;
        this.httpMethod = httpMethod;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }
}