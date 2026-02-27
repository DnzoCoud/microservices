-- BaseDatos.sql
-- This script is executed automatically by the official Postgres Docker image
-- Schemas:
--   - customer: customer-service owns these tables
--   - account:  account-service owns these tables

-- =========================
-- 1) Schemas
-- =========================
CREATE SCHEMA IF NOT EXISTS customer;
CREATE SCHEMA IF NOT EXISTS account;

-- =========================
-- 2) CUSTOMER SERVICE TABLES
-- =========================
-- Single Table Inheritance:
-- "Persona" + "Cliente" stored in a single table.

CREATE TABLE IF NOT EXISTS customer.customers (
    id              BIGSERIAL PRIMARY KEY,
    customer_id     VARCHAR(50) NOT NULL UNIQUE,
    password        VARCHAR(200) NOT NULL,
    status          BOOLEAN NOT NULL DEFAULT TRUE,

-- Persona fields
    name            VARCHAR(120) NOT NULL,
    gender          VARCHAR(20),
    age             INTEGER,
    identification  VARCHAR(50),
    address         VARCHAR(200),
    phone           VARCHAR(40),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_customers_identification
    ON customer.customers(identification);

-- =========================
-- 3) ACCOUNT SERVICE TABLES
-- =========================
-- Customer snapshot replicated asynchronously from customer-service events (RabbitMQ).
-- This avoids synchronous calls between microservices.

CREATE TABLE IF NOT EXISTS account.customer_snapshot (
    id           BIGSERIAL PRIMARY KEY,
    customer_id  VARCHAR(50) NOT NULL UNIQUE,
    name         VARCHAR(120) NOT NULL,
    status       BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- Accounts table (Cuenta)
CREATE TABLE IF NOT EXISTS account.accounts (
    id              BIGSERIAL PRIMARY KEY,
    account_number  VARCHAR(30) NOT NULL UNIQUE,
    account_type    VARCHAR(30) NOT NULL,
    initial_balance NUMERIC(18,2) NOT NULL DEFAULT 0,
    balance         NUMERIC(18,2) NOT NULL DEFAULT 0,
    status          BOOLEAN NOT NULL DEFAULT TRUE,
    customer_id     VARCHAR(50) NOT NULL,

    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
    );

CREATE INDEX IF NOT EXISTS idx_accounts_customer_id
    ON account.accounts(customer_id);

-- Movements table (Movimientos)
CREATE TABLE IF NOT EXISTS account.movements (
    id             BIGSERIAL PRIMARY KEY,
    account_id     BIGINT NOT NULL REFERENCES account.accounts(id),
    movement_date  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    movement_type  VARCHAR(10) NOT NULL,
    amount         NUMERIC(18,2) NOT NULL,
    balance_after  NUMERIC(18,2) NOT NULL,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT chk_movement_type CHECK (movement_type IN ('CREDIT', 'DEBIT')),
    CONSTRAINT chk_amount_positive CHECK (amount > 0)
    );

CREATE INDEX IF NOT EXISTS idx_movements_account_id_date
    ON account.movements(account_id, movement_date);