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


INSERT INTO  users (username, password, email, role_id, should_change_password)
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

CREATE TABLE IF NOT EXISTS client_bonus_balances
(
    id         BIGSERIAL PRIMARY KEY,
    client_id  BIGINT  NOT NULL,
    amount     NUMERIC NOT NULL DEFAULT 0,
    bonus_rate NUMERIC NOT NULL DEFAULT '10',

    FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_client_id ON client_bonus_balances (client_id);

