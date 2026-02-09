package com.yourcompany.ratelimiter.service;

import com.yourcompany.ratelimiter.persistence.entity.RateLimitEndpointPolicy;
import com.yourcompany.ratelimiter.persistence.repository.RateLimitEndpointPolicyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class EndpointPolicyService {

    private final RateLimitEndpointPolicyRepository endpointPolicyRepository;

    public EndpointPolicyService(RateLimitEndpointPolicyRepository endpointPolicyRepository) {
        this.endpointPolicyRepository = endpointPolicyRepository;
    }

    public List<RateLimitEndpointPolicy> getAllEndpointPolicies() {
        return endpointPolicyRepository.findAll();
    }

    public List<RateLimitEndpointPolicy> getEndpointPoliciesByTenant(UUID tenantId) {
        return endpointPolicyRepository.findByTenantId(tenantId);
    }

    public RateLimitEndpointPolicy getEndpointPolicyById(UUID id) {
        return endpointPolicyRepository.findById(id).orElseThrow(() -> new RuntimeException("Endpoint Policy not found"));
    }

    public RateLimitEndpointPolicy createEndpointPolicy(RateLimitEndpointPolicy policy) {
        policy.setId(UUID.randomUUID());
        policy.setCreatedAt(Instant.now());
        return endpointPolicyRepository.save(policy);
    }

    public RateLimitEndpointPolicy updateEndpointPolicy(UUID id, RateLimitEndpointPolicy policy) {
        RateLimitEndpointPolicy existing = getEndpointPolicyById(id);
        existing.setEndpoint(policy.getEndpoint());
        existing.setHttpMethod(policy.getHttpMethod());
        existing.setAlgorithm(policy.getAlgorithm());
        existing.setLimit(policy.getLimit());
        existing.setWindowSeconds(policy.getWindowSeconds());
        return endpointPolicyRepository.save(existing);
    }

    public void deleteEndpointPolicy(UUID id) {
        endpointPolicyRepository.deleteById(id);
    }
}