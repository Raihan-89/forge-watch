# 🏭 ForgeWatch - Factory Floor Monitoring System

<div align="center">
  <p><strong>Production-Ready Microservices Backend for Industrial Factory Monitoring</strong></p>
  <p>
    <img src="https://img.shields.io/badge/Java-21-blue.svg" alt="Java 21"/>
    <img src="https://img.shields.io/badge/Spring%20Boot-3.4.5-brightgreen.svg" alt="Spring Boot 3.4.5"/>
    <img src="https://img.shields.io/badge/Spring%20Cloud%20Gateway-2024.0.1-blueviolet.svg" alt="Spring Cloud Gateway"/>
    <img src="https://img.shields.io/badge/PostgreSQL-15-336791.svg" alt="PostgreSQL 15"/>
    <img src="https://img.shields.io/badge/RabbitMQ-3-FF6600.svg" alt="RabbitMQ"/>
    <img src="https://img.shields.io/badge/Redis-7-DC382D.svg" alt="Redis 7"/>
    <img src="https://img.shields.io/badge/Twilio-Integration-red.svg" alt="Twilio Integration"/>
    <img src="https://img.shields.io/badge/Docker-Compose-2496ED.svg" alt="Docker Compose"/>
  </p>
</div>

## 📋 Overview

ForgeWatch is a **production-ready, enterprise-grade microservices backend** designed for factory floor monitoring and management. Built with **Spring Boot 3.4.5** and a cloud-native architecture, it provides comprehensive capabilities for tracking machines, managing production shifts, reporting defects, and sending real-time alerts via email and SMS.

The system follows **industry best practices** including event-driven architecture, role-based access control, JWT authentication, centralized API gateway, distributed caching, and containerized deployment.

### 🌟 Key Features

| Feature | Description |
|---------|-------------|
| **🔐 JWT Authentication** | Secure login with role-based access (ADMIN, SUPERVISOR, WORKER) |
| **🏗️ Microservices Architecture** | 6 loosely-coupled services with API Gateway |
| **📡 Event-Driven Messaging** | RabbitMQ for asynchronous event processing |
| **🔔 Multi-Channel Alerts** | Email (SMTP) + SMS (Twilio) notifications |
| **📊 Production Tracking** | Shift planning, machine status, defect management |
| **🔍 API Documentation** | Swagger UI / OpenAPI 3.0 for all endpoints |
| **📈 Health Monitoring** | Spring Boot Actuator with health checks and metrics |
| **🔒 Security Best Practices** | Password validation, UUID public IDs, input sanitization |
| **🐳 Containerized** | Docker Compose for one-command deployment |

---

## 🏛️ System Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Applications                       │
│         (Web App / Mobile App / Postman / Curl)             │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP/HTTPS (Authorization: Bearer JWT)
                           ▼
┌───────────────────────────────────────────────────────────────┐
│                  API GATEWAY (Port 8080)                      │
│              Spring Cloud Gateway + JWT Filter                │
│                                                               │
│  Routes: /api/auth/** → auth-service                          │
│          /api/machines/** → machine-service                   │
│          /api/shifts/** → shift-service                       │
│          /api/defects/** → defect-service                     │
│          /api/notifications/** → alert-service                │
└──┬───────────┬───────────┬───────────┬───────────┬────────────┘
   │           │           │           │           │
   ▼           ▼           ▼           ▼           ▼
┌──────┐  ┌──────────┐  ┌──────┐  ┌────────┐  ┌──────────────┐
│Auth  │  │ Machine  │  │Shift │  │ Defect │  │   Alert      │
│:8081 │  │ :8082    │  │:8083 │  │ :8084  │  │   :8085      │
└──┬───┘  └──┬───────┘  └──┬───┘  └───┬────┘  └──────┬───────┘
   │         │              │          │               │
   │         │         ┌────┘          │         ┌─────┘
   ▼         ▼         ▼              ▼         ▼
┌──────────────────────────────────────────────────────────────┐
│                     PostgreSQL 15                             │
│     Schemas: auth, machine, shift, defect                    │
└──────────────────────────────────────────────────────────────┘
   │                        │
   ▼                        ▼
┌──────────────────────────────────────────────────────────────┐
│  RabbitMQ                    │  Redis 7                      │
│  machine.queue              │  Password Reset Tokens        │
│  defect.queue               │  (TTL: 15 min)                │
└─────────────────────────────┴────────────────────────────────┘
```

> 📁 **Architecture Diagram:** See [`resource/images/architecture.puml`](resource/images/architecture.puml) for a detailed PlantUML diagram. Generate the PNG using: `java -jar plantuml.jar -tpng resource/images/architecture.puml`

---

## 🔧 Tech Stack

### Core Framework
| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Runtime platform |
| Spring Boot | 3.4.5 | Backend framework |
| Spring Cloud Gateway | 2024.0.1 | API Gateway |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.x | Database access |
| Spring AMQP | 3.x | RabbitMQ integration |

### Database & Messaging
| Technology | Port | Purpose |
|------------|------|---------|
| PostgreSQL | 5433 (host) / 5432 (container) | Main database |
| RabbitMQ | 5672 (AMQP) / 15672 (Management UI) | Message broker |
| Redis | 6379 | Token caching |

### External Services
| Service | Purpose |
|---------|---------|
| Gmail SMTP | Email notifications |
| Twilio | SMS alerts & OTP verification |

### Third-Party Libraries
| Library | Purpose |
|---------|---------|
| JJWT (0.12.6) | JWT token management |
| SpringDoc OpenAPI (2.8.6) | API documentation |
| Lombok | Boilerplate code reduction |
| Twilio Java SDK (10.1.0) | SMS integration |

---

## 📁 Project Structure

```
forge-watch/
├── common/                          # Shared library module
│   └── src/main/java/com/forgewatch/common/
│       ├── config/                  # GlobalExceptionHandler, OpenApiConfig, JpaAuditingConfig
│       ├── dto/                     # ApiResponse, ErrorResponse, PagedResponse
│       └── exception/               # ResourceNotFoundException, DuplicateResourceException, InvalidOperationException
│
├── api-gateway/                     # Spring Cloud Gateway (Port 8080)
│   └── src/main/java/com/forgewatch/api_gateway/
│       ├── config/                  # CORS configuration
│       ├── filter/                  # JWT authentication filter
│       └── util/                    # JWT utility
│
├── auth-service/                    # Authentication Service (Port 8081)
│   ├── controller/                  # Auth endpoints
│   ├── service/                     # Business logic
│   ├── security/                    # JWT, filters, user details
│   ├── repository/                  # User repository
│   ├── entity/                      # User entity with UUID publicId
│   ├── dto/                         # Request/Response DTOs
│   └── enums/                       # Role enum
│
├── machine-service/                 # Machine Management (Port 8082)
│   ├── controller/                  # Machine CRUD endpoints
│   ├── service/                     # Business logic with event publishing
│   ├── repository/                  # Machine repository
│   ├── entity/                      # Machine entity with auditing
│   ├── dto/                         # Request/Response DTOs
│   ├── config/                      # RabbitMQ configuration
│   ├── messaging/                   # Event publisher
│   └── enums/                       # MachineStatus enum
│
├── shift-service/                   # Shift Management (Port 8083)
│   ├── controller/                  # Shift CRUD endpoints
│   ├── service/                     # Business logic
│   ├── repository/                  # Shift repository
│   ├── entity/                      # Shift entity with worker assignments
│   ├── dto/                         # Request/Response DTOs
│   └── enums/                       # ShiftType enum
│
├── defect-service/                  # Defect Management (Port 8084)
│   ├── controller/                  # Defect CRUD endpoints
│   ├── service/                     # Business logic with alert publishing
│   ├── repository/                  # Defect repository
│   ├── entity/                      # Defect entity
│   ├── dto/                         # Request/Response DTOs
│   ├── config/                      # RabbitMQ configuration
│   ├── messaging/                   # Event publisher
│   └── enums/                       # DefectStatus, DefectSeverity enums
│
├── alert-service/                   # Notification Service (Port 8085)
│   ├── controller/                  # Notification endpoints
│   ├── service/                     # Email, SMS, Password reset services
│   ├── listener/                    # RabbitMQ event listeners
│   ├── config/                      # Twilio, Redis, RabbitMQ configurations
│   └── dto/                         # Alert DTOs
│
├── resource/images/                 # Architecture and flow diagrams
├── docker-compose.yml               # Full stack orchestration
├── init-db.sql                      # Database schema initialization
└── pom.xml                          # Maven parent POM
```

---

## 🚀 Quick Start

### Prerequisites

- **Docker** and **Docker Compose** (recommended for full stack)
- **Java 21** (for local development)
- **Maven Wrapper** (included as `mvnw` / `mvnw.cmd`)

### Environment Variables

Create a `.env` file in the project root:

```env
# Gmail SMTP (use App Password, not regular password)
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Twilio Configuration
TWILIO_ACCOUNT_SID=your_account_sid
TWILIO_AUTH_TOKEN=your_auth_token
TWILIO_VERIFY_SERVICE_SID=your_verify_service_sid
TWILIO_FROM_PHONE_NUMBER=+1234567890
TWILIO_MESSAGING_SERVICE_SID=MGxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

# Notification Recipients
SUPERVISOR_EMAIL=supervisor@factory.com
SUPERVISOR_PHONE=+8801XXXXXXXXX
```

### Deploy with Docker (Recommended)

```bash
# 1. Build all services
./mvnw clean package -DskipTests

# 2. Start the entire stack
docker compose up -d --build

# 3. Check status
docker compose ps

# 4. View logs
docker compose logs -f api-gateway
```

### Local Development

```bash
# 1. Start infrastructure
docker compose up -d forgewatchdb rabbitmq redis

# 2. Start services individually (in separate terminals)
./mvnw -pl auth-service spring-boot:run
./mvnw -pl machine-service spring-boot:run
./mvnw -pl shift-service spring-boot:run
./mvnw -pl defect-service spring-boot:run
./mvnw -pl alert-service spring-boot:run
./mvnw -pl api-gateway spring-boot:run
```

### Verify Deployment

```bash
# Health checks
curl http://localhost:8080/actuator/health
curl http://localhost:8081/actuator/health
curl http://localhost:8082/actuator/health
# ... (all services should return {"status": "UP"})

# Swagger UI
open http://localhost:8080/swagger-ui.html
```

---

## 📖 API Documentation

### Swagger UI

Each service exposes its own Swagger UI:

| Service | Swagger URL |
|---------|-------------|
| API Gateway | `http://localhost:8080/swagger-ui.html` |
| Auth Service | `http://localhost:8081/swagger-ui.html` |
| Machine Service | `http://localhost:8082/swagger-ui.html` |
| Shift Service | `http://localhost:8083/swagger-ui.html` |
| Defect Service | `http://localhost:8084/swagger-ui.html` |
| Alert Service | `http://localhost:8085/swagger-ui.html` |

### Authentication Flow

```mermaid
sequenceDiagram
    Client->>API Gateway: POST /api/auth/register
    API Gateway->>Auth Service: Forward request
    Auth Service->>PostgreSQL: Save user
    Auth Service-->>Auth Service: Generate JWT
    Auth Service-->>API Gateway: AuthResponse with token
    API Gateway-->>Client: 201 Created + JWT

    Note over Client,API Gateway: Login with credentials

    Client->>API Gateway: POST /api/auth/login
    API Gateway->>Auth Service: Forward request
    Auth Service-->>Auth Service: Validate credentials
    Auth Service-->>API Gateway: AuthResponse with JWT
    API Gateway-->>Client: 200 OK + Bearer Token

    Note over Client,API Gateway: Authenticated requests

    Client->>API Gateway: GET /api/machines (Authorization: Bearer <JWT>)
    API Gateway-->>API Gateway: Validate JWT
    API Gateway->>Machine Service: Forward + user context headers
    Machine Service-->>API Gateway: Response data
    API Gateway-->>Client: 200 OK
```

### Public Endpoints (No JWT Required)

```http
POST /api/auth/register
POST /api/auth/login
POST /api/notifications/forgot-password
POST /api/notifications/validate-token
POST /api/notifications/reset-password
POST /api/notifications/send-otp
POST /api/notifications/verify-otp
```

### Protected Endpoints (JWT Required)

All other endpoints require:
```http
Authorization: Bearer <jwt-token>
```

#### Machines

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/machines` | Register a new machine |
| `GET` | `/api/machines` | Get all machines |
| `GET` | `/api/machines/paged?page=0&size=20` | Get machines with pagination |
| `GET` | `/api/machines/{id}` | Get machine by database ID |
| `GET` | `/api/machines/public/{publicId}` | Get machine by public ID (e.g., MCH-XXXXXXXX) |
| `GET` | `/api/machines/department/{department}` | Get machines by department |
| `GET` | `/api/machines/status/{status}` | Get machines by operational status |
| `PUT` | `/api/machines/{id}/status?status=BREAKDOWN` | Update machine status (triggers alerts) |
| `DELETE` | `/api/machines/{id}` | Delete a machine |

#### Shifts

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/shifts` | Create a new shift |
| `GET` | `/api/shifts` | Get all shifts |
| `GET` | `/api/shifts/{id}` | Get shift by database ID |
| `GET` | `/api/shifts/public/{publicId}` | Get shift by public ID (e.g., SFT-XXXXXXXX) |
| `GET` | `/api/shifts/department/{department}` | Get shifts by department |
| `GET` | `/api/shifts/date/{date}` | Get shifts by date (ISO format) |
| `GET` | `/api/shifts/department/{department}/date/{date}` | Filter by department and date |
| `PUT` | `/api/shifts/{id}/production?actualProduction=450` | Update production output |
| `DELETE` | `/api/shifts/{id}` | Delete a shift |

#### Defects

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/defects` | Report a new defect |
| `GET` | `/api/defects` | Get all defects |
| `GET` | `/api/defects/{id}` | Get defect by database ID |
| `GET` | `/api/defects/public/{publicId}` | Get defect by public ID (e.g., DFT-XXXXXXXX) |
| `GET` | `/api/defects/machine/{machineCode}` | Get defects by machine code |
| `GET` | `/api/defects/department/{department}` | Get defects by department |
| `GET` | `/api/defects/severity/{severity}` | Get defects by severity (LOW/MEDIUM/HIGH/CRITICAL) |
| `GET` | `/api/defects/status/{status}` | Get defects by status (OPEN/IN_PROGRESS/RESOLVED/CLOSED) |
| `PUT` | `/api/defects/{id}/resolve` | Resolve a defect |
| `DELETE` | `/api/defects/{id}` | Delete a defect |

#### Notifications

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/notifications/forgot-password` | Request password reset |
| `POST` | `/api/notifications/validate-token` | Validate reset token |
| `POST` | `/api/notifications/reset-password` | Reset password with token |
| `POST` | `/api/notifications/send-otp` | Send OTP to phone number |
| `POST` | `/api/notifications/verify-otp` | Verify OTP code |

### Request/Response Examples

#### Register User
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "fullName": "Factory Supervisor",
  "username": "supervisor1",
  "email": "supervisor@factory.com",
  "password": "Secure@123",
  "department": "Casting",
  "phoneNumber": "+8801XXXXXXXXX"
}
```

**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600000,
    "publicId": "USR-A1B2C3D4",
    "fullName": "Factory Supervisor",
    "email": "supervisor@factory.com",
    "role": "WORKER",
    "department": "Casting"
  },
  "timestamp": "2026-07-25T12:00:00"
}
```

#### Register Machine
```http
POST http://localhost:8080/api/machines
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "machineCode": "MCH-001",
  "machineName": "Hydraulic Press",
  "department": "Casting",
  "location": "Line A",
  "description": "Main hydraulic press machine for metal forming"
}
```

#### Trigger Breakdown Alert
```http
PUT http://localhost:8080/api/machines/1/status?status=BREAKDOWN
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

This will:
1. Update the machine status to BREAKDOWN
2. Publish a RabbitMQ event
3. Alert Service sends email + SMS to supervisor

#### Report Critical Defect
```http
POST http://localhost:8080/api/defects
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
  "machineCode": "MCH-001",
  "department": "Casting",
  "title": "Hydraulic oil leakage",
  "description": "Major oil leak detected near pressure line. Immediate shutdown recommended.",
  "severity": "CRITICAL"
}
```

**Data Types:**

| Enum | Values |
|------|--------|
| `MachineStatus` | `RUNNING`, `IDLE`, `MAINTENANCE`, `BREAKDOWN` |
| `ShiftType` | `MORNING`, `AFTERNOON`, `NIGHT` |
| `DefectStatus` | `OPEN`, `IN_PROGRESS`, `RESOLVED`, `CLOSED` |
| `DefectSeverity` | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |
| `Role` | `ADMIN`, `SUPERVISOR`, `WORKER` |

---

## 🗄️ Database Schema

The system uses PostgreSQL with separate schemas per service:

```sql
-- Schemas
CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS machine;
CREATE SCHEMA IF NOT EXISTS shift;
CREATE SCHEMA IF NOT EXISTS defect;
```

Hibernate `ddl-auto=update` automatically manages table creation/updates.

### Entity Relationships

- **User** → Auth schema — Role-based access (ADMIN, SUPERVISOR, WORKER)
- **Machine** → Machine schema — Tracks operational status and maintenance
- **Shift** → Shift schema — Linked to workers via `shift_workers` join table
- **Defect** → Defect schema — Referenced by machine code and reporter email

---

## 🔄 Event-Driven Flow

### Machine Breakdown Flow
```
1. Client → PUT /api/machines/{id}/status?status=BREAKDOWN
2. Machine Service updates status
3. Machine Service publishes to machine.exchange → machine.queue
4. Alert Service consumes the event
5. Email sent to supervisor via Gmail SMTP
6. SMS sent to supervisor via Twilio
```

### Defect Alert Flow
```
1. Client → POST /api/defects (with severity: HIGH or CRITICAL)
2. Defect Service saves the defect
3. Defect Service publishes to defect.exchange → defect.queue
4. Alert Service consumes the event
5. Email + SMS notifications sent to supervisor
```

### Password Reset Flow
```
1. Client → POST /api/notifications/forgot-password
2. Alert Service generates UUID token
3. Token stored in Redis with 15-minute TTL
4. Reset link sent via email
5. Client → POST /api/notifications/reset-password with token
```

---

## 🔐 Security Features

### Authentication & Authorization
- **JWT-based** authentication with configurable expiration
- **BCrypt password hashing** with strength-based encoding
- **Role-based access** (ADMIN, SUPERVISOR, WORKER)
- **Input validation** with comprehensive constraints
  - Password strength requirements (uppercase, lowercase, digit, special char)
  - Email format validation
  - Phone number validation (E.164 format)
  - Username format validation

### API Security
- **UUID public IDs** instead of sequential IDs for external references
- **Consistent error responses** (no stack trace leakage)
- **API Gateway** as single entry point — services not directly exposed
- **CORS configuration** (customizable for production)
- **Input sanitization** via Jakarta Validation annotations

### Infrastructure Security
- **Environment variables** for all secrets (no hardcoded credentials)
- **PostgreSQL** with separate schemas per service (logical isolation)
- **Docker networking** with internal bridge network

---

## 🧪 Error Handling

The system uses a centralized error handling approach via `@RestControllerAdvice`:

| HTTP Status | Error Type | Scenario |
|-------------|------------|----------|
| `400 Bad Request` | `InvalidOperationException` | Invalid operations, validation failures |
| `400 Bad Request` | Validation Error | Invalid input format, missing fields |
| `404 Not Found` | `ResourceNotFoundException` | Resource not found by ID or publicId |
| `409 Conflict` | `DuplicateResourceException` | Duplicate email, username, machine code |
| `500 Internal Server Error` | Generic Exception | Unexpected server errors |

**Error Response Format:**
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Machine not found with id: 999",
  "path": "/api/machines/999",
  "timestamp": "2026-07-25T12:00:00"
}
```

**Validation Error Response:**
```json
{
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed. Check validationErrors for details.",
  "path": "/api/auth/register",
  "timestamp": "2026-07-25T12:00:00",
  "validationErrors": [
    {
      "field": "password",
      "message": "Password must contain at least one digit, one lowercase, one uppercase, and one special character",
      "rejectedValue": "weak"
    }
  ]
}
```

---

## 📊 Monitoring & Health

Each service exposes Spring Boot Actuator endpoints:

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Health check with database, RabbitMQ, Redis status |
| `/actuator/info` | Application info (name, version, description) |
| `/actuator/metrics` | JVM, system, and request metrics |
| `/actuator/env` | Environment properties (filtered) |

---

## 🐳 Docker Configuration

```yaml
# Infrastructure
- forgewatchdb (PostgreSQL 15) :5433
- rabbitmq (3-management) :5672, :15672
- redis (7) :6379

# Microservices
- auth-service :8081
- api-gateway :8080
- machine-service :8082
- shift-service :8083
- defect-service :8084
- alert-service :8085
```

### Useful Docker Commands

```bash
# Start all services
docker compose up -d --build

# View logs for a specific service
docker compose logs -f api-gateway

# Stop all services
docker compose down

# Stop and remove volumes (reset database)
docker compose down -v

# Scale a service
docker compose up -d --scale shift-service=2
```

---

## ⚙️ Configuration Reference

### Auth Service (`application.properties`)
| Property | Default | Description |
|----------|---------|-------------|
| `jwt.secret` | `JWT_SECRET` env | JWT signing key |
| `jwt.expiration` | `3600000` | Token TTL (ms) |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5433/forgewatchdb` | Database URL |

### Machine / Defect Services
| Property | Default | Description |
|----------|---------|-------------|
| `spring.rabbitmq.host` | `localhost` | RabbitMQ host |
| `spring.rabbitmq.port` | `5672` | RabbitMQ port |

### Alert Service
| Property | Default | Description |
|----------|---------|-------------|
| `twilio.account-sid` | `TWILIO_ACCOUNT_SID` env | Twilio account SID |
| `twilio.auth-token` | `TWILIO_AUTH_TOKEN` env | Twilio auth token |
| `twilio.verify.service-sid` | `TWILIO_VERIFY_SERVICE_SID` env | Verify service SID |
| `spring.mail.username` | `MAIL_USERNAME` env | Gmail SMTP username |
| `spring.mail.password` | `MAIL_PASSWORD` env | Gmail SMTP password |
| `notification.supervisor.email` | `SUPERVISOR_EMAIL` env | Alert recipient email |
| `notification.supervisor.phone` | `SUPERVISOR_PHONE` env | Alert recipient phone |

---

## 🧰 Development

### Build All Services
```bash
./mvnw clean package -DskipTests
```

### Run Tests
```bash
./mvnw test
```

### Build Single Service
```bash
./mvnw -pl machine-service clean package -DskipTests
```

---

## 📝 Project Roadmap

- [x] Authentication & Authorization (JWT)
- [x] Machine management with status tracking
- [x] Shift planning and production tracking
- [x] Defect reporting and resolution workflow
- [x] Email and SMS alert notifications
- [x] API Gateway with centralized routing
- [x] Event-driven architecture (RabbitMQ)
- [x] Password reset with Redis token storage
- [x] OTP verification via Twilio
- [x] Swagger/OpenAPI documentation
- [x] Health monitoring (Actuator)
- [x] UUID public IDs for security
- [x] Global exception handling
- [x] Input validation and sanitization
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Kubernetes deployment manifests
- [ ] Grafana dashboards for metrics
- [ ] ELK stack for centralized logging
- [ ] Frontend web application (React/Angular)
- [ ] Rate limiting and API throttling
- [ ] Service discovery (Eureka/Consul)

---

## 👨‍💻 Author

**Md. Raihan Shikder** (@Raihan-89)

- GitHub: [https://github.com/Raihan-89](https://github.com/Raihan-89)

---

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## 🙏 Support

For support, feature requests, or bug reports, please open an issue on the GitHub repository or contact the author.
