package com.yourcompany.ratelimiter.service;

import com.yourcompany.ratelimiter.persistence.entity.TenantEntity;
import com.yourcompany.ratelimiter.persistence.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TenantService {

    private final TenantRepository tenantRepository;

    public TenantService(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    public List<TenantEntity> getAllTenants() {
        return tenantRepository.findAll();
    }

    public TenantEntity getTenantById(UUID id) {
        return tenantRepository.findById(id).orElseThrow(() -> new RuntimeException("Tenant not found"));
    }

    public TenantEntity createTenant(TenantEntity tenant) {
        tenant.setId(UUID.randomUUID());
        tenant.setCreatedAt(Instant.now());
        return tenantRepository.save(tenant);
    }

    public TenantEntity updateTenant(UUID id, TenantEntity tenant) {
        TenantEntity existing = getTenantById(id);
        existing.setName(tenant.getName());
        existing.setStatus(tenant.getStatus());
        return tenantRepository.save(existing);
    }

    public void deleteTenant(UUID id) {
        tenantRepository.deleteById(id);
    }
}