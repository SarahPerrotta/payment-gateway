# Payment Gateway Microservices Platform

A production-grade payment processing system built with Java and Spring Boot, consisting of three independently deployable microservices. Built to mirror the architecture used in enterprise fintech environments like JPMorgan Chase's payments infrastructure.

---

## Architecture

```
┌─────────────────┐     JWT Token      ┌──────────────────────┐
│   Auth Service  │ ─────────────────► │  Transaction Service  │
│   Port: 8080    │                    │  Port: 8082           │
│                 │                    │                        │
│ • Register      │                    │ • Create transaction   │
│ • Login         │                    │ • Live FX conversion   │
│ • JWT tokens    │                    │ • Calls fraud service  │
│ • BCrypt hashing│                    └──────────┬───────────┘
└─────────────────┘                               │
                                                  │ REST call
                                                  ▼
                                    ┌─────────────────────────┐
                                    │     Fraud Service        │
                                    │     Port: 8083           │
                                    │                          │
                                    │ • Rule-based detection   │
                                    │ • Amount threshold check │
                                    │ • Account validation     │
                                    └─────────────────────────┘
```

---

## Services

### Auth Service (Port 8080)
Handles user registration, authentication, and JWT token generation.

- **POST** `/register` — Register a new user (BCrypt password hashing)
- **POST** `/login` — Authenticate and receive a signed JWT token
- **GET** `/protected` — Example protected endpoint (requires JWT)

### Transaction Service (Port 8082)
Processes payments with live currency conversion and automatic fraud checking.

- **POST** `/transactions` — Create a new transaction (requires JWT)
- **GET** `/transactions/{id}` — Retrieve a transaction by ID (requires JWT)
- **GET** `/transactions` — List all transactions (requires JWT)

### Fraud Service (Port 8083)
Rule-based fraud detection engine that evaluates every transaction before approval.

- **POST** `/fraud/check` — Evaluate a transaction for fraud risk

**Fraud rules:**
- Transactions over £1,000 → `FLAGGED`
- Same sender and receiver account → `FLAGGED`
- Zero or negative amount → `FLAGGED`
- All other transactions → `APPROVED`

---

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 3.x | Microservices framework |
| Spring Security | Authentication & authorisation |
| JWT (jjwt) | Stateless token-based auth |
| BCrypt | Password hashing |
| Spring Data JPA | Database ORM |
| H2 | In-memory database (development) |
| RestTemplate | Service-to-service HTTP communication |
| Exchange Rates API | Live FX currency conversion |
| Maven | Build tool |

---

## Getting Started

### Prerequisites
- Java 17+
- Maven 3.9+

### Running the services

Open three terminal windows and run each service:

**Terminal 1 — Auth Service:**
```bash
cd auth-service
mvn spring-boot:run
```

**Terminal 2 — Transaction Service:**
```bash
cd transaction-service
mvn spring-boot:run
```

**Terminal 3 — Fraud Service:**
```bash
cd fraud-service
mvn spring-boot:run
```

---

## API Usage

### 1. Register a user
```bash
POST http://localhost:8080/register
Content-Type: application/json

{
    "username": "sarah",
    "password": "password123"
}
```

### 2. Login and get JWT token
```bash
POST http://localhost:8080/login
Content-Type: application/json

{
    "username": "sarah",
    "password": "password123"
}
```
Response: `eyJhbGciOiJIUzI1NiJ9...` (JWT token)

### 3. Create a transaction (with JWT token)
```bash
POST http://localhost:8082/transactions
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
Content-Type: application/json

{
    "fromAccount": "ACC001",
    "toAccount": "ACC002",
    "amount": 500.00,
    "currency": "USD"
}
```
Response:
```json
{
    "id": 1,
    "fromAccount": "ACC001",
    "toAccount": "ACC002",
    "amount": 394.52,
    "currency": "GBP",
    "status": "APPROVED",
    "timestamp": "2026-05-25T13:12:49"
}
```

---

## Key Features

**Stateless JWT Authentication** — Auth service issues signed JWT tokens. Every other service validates tokens independently using a shared secret key — no session storage required.

**Live Currency Conversion** — All non-GBP transactions are automatically converted to GBP using real-time exchange rates from the Exchange Rates API before processing.

**Automatic Fraud Detection** — Every transaction triggers a fraud check before being saved. The rule engine evaluates amount thresholds, account patterns, and flags suspicious activity.

**Microservices Architecture** — Three independently deployable services each with a single responsibility, communicating via REST — mirroring enterprise payment infrastructure patterns.

---

## JPMC Relevance

This project was built to mirror core concepts used in JPMorgan Chase's payments technology stack:

- Microservices architecture matching JPMC's distributed systems approach
- JWT-based stateless authentication matching enterprise API security patterns  
- Rule-based fraud detection reflecting real-world pre-payment risk checks
- Multi-currency transaction processing reflecting cross-border payments infrastructure
- Spring Boot and Java — JPMC's primary backend technology stack

---

## Author

**Sarah Perrotta** — Incoming Software Engineer (SEP), JPMorgan Chase Tech Connect Programme, Glasgow (July 2026)

GitHub: [github.com/SarahPerrotta](https://github.com/SarahPerrotta)
