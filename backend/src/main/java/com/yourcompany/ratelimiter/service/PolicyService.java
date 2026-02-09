package com.yourcompany.ratelimiter.service;

import com.yourcompany.ratelimiter.persistence.entity.RateLimitPolicyEntity;
import com.yourcompany.ratelimiter.persistence.repository.RateLimitPolicyRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyService {

    private final RateLimitPolicyRepository policyRepository;

    public PolicyService(RateLimitPolicyRepository policyRepository) {
        this.policyRepository = policyRepository;
    }

    public List<RateLimitPolicyEntity> getAllPolicies() {
        return policyRepository.findAll();
    }

    public Optional<RateLimitPolicyEntity> getPoliciesByTenant(UUID tenantId) {
        return policyRepository.findByTenantId(tenantId);
    }

    public RateLimitPolicyEntity getPolicyById(UUID id) {
        return policyRepository.findById(id).orElseThrow(() -> new RuntimeException("Policy not found"));
    }

    public RateLimitPolicyEntity createPolicy(RateLimitPolicyEntity policy) {
        policy.setId(UUID.randomUUID());
        policy.setCreatedAt(Instant.now());
        return policyRepository.save(policy);
    }

    public RateLimitPolicyEntity updatePolicy(UUID id, RateLimitPolicyEntity policy) {
        RateLimitPolicyEntity existing = getPolicyById(id);
        existing.setName(policy.getName());
        existing.setAlgorithm(policy.getAlgorithm());
        existing.setLimit(policy.getLimit());
        existing.setWindowSeconds(policy.getWindowSeconds());
        existing.setTenant(policy.getTenant());
        return policyRepository.save(existing);
    }

    public void deletePolicy(UUID id) {
        policyRepository.deleteById(id);
    }
}