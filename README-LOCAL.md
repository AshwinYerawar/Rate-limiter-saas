# Rate Limiter SaaS - Local Development Setup (Without Docker)

This guide explains how to run the Rate Limiter SaaS application locally without Docker.

## Prerequisites

- Java 17 or higher
- Node.js 16 or higher
- PostgreSQL 15
- Redis 7
- Maven 3.6+

## 1. Database Setup

### PostgreSQL Setup

1. Install PostgreSQL locally (or use a cloud instance)
2. Create a database named `ratelimiter`:
   ```sql
   CREATE DATABASE ratelimiter;
   ```
3. Create a user with password:
   ```sql
   CREATE USER postgres WITH PASSWORD '1234';
   GRANT ALL PRIVILEGES ON DATABASE ratelimiter TO postgres;
   ```

### Redis Setup

1. Install Redis locally
2. Start Redis server (default port 6379)

## 2. Backend Setup

### Database Schema

The application uses Hibernate with `ddl-auto: validate`, so you need to create the tables manually or change to `create-drop` for development.

For development, you can temporarily change `application.yml`:
```yaml
jpa:
  hibernate:
    ddl-auto: create-drop  # Change from 'validate' to 'create-drop'
```

### Build and Run

```bash
# Navigate to the root directory (where pom.xml is)
cd /path/to/ratelimiter-saas

# Build the application
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

## 3. Frontend Setup

### Install Dependencies

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install
```

### Development Server

```bash
# Start the development server
npm start
```

The frontend will start on `http://localhost:3000` and proxy API calls to `http://localhost:8080`

### Production Build

```bash
# Build for production
npm run build

# Serve the built files (requires a static server)
npx serve -s build -l 3000
```

## 4. Application URLs

- **Frontend**: http://localhost:3000
- **Backend API**: http://localhost:8080
- **API Documentation**: http://localhost:8080/swagger-ui.html (if enabled)

## 5. Sample Data

To test the application, you'll need to create some sample data:

### Create a Tenant
```bash
curl -X POST http://localhost:8080/api/v1/tenants \
  -H "Content-Type: application/json" \
  -d '{"name": "Test Company", "status": "ACTIVE"}'
```

### Create a Policy
```bash
curl -X POST http://localhost:8080/api/v1/policies \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Default Policy",
    "algorithm": "TOKEN_BUCKET",
    "limit": 100,
    "windowSeconds": 60,
    "tenant": {"id": "tenant-uuid-here"}
  }'
```

### Create an API Key
```bash
curl -X POST http://localhost:8080/api/v1/api-keys \
  -H "Content-Type: application/json" \
  -d '{
    "key": "test-api-key-123",
    "status": "ACTIVE",
    "tenant": {"id": "tenant-uuid-here"}
  }'
```

## 6. Testing Rate Limiting

Use the frontend interface at http://localhost:3000/test to test rate limiting, or use curl:

```bash
curl -X POST http://localhost:8080/api/v1/rate-limit/check \
  -H "Content-Type: application/json" \
  -H "X-API-Key: test-api-key-123" \
  -d '{"endpoint": "/api/users", "httpMethod": "GET"}'
```

## 7. Troubleshooting

### Backend Issues
- Check PostgreSQL connection: `psql -h localhost -U postgres -d ratelimiter`
- Check Redis connection: `redis-cli ping`
- Verify Java version: `java -version`
- Check Maven: `mvn -version`

### Frontend Issues
- Clear node_modules: `rm -rf node_modules && npm install`
- Check Node version: `node -version`
- Check npm version: `npm -version`

### Common Errors
- **Port already in use**: Change ports in `application.yml` or kill processes
- **Database connection failed**: Verify PostgreSQL credentials and database exists
- **Redis connection failed**: Ensure Redis is running on port 6379

## 8. Development Workflow

1. Start PostgreSQL and Redis
2. Start backend: `mvn spring-boot:run`
3. Start frontend: `cd frontend && npm start`
4. Access application at http://localhost:3000
5. Make changes and restart services as needed

## 9. IDE Setup

### IntelliJ IDEA
- Import as Maven project
- Set Java SDK to 17
- Run/debug configurations for Spring Boot

### VS Code
- Install Java Extension Pack
- Install Spring Boot Extension Pack
- Use integrated terminal for commands

## 10. Environment Variables (Optional)

You can override application.yml settings with environment variables:

```bash
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/ratelimiter
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=1234
export SPRING_DATA_REDIS_HOST=localhost
export SPRING_DATA_REDIS_PORT=6379
```

This setup allows you to develop and test the Rate Limiter SaaS application locally without Docker.