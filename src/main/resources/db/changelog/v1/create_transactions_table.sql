CREATE TABLE IF NOT EXISTS transactions
(
    id              BIGSERIAL PRIMARY KEY,
    client_id       BIGINT      NOT NULL,
    amount          INTEGER     NOT NULL,
    type            VARCHAR(20) NOT NULL,
    purchase_amount DECIMAL(10, 2),
    description     TEXT,
    receipt_id      VARCHAR(50),
    source          VARCHAR(20) NOT NULL,
    created_at      TIMESTAMP   NOT NULL,
    CONSTRAINT fk_transactions_client FOREIGN KEY (client_id)
        REFERENCES clients (id) ON DELETE CASCADE
);