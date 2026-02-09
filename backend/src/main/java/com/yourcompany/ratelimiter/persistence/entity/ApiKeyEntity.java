package com.yourcompany.ratelimiter.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "api_keys")
public class ApiKeyEntity {

    @Id
    private UUID id;

    @Column(name = "api_key", unique = true)
    private String apiKey;

    private boolean active;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private TenantEntity tenant;

    @Column(name = "created_at")
    private Instant createdAt;

    public UUID getId() {
        return id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public boolean isActive() {
        return active;
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

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setTenant(TenantEntity tenant) {
        this.tenant = tenant;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getKey() {
        return apiKey;
    }

    public String getStatus() {
        return active ? "ACTIVE" : "INACTIVE";
    }
}