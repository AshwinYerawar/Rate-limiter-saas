package com.yourcompany.ratelimiter.persistence.repository;

import com.yourcompany.ratelimiter.persistence.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TenantRepository extends JpaRepository<TenantEntity, UUID> {
}