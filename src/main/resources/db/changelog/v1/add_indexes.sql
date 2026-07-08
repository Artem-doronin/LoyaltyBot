-- liquibase formatted sql

-- changeset author:1
CREATE INDEX IF NOT EXISTS idx_clients_bonus_balance ON clients(bonus_balance);
CREATE INDEX IF NOT EXISTS idx_clients_registered_at ON clients(registered_at);

-- changeset author:2
CREATE INDEX IF NOT EXISTS idx_transactions_client_created
    ON transactions(client_id, created_at DESC);