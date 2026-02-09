package com.yourcompany.ratelimiter.api.controller;

import com.yourcompany.ratelimiter.persistence.entity.ApiKeyEntity;
import com.yourcompany.ratelimiter.service.ApiKeyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/api-keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @GetMapping
    public List<ApiKeyEntity> getAllApiKeys() {
        return apiKeyService.getAllApiKeys();
    }

    @GetMapping("/tenant/{tenantId}")
    public List<ApiKeyEntity> getApiKeysByTenant(@PathVariable UUID tenantId) {
        return apiKeyService.getApiKeysByTenant(tenantId);
    }

    @GetMapping("/{id}")
    public ApiKeyEntity getApiKeyById(@PathVariable UUID id) {
        return apiKeyService.getApiKeyById(id);
    }

    @PostMapping
    public ApiKeyEntity createApiKey(@RequestBody ApiKeyEntity apiKey) {
        return apiKeyService.createApiKey(apiKey);
    }

    @PutMapping("/{id}")
    public ApiKeyEntity updateApiKey(@PathVariable UUID id, @RequestBody ApiKeyEntity apiKey) {
        return apiKeyService.updateApiKey(id, apiKey);
    }

    @DeleteMapping("/{id}")
    public void deleteApiKey(@PathVariable UUID id) {
        apiKeyService.deleteApiKey(id);
    }
}