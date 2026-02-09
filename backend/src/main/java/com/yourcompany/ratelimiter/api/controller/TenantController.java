package com.yourcompany.ratelimiter.api.controller;

import com.yourcompany.ratelimiter.persistence.entity.TenantEntity;
import com.yourcompany.ratelimiter.service.TenantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public List<TenantEntity> getAllTenants() {
        return tenantService.getAllTenants();
    }

    @GetMapping("/{id}")
    public TenantEntity getTenantById(@PathVariable UUID id) {
        return tenantService.getTenantById(id);
    }

    @PostMapping
    public TenantEntity createTenant(@RequestBody TenantEntity tenant) {
        return tenantService.createTenant(tenant);
    }

    @PutMapping("/{id}")
    public TenantEntity updateTenant(@PathVariable UUID id, @RequestBody TenantEntity tenant) {
        return tenantService.updateTenant(id, tenant);
    }

    @DeleteMapping("/{id}")
    public void deleteTenant(@PathVariable UUID id) {
        tenantService.deleteTenant(id);
    }
}