-- liquibase formatted sql

-- changeset author:1
CREATE INDEX IF NOT EXISTS idx_clients_phone ON clients(phone);
CREATE INDEX IF NOT EXISTS idx_clients_telegram_username ON clients(telegram_username);

-- changeset author:2
CREATE INDEX IF NOT EXISTS idx_transactions_client_created
    ON transactions(client_id, created_at DESC);