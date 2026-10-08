CREATE TABLE IF NOT EXISTS clients
(
    id                 BIGSERIAL PRIMARY KEY,
    chat_id            BIGINT    NOT NULL UNIQUE,
    telegram_user_id   BIGINT    NOT NULL UNIQUE,
    telegram_username  VARCHAR(50),
    first_name         VARCHAR(50),
    last_name          VARCHAR(50),
    phone              VARCHAR(20),
    gender             VARCHAR(6),
    birthday           DATE,
    created_at         TIMESTAMP NOT NULL,
    updated_at         TIMESTAMP,
    is_active          BOOLEAN   NOT NULL DEFAULT TRUE,
    registration_state VARCHAR(50)        DEFAULT 'NOT_REGISTERED'
);

CREATE INDEX IF NOT EXISTS idx_chat_id ON clients (telegram_user_id);


CREATE TABLE IF NOT EXISTS roles
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users
(
    id                     BIGSERIAL PRIMARY KEY,
    username               VARCHAR(50) UNIQUE NOT NULL,
    password               VARCHAR(255)       NOT NULL,
    email                  VARCHAR(100)       NOT NULL UNIQUE,
    role_id                BIGINT             NOT NULL,
    enabled                BOOLEAN            NOT NULL DEFAULT TRUE,
    should_change_password BOOLEAN            NOT NULL DEFAULT TRUE,
    created_at             TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT
);

INSERT INTO roles (name, description)
VALUES ('ROLE_USER', 'Кассир'),
       ('ROLE_ADMIN', 'Администратор');


INSERT INTO users (username, password, email, role_id, should_change_password)
VALUES ('admin',
        '$2a$12$QpTzxRtGq2kGh6w/btex2eKnTg8Yx4T9k0qNY/I9CppvRN6V3jAcm',
        'admin@mail.ru',
        (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'),
        false);


CREATE TABLE IF NOT EXISTS client_bonus_transactions
(
    id               BIGSERIAL PRIMARY KEY,
    client_id        BIGINT      NOT NULL,
    user_id          BIGINT      NOT NULL,
    operation_amount NUMERIC     NOT NULL,
    bonus_amount     NUMERIC     NOT NULL,
    operation_type   VARCHAR(30) NOT NULL,
    description      VARCHAR(128),
    created_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE RESTRICT
);

CREATE INDEX idx_bonus_tx_client_type_created
    ON client_bonus_transactions (client_id, operation_type, created_at);

CREATE TABLE IF NOT EXISTS client_bonus_balances
(
    id         BIGSERIAL PRIMARY KEY,
    client_id  BIGINT  NOT NULL UNIQUE,
    amount     NUMERIC NOT NULL DEFAULT 0,
    bonus_rate NUMERIC NOT NULL DEFAULT '10',

    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_client_id ON client_bonus_balances (client_id);

CREATE TABLE IF NOT EXISTS outbox_message
(
    id                    BIGSERIAL PRIMARY KEY,
    client_id             BIGINT      NOT NULL,
    message_id            UUID        NOT NULL UNIQUE,
    payload               TEXT        NOT NULL,
    status                VARCHAR(20) NOT NULL,
    attempts              INT         NOT NULL DEFAULT 0,
    locked_by             VARCHAR,
    locked_until          TIMESTAMPTZ,
    next_attempt_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now(),
    error_message         TEXT,
    campaign_execution_id BIGINT,
    attachments           JSONB,

    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE RESTRICT,
    FOREIGN KEY (campaign_execution_id) REFERENCES campaign_execution (id) ON DELETE RESTRICT

);

CREATE UNIQUE INDEX uq_outbox_campaign_client
    ON outbox_message (campaign_execution_id, client_id)
    WHERE campaign_execution_id IS NOT NULL;

CREATE INDEX idx_outbox_status_next_attempt
    ON outbox_message (status, next_attempt_at)
    WHERE status IN ('NEW', 'FAILED');

CREATE INDEX idx_outbox_status_locked_until
    ON outbox_message (status, locked_until)
    WHERE status = 'PROCESSING';

CREATE TABLE campaigns
(
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    status          VARCHAR(20)  NOT NULL,
    schedule_type   VARCHAR(20)  NOT NULL,
    send_at         TIMESTAMPTZ,
    cron_expression VARCHAR(50),
    audience_type   VARCHAR(30)  NOT NULL,
    audience_params JSONB,
    created_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ  NOT NULL DEFAULT now(),

    CONSTRAINT chk_campaign_schedule CHECK (
        (schedule_type = 'ONE_TIME' AND send_at IS NOT NULL AND cron_expression IS NULL)
            OR (schedule_type = 'RECURRING' AND cron_expression IS NOT NULL)
        )
);

CREATE TABLE campaign_messages
(
    id          BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT UNIQUE NOT NULL,
    text        TEXT          NOT NULL CHECK ( length(trim(text)) > 0 ),
    channel     VARCHAR(20)   NOT NULL,

    FOREIGN KEY (campaign_id) REFERENCES campaigns (id) ON DELETE CASCADE
);

CREATE TABLE campaign_attachments
(
    id                  BIGSERIAL PRIMARY KEY,
    campaign_message_id BIGINT      NOT NULL,
    file_url            TEXT,
    file_id             TEXT,
    type                VARCHAR(20) NOT NULL,
    sort_order          INT         NOT NULL,

    FOREIGN KEY (campaign_message_id) REFERENCES campaign_messages (id) ON DELETE CASCADE

);

CREATE INDEX idx_campaign_attachments_message_sort
    ON campaign_attachments (campaign_message_id, sort_order);

CREATE TABLE campaign_execution
(
    id               BIGSERIAL PRIMARY KEY,
    campaign_id      BIGINT      NOT NULL,
    scheduled_at     TIMESTAMPTZ NOT NULL,
    started_at       TIMESTAMPTZ NOT NULL,
    finished_at      TIMESTAMPTZ,
    status           VARCHAR(20) NOT NULL,
    recipients_count INT,
    enqueued_count   INT,
    failed_count     INT,
    error_message    TEXT,

    FOREIGN KEY (campaign_id) REFERENCES campaigns (id) ON DELETE RESTRICT,
    CONSTRAINT uq_campaign_execution_schedule UNIQUE (campaign_id, scheduled_at)
);



