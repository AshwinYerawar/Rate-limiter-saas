package com.yourcompany.ratelimiter.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
    name = "rate_limit_endpoint_policies",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = {"tenant_id", "endpoint", "http_method"})
    }
)
@Getter
public class RateLimitEndpointPolicy {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    private String endpoint;
    private String httpMethod;

    private String algorithm;

    @Column(name = "limit_value")
    private long limit;

    private long windowSeconds;

    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public long getLimit() {
        return limit;
    }

    public long getWindowSeconds() {
        return windowSeconds;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public void setLimit(long limit) {
        this.limit = limit;
    }

    public void setWindowSeconds(long windowSeconds) {
        this.windowSeconds = windowSeconds;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}