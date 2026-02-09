package com.yourcompany.ratelimiter.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "rate_limit_policies")
public class RateLimitPolicyEntity {

    @Id
    private UUID id;

    private String name;
    private String algorithm;

    @Column(name = "limit_value")
    private long limit;

    @Column(name = "window_seconds")
    private long windowSeconds;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private TenantEntity tenant;

    @Column(name = "created_at")
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public TenantEntity getTenant() {
        return tenant;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setTenant(TenantEntity tenant) {
        this.tenant = tenant;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}