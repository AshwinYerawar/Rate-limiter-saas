package com.yourcompany.ratelimiter.service;

import com.yourcompany.ratelimiter.persistence.entity.ApiKeyEntity;
import com.yourcompany.ratelimiter.persistence.repository.ApiKeyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    public ApiKeyService(ApiKeyRepository apiKeyRepository) {
        this.apiKeyRepository = apiKeyRepository;
    }

    public List<ApiKeyEntity> getAllApiKeys() {
        return apiKeyRepository.findAll();
    }

    public List<ApiKeyEntity> getApiKeysByTenant(UUID tenantId) {
        return apiKeyRepository.findByTenantId(tenantId);
    }

    public ApiKeyEntity getApiKeyById(UUID id) {
        return apiKeyRepository.findById(id).orElseThrow(() -> new RuntimeException("API Key not found"));
    }

    public ApiKeyEntity createApiKey(ApiKeyEntity apiKey) {
        apiKey.setId(UUID.randomUUID());
        apiKey.setCreatedAt(Instant.now());
        return apiKeyRepository.save(apiKey);
    }

    public ApiKeyEntity updateApiKey(UUID id, ApiKeyEntity apiKey) {
        ApiKeyEntity existing = getApiKeyById(id);

        existing.setApiKey(apiKey.getApiKey());
        existing.setTenant(apiKey.getTenant());
        existing.setActive(apiKey.isActive());

        return apiKeyRepository.save(existing);
    }

    public void deleteApiKey(UUID id) {
        apiKeyRepository.deleteById(id);
    }
}