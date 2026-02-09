package com.yourcompany.ratelimiter.persistence.repository;

import com.yourcompany.ratelimiter.persistence.entity.ApiKeyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ApiKeyRepository extends JpaRepository<ApiKeyEntity, UUID> {

    Optional<ApiKeyEntity> findByApiKeyAndActiveTrue(String apiKey);

    List<ApiKeyEntity> findByTenantId(UUID tenantId);
}