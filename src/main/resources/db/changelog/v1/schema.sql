CREATE TABLE IF NOT EXISTS clients
(
    id                 BIGSERIAL PRIMARY KEY,
    chat_id            BIGINT    NOT NULL UNIQUE,
    telegram_user_id   BIGINT    NOT NULL UNIQUE,
    telegram_username  VARCHAR(50),
    first_name         VARCHAR(50),
    last_name          VARCHAR(50),
    phone              VARCHAR(20),
    birthday           DATE,
    bonus_balance      INTEGER   NOT NULL DEFAULT 0,
    total_spent        INTEGER   NOT NULL DEFAULT 0,
    created_at         TIMESTAMP NOT NULL,
    updated_at         TIMESTAMP,
    is_active          BOOLEAN   NOT NULL DEFAULT TRUE,
    registration_state VARCHAR(50) DEFAULT 'NOT_REGISTERED'
);

CREATE INDEX IF NOT EXISTS idx_chat_id ON clients (telegram_user_id);