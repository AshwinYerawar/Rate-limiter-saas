# Rate Limiter SaaS

A complete SaaS application for rate limiting with multiple algorithms, multi-tenant support, and a React frontend for testing.

## Features

- Multiple rate limiting algorithms: Fixed Window, Sliding Window, Token Bucket, Leaky Bucket
- Multi-tenant architecture
- PostgreSQL database for policies
- Redis for caching
- Spring Boot backend
- React frontend for testing
- Docker containerization

## Prerequisites

- Docker and Docker Compose installed

## Installation and Setup

1. Clone the repository
2. Navigate to the project directory
3. Run the application:

```bash
docker-compose up --build
```

This will start all services:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
- PostgreSQL: localhost:5432
- Redis: localhost:6379

## Testing the API

### Via Frontend

1. Open http://localhost:3000 in your browser
2. Fill in the form:
   - **Endpoint**: `/api/users` (or any endpoint)
   - **HTTP Method**: GET, POST, PUT, DELETE
   - **API Key**: `test-api-key-123` (pre-configured)
3. Click "Test Single Request" to make one request
4. Or use "Test 5/10/20 Requests" for bulk testing

The frontend will display:
- Whether the request was allowed or blocked
- Current limit
- Remaining requests
- Algorithm used
- Reset time

### Via API Directly

```bash
curl -X POST http://localhost:8080/api/v1/rate-limit/check \
  -H "Content-Type: application/json" \
  -H "X-API-Key: test-api-key-123" \
  -d '{
    "endpoint": "/api/users",
    "httpMethod": "GET"
  }'
```

Response:
```json
{
  "allowed": true,
  "limit": 10,
  "remaining": 9,
  "resetAt": "2024-01-01T12:00:00Z",
  "algorithm": "FIXED_WINDOW"
}
```

## Architecture

- **Frontend**: React app served by Nginx, proxies API calls to backend
- **Backend**: Spring Boot application with REST API
- **Database**: PostgreSQL for storing rate limit policies
- **Cache**: Redis for storing rate limit counters
- **Network**: All services communicate via Docker network

## Development

For development without Docker:

1. Start PostgreSQL and Redis locally
2. Update `application.yml` to use `localhost` instead of service names
3. Run backend: `./mvnw spring-boot:run`
4. Run frontend: `cd frontend && npm start`

## Configuration

- Database: PostgreSQL with user `postgres`, password `1234`, database `ratelimiter`
- Redis: Default configuration
- API Key: `test-api-key-123` (configured in ApiKeyStore)

## Rate Limiting Algorithms

1. **Fixed Window**: Limits requests per fixed time window
2. **Sliding Window**: More accurate windowing using rolling time
3. **Token Bucket**: Accumulates tokens over time
4. **Leaky Bucket**: Smooths out request bursts

## Multi-Tenant Support

The application supports multiple tenants with isolated rate limiting policies.