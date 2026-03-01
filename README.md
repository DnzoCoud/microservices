# Backend Technical Test – Microservices (Java / Spring Boot)

This repository contains a two-microservice backend implementation using **Hexagonal Architecture (Ports & Adapters)**, **PostgreSQL**, **RabbitMQ**, **Swagger/OpenAPI**, and **unit tests**.

## Services

- **customer-service**  
  Owns **Person** + **Customer** data (inheritance). Exposes CRUD endpoints for customers and publishes customer events to RabbitMQ.

- **account-service**  
  Owns **Account**, **Movements**, and **Customer Snapshot** (local read model). Exposes CRUD endpoints for accounts, movement registration, and account statement reports. Consumes customer events from RabbitMQ to keep the snapshot updated.
---

## Architecture (Hexagonal / Ports & Adapters)

Each microservice follows a layered Hexagonal approach:

- **domain/**
    - Pure business model (no Spring/JPA annotations)
    - Business rules and domain exceptions
    - **Ports**: interfaces that describe what the domain needs (repositories, publishers, etc.)

- **application/**
    - Use cases (business workflows)
    - Commands/DTOs for application layer (not REST DTOs)
    - Coordinates domain + ports (transaction boundaries live here)

- **infrastructure/**
    - Adapters implementing ports:
        - **persistence**: JPA entities + repositories + mappers + adapters
        - **web**: controllers + REST DTOs + mappers + exception handlers
        - **messaging**: RabbitMQ publisher/consumer adapters + config

### Dependency rule
- `domain` depends on nothing
- `application` depends on `domain`
- `infrastructure` depends on `domain` (and may wire `application`)

This ensures the domain stays framework-agnostic and testable.

---
# Quick Start (Docker)

## 1) Prerequisites
- Docker + Docker Compose

## 2) Run everything
From the repository root:

```bash
docker compose up --build
```

This will start:
- PostgreSQL (port 5432)
- RabbitMQ (ports 5672 and 15672)
- customer-service (port 8081)
- account-service (port 8082)
- swagger Open api documentation (< port of microservice >/swagger-ui/index.html)

> Note: The database schemas/tables are created automatically using the SQL file mounted in the Postgres container.
