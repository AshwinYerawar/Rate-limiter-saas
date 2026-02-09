package com.yourcompany.ratelimiter.api.controller;

import com.yourcompany.ratelimiter.persistence.entity.RateLimitPolicyEntity;
import com.yourcompany.ratelimiter.service.PolicyService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @GetMapping
    public List<RateLimitPolicyEntity> getAllPolicies() {
        return policyService.getAllPolicies();
    }

    @GetMapping("/tenant/{tenantId}")
    public Optional<RateLimitPolicyEntity> getPoliciesByTenant(@PathVariable UUID tenantId) {
        return policyService.getPoliciesByTenant(tenantId);
    }

    @GetMapping("/{id}")
    public RateLimitPolicyEntity getPolicyById(@PathVariable UUID id) {
        return policyService.getPolicyById(id);
    }

    @PostMapping
    public RateLimitPolicyEntity createPolicy(@RequestBody RateLimitPolicyEntity policy) {
        return policyService.createPolicy(policy);
    }

    @PutMapping("/{id}")
    public RateLimitPolicyEntity updatePolicy(@PathVariable UUID id, @RequestBody RateLimitPolicyEntity policy) {
        return policyService.updatePolicy(id, policy);
    }

    @DeleteMapping("/{id}")
    public void deletePolicy(@PathVariable UUID id) {
        policyService.deletePolicy(id);
    }
}