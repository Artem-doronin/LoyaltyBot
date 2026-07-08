CREATE TABLE IF NOT EXISTS clients
(
    id                BIGSERIAL PRIMARY KEY,
    chat_id           BIGINT      NOT NULL UNIQUE,
    telegram_username VARCHAR(50),
    first_name        VARCHAR(50),
    last_name         VARCHAR(50),
    phone             VARCHAR(20),
    birthday          DATE,
    bonus_balance     INTEGER     NOT NULL DEFAULT 0,
    total_spent       INTEGER     NOT NULL DEFAULT 0,
    registered_at     TIMESTAMP   NOT NULL,
    last_active_at    TIMESTAMP,
    active            BOOLEAN     NOT NULL DEFAULT TRUE
);