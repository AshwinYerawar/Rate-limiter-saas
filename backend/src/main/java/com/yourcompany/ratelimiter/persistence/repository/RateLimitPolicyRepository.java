package com.yourcompany.ratelimiter.persistence.repository;

import com.yourcompany.ratelimiter.persistence.entity.RateLimitPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface RateLimitPolicyRepository
        extends JpaRepository<RateLimitPolicyEntity, UUID> {

    Optional<RateLimitPolicyEntity> findByTenantId(UUID tenantId);
}