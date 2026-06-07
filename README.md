# ForgeWatch

ForgeWatch is a Spring Boot microservices backend for factory-floor monitoring. It tracks users, machines, shifts, production defects, and operational alerts. The system is designed around a central API Gateway, service-specific PostgreSQL schemas, RabbitMQ event delivery, Redis-backed password reset tokens, email notifications, and Twilio-based OTP/custom SMS support.

## Architecture

The project is a Maven multi-module workspace:

| Module | Port | Responsibility |
| --- | ---: | --- |
| `api-gateway` | `8080` | Single entry point, request routing, JWT validation, user context forwarding |
| `auth-service` | `8081` | User registration, login, JWT issuing |
| `machine-service` | `8082` | Machine registration, status tracking, breakdown event publishing |
| `shift-service` | `8083` | Shift planning, department/date lookup, production updates |
| `defect-service` | `8084` | Defect reporting, filtering, resolution, high-severity event publishing |
| `alert-service` | `8085` | RabbitMQ event listeners, supervisor email/SMS alerts, OTP, password reset |

Supporting infrastructure:

| Service | Host Port | Use |
| --- | ---: | --- |
| PostgreSQL | `5433` | Main database, container port `5432` |
| RabbitMQ | `5672` | Event broker |
| RabbitMQ Management | `15672` | Broker UI |
| Redis | `6379` | Password reset token storage |

## Main Flow

1. A user registers or logs in through `auth-service`.
2. The client sends the returned JWT as `Authorization: Bearer <token>` to protected API routes.
3. `api-gateway` validates the JWT and forwards user context headers:
   - `X-User-Email`
   - `X-User-Role`
   - `X-User-Department`
4. `machine-service` publishes a RabbitMQ event when a machine status becomes `BREAKDOWN`.
5. `defect-service` publishes a RabbitMQ event when a reported defect has severity `HIGH` or `CRITICAL`.
6. `alert-service` consumes those events and sends supervisor notifications.

## Tech Stack

- Java 21
- Spring Boot 3.4.5
- Spring Cloud Gateway
- Spring Security and JWT
- Spring Data JPA
- PostgreSQL 15
- RabbitMQ
- Redis
- Spring Mail
- Twilio Java SDK
- Docker Compose
- Maven multi-module build

## Prerequisites

- Docker and Docker Compose
- Java 21
- Maven Wrapper is included as `mvnw` / `mvnw.cmd`

On Windows, if Maven wrapper reports `JAVA_HOME` is not set, set it to your JDK path:

```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
```

## Environment Variables

Create a `.env` file in the repository root before starting Docker Compose:

```env
MAIL_USERNAME=your-gmail-address@gmail.com
MAIL_PASSWORD=your-gmail-app-password

TWILIO_ACCOUNT_SID=your-twilio-account-sid
TWILIO_AUTH_TOKEN=your-twilio-auth-token
TWILIO_VERIFY_SERVICE_SID=your-twilio-verify-service-sid

SUPERVISOR_EMAIL=supervisor@example.com
SUPERVISOR_PHONE=+8801XXXXXXXXX

# Optional: required only for custom breakdown/defect SMS text.
# Twilio Verify Service can only send OTP messages.
TWILIO_FROM_PHONE_NUMBER=
TWILIO_MESSAGING_SERVICE_SID=
```

Notes:

- `TWILIO_VERIFY_SERVICE_SID` is used for OTP only.
- Custom SMS alert bodies require either `TWILIO_FROM_PHONE_NUMBER` or `TWILIO_MESSAGING_SERVICE_SID`.
- If you only have a Twilio Verify Service, OTP will work but breakdown/defect SMS cannot contain custom alert text.
- For Gmail SMTP, use an app password, not your normal Gmail password.

## Build and Run with Docker

Build the Spring Boot jars first:

```powershell
.\mvnw.cmd clean package -DskipTests
```

Start all services:

```powershell
docker compose up -d --build
```

Check status:

```powershell
docker compose ps
```

View logs:

```powershell
docker compose logs -f api-gateway
docker compose logs -f alert-service
```

Stop the stack:

```powershell
docker compose down
```

Stop and remove database data:

```powershell
docker compose down -v
```

## Database Setup

PostgreSQL starts with database:

```text
forgewatchdb
```

The `init-db.sql` file creates these schemas:

```sql
auth
machine
shift
defect
```

Each Spring service uses Hibernate `ddl-auto=update` to create/update its own tables.

## API Gateway

Use the gateway for normal API calls:

```text
http://localhost:8080
```

The gateway routes:

| Path | Service |
| --- | --- |
| `/api/auth/**` | `auth-service` |
| `/api/machines/**` | `machine-service` |
| `/api/shifts/**` | `shift-service` |
| `/api/defects/**` | `defect-service` |
| `/api/notifications/**` | `alert-service` |

Open endpoints:

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/notifications/forgot-password`
- `POST /api/notifications/validate-token`
- `POST /api/notifications/reset-password`
- `POST /api/notifications/send-otp`
- `POST /api/notifications/verify-otp`

All other endpoints require:

```http
Authorization: Bearer <jwt-token>
```

## API Examples

### Register

```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json
```

```json
{
  "fullName": "Factory Supervisor",
  "username": "supervisor1",
  "email": "supervisor@example.com",
  "password": "password123",
  "department": "Casting"
}
```

### Login

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

```json
{
  "email": "supervisor@example.com",
  "password": "password123"
}
```

The response contains a JWT token.

### Register a Machine

```http
POST http://localhost:8080/api/machines
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

```json
{
  "machineCode": "MCH-001",
  "machineName": "Hydraulic Press",
  "department": "Casting",
  "location": "Line A",
  "description": "Main press machine"
}
```

### Trigger a Machine Breakdown Alert

```http
PUT http://localhost:8080/api/machines/1/status?status=BREAKDOWN
Authorization: Bearer <jwt-token>
```

This publishes a RabbitMQ event to `machine.queue`. `alert-service` consumes the event and sends supervisor notifications.

Supported machine statuses:

```text
RUNNING, IDLE, MAINTENANCE, BREAKDOWN
```

### Create a Shift

```http
POST http://localhost:8080/api/shifts
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

```json
{
  "shiftType": "MORNING",
  "department": "Casting",
  "shiftDate": "2026-06-07",
  "supervisorEmail": "supervisor@example.com",
  "workerEmails": ["worker1@example.com", "worker2@example.com"],
  "productionTarget": 500
}
```

Supported shift types:

```text
MORNING, AFTERNOON, NIGHT
```

### Report a Defect

```http
POST http://localhost:8080/api/defects
Authorization: Bearer <jwt-token>
Content-Type: application/json
```

```json
{
  "machineCode": "MCH-001",
  "department": "Casting",
  "title": "Oil leakage",
  "description": "Hydraulic oil leaking near pressure line",
  "severity": "HIGH"
}
```

`HIGH` and `CRITICAL` defects publish a RabbitMQ event to `defect.queue`.

Supported defect severities:

```text
LOW, MEDIUM, HIGH, CRITICAL
```

Supported defect statuses:

```text
OPEN, IN_PROGRESS, RESOLVED, CLOSED
```

### OTP

```http
POST http://localhost:8080/api/notifications/send-otp
Content-Type: application/json
```

```json
{
  "phoneNumber": "+8801XXXXXXXXX"
}
```

```http
POST http://localhost:8080/api/notifications/verify-otp
Content-Type: application/json
```

```json
{
  "phoneNumber": "+8801XXXXXXXXX",
  "otpCode": "123456"
}
```

## Useful Endpoints

### Machines

- `POST /api/machines`
- `GET /api/machines`
- `GET /api/machines/{id}`
- `GET /api/machines/department/{department}`
- `GET /api/machines/status/{status}`
- `PUT /api/machines/{id}/status?status=BREAKDOWN`
- `DELETE /api/machines/{id}`

### Shifts

- `POST /api/shifts`
- `GET /api/shifts`
- `GET /api/shifts/{id}`
- `GET /api/shifts/department/{department}`
- `GET /api/shifts/date/{date}`
- `GET /api/shifts/department/{department}/date/{date}`
- `PUT /api/shifts/{id}/production?actualProduction=450`
- `DELETE /api/shifts/{id}`

### Defects

- `POST /api/defects`
- `GET /api/defects`
- `GET /api/defects/{id}`
- `GET /api/defects/machine/{machineCode}`
- `GET /api/defects/department/{department}`
- `GET /api/defects/severity/{severity}`
- `GET /api/defects/status/{status}`
- `PUT /api/defects/{id}/resolve`
- `DELETE /api/defects/{id}`

### Notifications

- `POST /api/notifications/forgot-password`
- `POST /api/notifications/validate-token`
- `POST /api/notifications/reset-password`
- `POST /api/notifications/send-otp`
- `POST /api/notifications/verify-otp`

## Local Development Without Docker

Run infrastructure with Docker:

```powershell
docker compose up -d forgewatchdb rabbitmq redis
```

Then run services from separate terminals:

```powershell
.\mvnw.cmd -pl auth-service spring-boot:run
.\mvnw.cmd -pl machine-service spring-boot:run
.\mvnw.cmd -pl shift-service spring-boot:run
.\mvnw.cmd -pl defect-service spring-boot:run
.\mvnw.cmd -pl alert-service spring-boot:run
.\mvnw.cmd -pl api-gateway spring-boot:run
```

For local runs, service `application.properties` files default to:

- PostgreSQL: `localhost:5433`
- RabbitMQ: `localhost:5672`
- Redis: `localhost:6379`

## Troubleshooting

### Postman shows `ECONNREFUSED`

Check that `api-gateway` is running:

```powershell
docker compose ps api-gateway
docker compose logs --tail 100 api-gateway
```

The gateway should expose:

```text
0.0.0.0:8080->8080/tcp
```

### Gateway cannot reach a service in Docker

Inside Docker, `localhost` means the current container. The gateway must use Compose service names such as:

```text
http://auth-service:8081
http://machine-service:8082
```

The provided `docker-compose.yml` sets those values through environment variables.

### RabbitMQ connection errors during startup

Short-lived startup errors can happen while RabbitMQ is still opening port `5672`. If logs later show `Created new connection`, the service recovered.

### Email log says sent but no email arrives

`alert-service` logs success after Gmail SMTP accepts the message. If it does not arrive:

- Confirm `SUPERVISOR_EMAIL` in `.env`.
- Check spam/promotions.
- Confirm `MAIL_USERNAME` and Gmail app password are valid.
- Check Gmail account security restrictions.

### SMS sends OTP instead of breakdown text

That means the alert path is using Twilio Verify. Verify only sends OTP messages. Custom alert SMS requires:

```env
TWILIO_FROM_PHONE_NUMBER=+1xxxxxxxxxx
```

or:

```env
TWILIO_MESSAGING_SERVICE_SID=MGxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
```

The Messaging Service must be configured in Twilio with a valid sender.

