# TODO: Full Rate Limiter SaaS Application

## Backend Enhancements
- [ ] Add TenantController for CRUD operations on tenants
- [ ] Add PolicyController for CRUD operations on rate limit policies (allow algorithm selection)
- [ ] Add EndpointPolicyController for managing endpoint-specific policies
- [ ] Add ApiKeyController for managing API keys per tenant
- [ ] Update security to allow tenant-specific API key management
- [ ] Add validation for algorithm selection based on tenant permissions (if needed)
- [ ] Add endpoints for tenant dashboard data (usage stats, etc.)

## Frontend Enhancements
- [ ] Install react-router-dom for routing
- [ ] Create Login page (API key based authentication)
- [ ] Create Dashboard page (tenant overview, usage stats)
- [ ] Create Policies page (list, create, edit policies with algorithm selection)
- [ ] Create Endpoints page (manage endpoint policies)
- [ ] Create API Keys page (manage tenant API keys)
- [ ] Update existing Test page to be part of the full app
- [ ] Add navigation and layout components
- [ ] Implement API calls for all backend endpoints

## Database/Configuration
- [ ] Ensure database schema supports all entities
- [ ] Add sample data for testing
- [ ] Update application.yml if needed

## Testing and Deployment
- [ ] Test full flow: tenant login, create policy, select algorithm, test rate limiting
- [ ] Update Docker compose for full stack
- [ ] Ensure frontend builds and serves correctly