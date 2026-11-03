# Bike Lease Application

A full-stack bike leasing platform built with a microservice architecture. The project includes:

- Angular frontend for the web application
- Spring Boot authentication service with JWT and refresh tokens
- Spring Boot bike catalog service for inventory and leasing data
- Spring Boot notification service for lease-related events via Kafka
- PostgreSQL for persistent data storage
- Kafka + Zookeeper for asynchronous event processing

This application is designed for managing bike inventory, user access, and leasing workflows in a multi-service setup.

## Table of contents

- Overview
- Features
- Project structure
- Prerequisites
- Recommended environment versions
- Quick start (Docker Compose)
- Manual startup
- Configuration details
- Service endpoints and ports
- Database details
- Authentication and authorization
- Typical usage flow
- Troubleshooting
- Useful commands

## Overview

The platform is split into four main parts:

1. Frontend: `bike-lease-ui`
   - Angular 21 single-page application
   - Provides login, dashboard, user list, and bike inventory screens
   - Uses JWT-based authentication with protected routes

2. Auth service: `auth-service`
   - Handles registration, login, logout-like token invalidation strategy, and refresh tokens
   - Uses PostgreSQL and Liquibase migrations
   - Exposes JWT-secured endpoints under `/api/v1/auth` and `/api/v1/users`

3. Bike catalog service: `bike-catalog`
   - Stores and manages available bikes, lease data, pricing, mileage, insurance info, and image metadata
   - Supports create, read, update, delete, and search operations
   - Protected by role-based access control

4. Notification service: `notification-service`
   - Consumes Kafka events
   - Persists notification records and templates
   - Intended for sending or logging lease or catalog related notifications

## Features

The application covers the core lifecycle of a bike leasing system:

- User login and registration
- JWT authentication and refresh token support
- Role-based access for users and admins
- Dashboard with summary cards
- User management list and retrieval APIs
- Bike catalog management with:
  - brand
  - model
  - variant
  - engine CC
  - price
  - lease amount
  - lease tenure
  - mileage
  - availability status
  - insurance details
  - image payload storage
- Search and pagination for bike inventory
- Kafka-based event processing for notifications
- PostgreSQL-backed persistence with Liquibase database migration scripts

## Project structure

```text
Leasing/
├── README.md
├── docker-compose.yaml
├── docker/
│   └── init/
│       └── init-databases.sql
├── auth-service/
│   ├── build.gradle.kts
│   ├── gradlew
│   ├── gradlew.bat
│   ├── gradle/
│   └── src/
│       ├── main/
│       └── test/
├── bike-catalog/
│   ├── build.gradle.kts
│   ├── gradlew
│   ├── gradlew.bat
│   ├── gradle/
│   └── src/
├── notification-service/
│   ├── build.gradle.kts
│   ├── gradlew
│   ├── gradlew.bat
│   ├── gradle/
│   └── src/
├── bike-lease-ui/
│   ├── package.json
│   ├── package-lock.json
│   ├── angular.json
│   ├── src/
│   └── node_modules/
└── .gitignore
```

## Prerequisites

Before running the project, make sure the following are installed:

- Git
- Java 21 JDK
- Gradle wrapper is included in each service, but Java 21 is still required
- Node.js 20+ and npm
- Docker Desktop or Docker Engine + Docker Compose
- A browser such as Chrome, Edge, or Firefox
- Access to localhost ports: 4200, 5432, 7001, 7002, 7003, 8085, 9092, 2181

### Recommended tool versions

- JDK: 21
- Node.js: 20 LTS or newer
- npm: 10+
- Angular CLI: managed by project dependencies
- PostgreSQL: 16 (as defined in Docker Compose)
- Kafka: Confluent 7.6.1 container image

## Quick start (recommended)

From the project root:

```bash
git clone https://github.com/CkanthG/Leasing.git
cd Leasing

# Windows PowerShell:
.\auth-service\gradlew.bat -p auth-service bootJar
.\bike-catalog\gradlew.bat -p bike-catalog bootJar
.\notification-service\gradlew.bat -p notification-service bootJar

# Build all images and start the complete stack:
docker compose up --build -d
```

The Gradle wrapper commands create the JAR files expected by the backend Dockerfiles. Compose then builds the three backend images and the Angular/Nginx frontend image, and starts the database, Kafka, Zookeeper, Kafka UI, and app containers.

On macOS/Linux, run the wrapper scripts instead:

```bash
./auth-service/gradlew -p auth-service bootJar
./bike-catalog/gradlew -p bike-catalog bootJar
./notification-service/gradlew -p notification-service bootJar
docker compose up --build -d
```

This will start the following services:

- PostgreSQL on `localhost:5432`
- Auth service on `http://localhost:7001`
- Bike catalog service on `http://localhost:7002`
- Notification service on `http://localhost:7003`
- Kafka UI on `http://localhost:8085`
- Angular frontend on `http://localhost:4200`

After the containers are running:

1. Open `http://localhost:4200`
2. Register a user through the API below, then log in (the frontend does not have a registration screen)
3. Visit dashboard, users, and inventory pages from the frontend navigation

### Register the first user

Registration is public and the current backend accepts the roles `USER` and `ADMIN`. To create an admin for managing catalog inventory:

```powershell
curl.exe -i -X POST http://localhost:7001/api/v1/auth/register `
  -H "Content-Type: application/json" `
  -d '{ "email": "admin@bikelease.com", "password": "ChangeThisPassword123!", "role": "ADMIN" }'
```

In macOS/Linux shells, replace PowerShell backticks with backslashes, or put the `curl` command on one line. Registration does not return the tokens; use the login form or `POST /api/v1/auth/login` to sign in.

### Build only, then deploy

To build the images without starting containers:

```bash
docker compose build
```

To deploy/start the built stack:

```bash
docker compose up -d
```

To rebuild changed application images and recreate containers:

```bash
# Rebuild backend JARs when Kotlin/backend source has changed.
.\auth-service\gradlew.bat -p auth-service bootJar
.\bike-catalog\gradlew.bat -p bike-catalog bootJar
.\notification-service\gradlew.bat -p notification-service bootJar

# Rebuild images, then recreate the containers.
docker compose up --build -d
```

The frontend image is built from `bike-lease-ui/Dockerfile` and does not need a separate host-side `npm install`. Its Angular production build is baked into the Nginx image. Backend Dockerfiles package the JARs produced under each service's `build/libs/` directory; therefore, run the Gradle commands before `docker compose build` after a clean checkout or after changing backend code.

To build one backend image individually, build its JAR first, then use that service's directory as the Docker build context (each Dockerfile copies its JAR from that context's `build/libs/` directory):

```bash
.\auth-service\gradlew.bat -p auth-service bootJar
docker build -t bike-lease-auth:latest .\auth-service

.\bike-catalog\gradlew.bat -p bike-catalog bootJar
docker build -t bike-lease-catalog:latest .\bike-catalog

.\notification-service\gradlew.bat -p notification-service bootJar
docker build -t bike-lease-notification:latest .\notification-service

docker build -t bike-lease-ui:latest .\bike-lease-ui
```

Use `./<service>/gradlew -p <service> bootJar` and `./<service>` as the Docker context on macOS/Linux. For deployment through this Compose file, use `docker compose build` / `docker compose up --build`; Compose builds each service with its configured context and image name. Individual `docker build` commands are useful for building or testing one image separately.

### Inspect and stop the deployment

```bash
docker compose ps
docker compose logs -f
docker compose logs -f auth-service bike-catalog-service notification-service bike-lease-ui
docker compose down
```

`docker compose down` keeps the PostgreSQL named volume. To also erase the persisted database data and recreate databases from the initialization SQL on the next start, use `docker compose down -v`. This is destructive and removes local database contents.

## Manual startup

If you prefer to run the services directly instead of using Docker Compose:

### 1) Start infrastructure services

Use Docker for PostgreSQL and Kafka only:

```bash
docker compose up postgres zookeeper kafka kafka-ui -d
```

This starts the database and event bus without launching the app services themselves.

### 2) Start the auth service

```bash
cd auth-service
./gradlew bootRun
```

Expected port:

- `7001`

Configuration defaults:

- database: `auth_db`
- username: `postgres`
- password: `password`
- host: `localhost`
- port: `5432`

You can override values at runtime:

```bash
DB_HOST=localhost DB_PORT=5432 DB_NAME_USER_SERVICE=auth_db DB_USER=postgres DB_PASSWORD=password ./gradlew bootRun
```

### 3) Start the bike catalog service

```bash
cd bike-catalog
./gradlew bootRun
```

Expected port:

- `7002`

Configuration defaults:

- database: `bike_catalog_db`
- username: `postgres`
- password: `password`

Optional runtime override:

```bash
DB_HOST=localhost DB_PORT=5432 DB_NAME_USER_SERVICE=bike_catalog_db DB_USER=postgres DB_PASSWORD=password ./gradlew bootRun
```

### 4) Start the notification service

```bash
cd notification-service
./gradlew bootRun
```

Expected port:

- `7003`

Configuration defaults:

- database: `notifications_db`
- username: `postgres`
- password: `password`

Optional runtime override:

```bash
DB_HOST=localhost DB_PORT=5432 DB_NAME_USER_SERVICE=notifications_db DB_USER=postgres DB_PASSWORD=password ./gradlew bootRun
```

### 5) Start the frontend

```bash
cd bike-lease-ui
npm install
npm start
```

The UI is served by Angular dev server on:

- `http://localhost:4200`

## Configuration details

### Root Docker Compose stack

The project includes a complete compose file at the repository root:

- `postgres` container using `postgres:16`
- `auth-service` container on port `7001`
- `bike-catalog-service` container on port `7002`
- `notification-service` container on port `7003`
- `zookeeper` on `2181`
- `kafka` on `9092` and internal `29092`
- `kafka-ui` on `8085`
- `bike-lease-ui` container on `4200`

### Shared database initialization

The SQL initializer creates the required databases:

```sql
CREATE DATABASE auth_db;
CREATE DATABASE bike_catalog_db;
CREATE DATABASE notifications_db;
```

These are automatically initialized when the Postgres container starts with the provided scripts in `docker/init`.

### JWT configuration

The auth and bike catalog services use a JWT secret configured in their `application.yaml` files:

```yaml
jwt:
  secret: "your-super-secure-random-secret-key-256-bit"
```

Important: in a real environment, replace this with an environment-specific secret and do not leave a development placeholder in production.

### Kafka configuration

The catalog and notification services use Kafka with the following default broker settings:

- `kafka:29092` when running inside Docker
- `localhost:9092` when running services outside the compose network

The notification service is configured as a consumer group:

- `notification-group`

Kafka UI is available at:

- `http://localhost:8085`

## Service endpoints and ports

### Frontend

- `http://localhost:4200` — Angular app

### Auth service

Base URL: `http://localhost:7001`

- `POST /api/v1/auth/login`
  - logs in a user
  - returns access and refresh tokens
- `POST /api/v1/auth/register`
  - creates a new user
- `POST /api/v1/auth/refresh`
  - refreshes JWT using a refresh token
- `GET /api/v1/users`
  - returns all users; requires authenticated user or admin privileges
- `GET /api/v1/users/{userId}`
  - fetches a user by ID
- `GET /api/v1/users/me`
  - fetches the currently authenticated user from the JWT

### Bike catalog service

Base URL: `http://localhost:7002`

- `POST /api/v1/bikeCatalog`
  - creates a bike catalog entry by admin only
  - accepts multipart form data
- `GET /api/v1/bikeCatalog/{bikeCatalogId}`
  - fetches one bike catalog item
- `GET /api/v1/bikeCatalog`
  - searches and paginates bike inventory
- `PUT /api/v1/bikeCatalog/{bikeCatalogId}`
  - updates a bike catalog entry by admin only
- `DELETE /api/v1/bikeCatalog/{bikeCatalogId}`
  - deletes a catalog item by admin only

### Notification service

Base URL: `http://localhost:7003`

This service primarily consumes Kafka notifications and persists them to the database. The notification schema includes:

- id
- event_id
- lease_id
- user_id
- email
- event_type
- subject
- message
- status
- retry_count
- created_at
- sent_at
- failure_reason

## Database details

### Auth database (`auth_db`)

Tables created via Liquibase:

- `users`
  - `id`
  - `email`
  - `password`
  - `role`
  - `enabled`
  - `created_at`
- `refresh_tokens`
  - `id`
  - `user_id`
  - `token`
  - `expires_at`
  - `revoked`
  - `created_at`

### Bike catalog database (`bike_catalog_db`)

Table:

- `BIKE_CATALOG`
  - `id`
  - `brand`
  - `model`
  - `variant`
  - `engine_cc`
  - `price`
  - `lease_amount`
  - `lease_tenure`
  - `mileage`
  - `images`
  - `availability_status`
  - `insurance_details`

### Notification database (`notifications_db`)

Tables:

- `NOTIFICATION`
- `EMAIL_TEMPLATE`

## Authentication and authorization

The auth service is the identity provider for the application.

- Public endpoints: `/api/v1/auth/**`
- Protected routes require a valid JWT token
- Roles used by the app include:
  - `ADMIN`
  - `USER`
  - `DEALER`
  - `SUPPORT`
  - `FINANCE`
- Access is controlled with Spring Security `@PreAuthorize` checks
- CORS is configured for `http://localhost:4200` and a local IP variant

### Example login flow

1. POST to `/api/v1/auth/login` with `email` and `password`
2. Receive an access token and refresh token
3. Attach the access token to the `Authorization` header as a Bearer token
4. Call protected APIs such as `/api/v1/users/me` or `/api/v1/bikeCatalog`

## Typical usage flow

A common flow for the app is:

1. Create or register a user via the auth service
2. Log in from the Angular frontend
3. View dashboard summary cards
4. Manage users via `/users`
5. Add or update bike inventory via `/inventory`
6. Bike catalog data is returned through the catalog service
7. Lease or notification events are emitted to Kafka and consumed by notification service

## Sample API calls

### Register a new user

```bash
curl -X POST http://localhost:7001/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@bikelease.com",
    "password": "StrongPass123!",
    "role": "ADMIN"
  }'
```

The current code accepts the enum values `USER` and `ADMIN`.

### Log in and get tokens

```bash
curl -X POST http://localhost:7001/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@bikelease.com",
    "password": "StrongPass123!"
  }'
```

Example response:

```json
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "3f7d8e33-..."
}
```

### Use the access token

```bash
curl http://localhost:7001/api/v1/users/me \
  -H "Authorization: Bearer <accessToken>"
```

### Refresh a token

```bash
curl -X POST http://localhost:7001/api/v1/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "<refresh-token>"
  }'
```

### Create a bike catalog item

```bash
curl -X POST http://localhost:7002/api/v1/bikeCatalog \
  -H "Authorization: Bearer <accessToken>" \
  -F "brand=KTM" \
  -F "model=390 Adventure" \
  -F "variant=X" \
  -F "engineCc=399" \
  -F "price=9500" \
  -F "leaseAmount=260" \
  -F "leaseTenure=24" \
  -F "mileage=1200" \
  -F "availabilityStatus=true" \
  -F "insuranceDetails=Comprehensive insurance" \
  -F "images=@/path/to/image.jpg"
```

## Troubleshooting

### Postgres connection issues

- Verify the database is running
- Ensure the Docker container has started successfully
- Check the credentials match the configured environment values
- Confirm the database names exist: `auth_db`, `bike_catalog_db`, `notifications_db`

### App startup fails due to Java version

- Ensure Java 21 is installed and active in your shell
- Check with:

```bash
java -version
```

### Frontend not loading

- Ensure Node.js and npm are installed
- Run:

```bash
cd bike-lease-ui
npm install
npm start
```

### Kafka connection issues

- Confirm Zookeeper and Kafka containers are running
- Validate that Kafka UI is reachable at `http://localhost:8085`
- If running outside Docker, set the Kafka bootstrap server to `localhost:9092`

### JWT or 401 unauthorized errors

- Confirm you are sending the `Authorization: Bearer <token>` header
- Ensure the token has not expired
- Use `/api/v1/auth/refresh` to get a new access token if needed

## Useful commands

### Start everything

```bash
docker compose up --build
```

### Stop everything

```bash
docker compose down
```

### Stop everything and remove volumes

```bash
docker compose down -v
```

### Build backend services manually

```bash
cd auth-service && ./gradlew build
cd ../bike-catalog && ./gradlew build
cd ../notification-service && ./gradlew build
```

### Run frontend production build

```bash
cd bike-lease-ui
npm install
npm run build
```

## Notes for developers

- The repo uses modular microservice boundaries rather than a single monolith.
- Liquibase handles database migrations for each service independently.
- The frontend is intentionally slim and relies on backend services for business logic and security.
- JWT tokens are used for authenticated requests, while refresh tokens are persisted in the auth database.
- For production, change the default JWT secret, database credentials, and service networking configuration.

## Summary

This project is a complete bike lease management application with a React-free Angular front end and multiple Spring Boot services. It is structured for realistic deployment, secure user access, inventory management, and asynchronous notification processing.

Before running in production, update secrets, environment variables, and external service connectivity so they match your target environment.
