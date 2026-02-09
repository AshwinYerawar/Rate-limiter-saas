package com.yourcompany.ratelimiter.persistence.repository;

import com.yourcompany.ratelimiter.persistence.entity.RateLimitEndpointPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RateLimitEndpointPolicyRepository extends JpaRepository<RateLimitEndpointPolicy, UUID> {

    List<RateLimitEndpointPolicy> findByTenantId(UUID tenantId);
    Optional<RateLimitEndpointPolicy> findByTenantIdAndEndpointAndHttpMethod(UUID tenantId, String endpoint, String httpMethod);
}