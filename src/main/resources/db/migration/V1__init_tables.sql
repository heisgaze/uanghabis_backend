-- =========================
-- EXTENSION
-- =========================
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- =========================
-- ENUM TYPES
-- =========================
CREATE TYPE category_type AS ENUM ('income', 'expense');
CREATE TYPE transaction_type AS ENUM ('income', 'expense');
CREATE TYPE summary_status AS ENUM ('aman', 'waspada', 'bahaya');
CREATE TYPE decision_recommendation AS ENUM ('aman', 'tunda', 'berisiko');

-- =========================
-- USERS
-- =========================
CREATE TABLE users
(
    id         UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- =========================
-- FINANCIAL SETUPS
-- =========================
CREATE TABLE financial_setups
(
    id             UUID PRIMARY KEY        DEFAULT uuid_generate_v4(),
    user_id        UUID           NOT NULL,
    monthly_income DECIMAL(15, 2) NOT NULL,
    fixed_expense  DECIMAL(15, 2) NOT NULL,
    period_month   INT            NOT NULL,
    period_year    INT            NOT NULL,
    created_at     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_financial_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_financial_period
        UNIQUE (user_id, period_month, period_year)
);

-- =========================
-- CATEGORIES
-- =========================
CREATE TABLE categories
(
    id         UUID PRIMARY KEY       DEFAULT uuid_generate_v4(),
    name       VARCHAR(100)  NOT NULL,
    type       category_type NOT NULL,
    is_default BOOLEAN       NOT NULL DEFAULT FALSE
);

-- =========================
-- TRANSACTIONS
-- =========================
CREATE TABLE transactions
(
    id               UUID PRIMARY KEY          DEFAULT uuid_generate_v4(),
    user_id          UUID             NOT NULL,
    category_id      UUID             NOT NULL,
    amount           DECIMAL(15, 2)   NOT NULL,
    note             VARCHAR(255),
    transaction_type transaction_type NOT NULL,
    transaction_date DATE             NOT NULL,
    created_at       TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_transaction_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_transaction_category
        FOREIGN KEY (category_id) REFERENCES categories (id)
);

-- =========================
-- MONTHLY SUMMARIES
-- =========================
CREATE TABLE monthly_summaries
(
    id                UUID PRIMARY KEY        DEFAULT uuid_generate_v4(),
    user_id           UUID           NOT NULL,
    month             INT            NOT NULL,
    year              INT            NOT NULL,
    total_income      DECIMAL(15, 2) NOT NULL DEFAULT 0,
    total_expense     DECIMAL(15, 2) NOT NULL DEFAULT 0,
    remaining_balance DECIMAL(15, 2) NOT NULL DEFAULT 0,
    status            summary_status NOT NULL,
    created_at        TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_summary_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT uq_summary_period
        UNIQUE (user_id, month, year)
);

-- =========================
-- DECISIONS
-- =========================
CREATE TABLE decisions
(
    id             UUID PRIMARY KEY                 DEFAULT uuid_generate_v4(),
    user_id        UUID                    NOT NULL,
    planned_amount DECIMAL(15, 2)          NOT NULL,
    recommendation decision_recommendation NOT NULL,
    created_at     TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_decision_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE
);

-- =========================
-- NOTIFICATIONS
-- =========================
CREATE TABLE notifications
(
    id         UUID PRIMARY KEY      DEFAULT uuid_generate_v4(),
    user_id    UUID         NOT NULL,
    title      VARCHAR(150) NOT NULL,
    message    TEXT         NOT NULL,
    is_read    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id) REFERENCES users (id)
            ON DELETE CASCADE
);

-- =========================
-- INDEXES (PERFORMANCE)
-- =========================
CREATE INDEX idx_transactions_user_date
    ON transactions (user_id, transaction_date);

CREATE INDEX idx_notifications_user_read
    ON notifications (user_id, is_read);
