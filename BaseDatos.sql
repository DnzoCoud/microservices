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

-- Persona table
CREATE TABLE IF NOT EXISTS customer.persons (
    person_id       BIGSERIAL PRIMARY KEY,
    name            VARCHAR(120) NOT NULL,
    gender          VARCHAR(20),
    age             INTEGER,
    identification  VARCHAR(50),
    address         VARCHAR(200),
    phone           VARCHAR(40)
);

CREATE INDEX IF NOT EXISTS idx_persons_identification
    ON customer.persons(identification);

CREATE TABLE IF NOT EXISTS customer.customers (
    person_id     BIGINT PRIMARY KEY,
    customer_id   VARCHAR(50) NOT NULL UNIQUE,
    password      VARCHAR(200) NOT NULL,
    status        BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_customers_person
        FOREIGN KEY (person_id)
        REFERENCES customer.persons(person_id)
        ON DELETE CASCADE
);

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

-- Update timestamp helper
CREATE OR REPLACE FUNCTION account.set_updated_at()
RETURNS TRIGGER AS $$
    BEGIN
      NEW.updated_at = NOW();
    RETURN NEW;
    END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_accounts_updated_at ON account.accounts;
CREATE TRIGGER trg_accounts_updated_at
BEFORE UPDATE ON account.accounts
FOR EACH ROW
EXECUTE FUNCTION account.set_updated_at();

-- =========================
-- 4) DB-Owned Business Logic (Stored Procedure)
-- =========================
-- Atomic movement registration:
-- - Locks the account row to avoid concurrent race conditions
-- - Validates balance for DEBIT
-- - Inserts movement
-- - Updates account balance
-- Required error message: "Saldo no disponible"

CREATE OR REPLACE FUNCTION account.create_movement(
  p_account_id BIGINT,
  p_movement_type VARCHAR,
  p_amount NUMERIC,
  p_movement_date TIMESTAMPTZ DEFAULT NOW()
)
RETURNS TABLE (
  movement_id BIGINT,
  new_balance NUMERIC
)
LANGUAGE plpgsql
AS $$
DECLARE
v_balance NUMERIC(18,2);
  v_status  BOOLEAN;
  v_new_balance NUMERIC(18,2);
BEGIN
  IF p_amount IS NULL OR p_amount <= 0 THEN
    RAISE EXCEPTION 'Invalid amount';
END IF;

  IF p_movement_type NOT IN ('CREDIT', 'DEBIT') THEN
    RAISE EXCEPTION 'Invalid movement type';
END IF;

  -- Lock the account row to prevent race conditions
    SELECT a.balance, a.status
    INTO v_balance, v_status
    FROM account.accounts a
    WHERE a.id = p_account_id
        FOR UPDATE;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'Account not found';
    END IF;

      IF v_status IS FALSE THEN
        RAISE EXCEPTION 'Account inactive';
    END IF;

      IF p_movement_type = 'DEBIT' THEN
        IF v_balance < p_amount THEN
          RAISE EXCEPTION 'Insufficient balance';
    END IF;
        v_new_balance := v_balance - p_amount;
    ELSE
        v_new_balance := v_balance + p_amount;
    END IF;

    INSERT INTO account.movements(account_id, movement_date, movement_type, amount, balance_after)
    VALUES (p_account_id, COALESCE(p_movement_date, NOW()), p_movement_type, p_amount, v_new_balance)
        RETURNING id INTO movement_id;

    UPDATE account.accounts
    SET balance = v_new_balance
    WHERE id = p_account_id;

    new_balance := v_new_balance;
      RETURN NEXT;
    END;
$$;