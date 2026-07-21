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
    registration_state VARCHAR(50)        DEFAULT 'NOT_REGISTERED'
);

CREATE INDEX IF NOT EXISTS idx_chat_id ON clients (telegram_user_id);


CREATE TABLE roles
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS users
(
    id                      BIGSERIAL PRIMARY KEY,
    username                VARCHAR(50) UNIQUE NOT NULL,
    password                VARCHAR(255)       NOT NULL,
    email                   VARCHAR(100)       NOT NULL UNIQUE,
    role_id                 BIGINT             NOT NULL,
    enabled                 BOOLEAN            NOT NULL DEFAULT TRUE,
    account_non_expired     BOOLEAN            NOT NULL DEFAULT TRUE,
    account_non_locked      BOOLEAN            NOT NULL DEFAULT TRUE,
    credentials_non_expired BOOLEAN            NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP          NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT
);

INSERT INTO roles (name, description)
VALUES ('ROLE_USER', 'Кассир'),
       ('ROLE_ADMIN', 'Администратор');

INSERT INTO users (username, password, email, role_id)
VALUES ('admin',
        '$2a$12$QpTzxRtGq2kGh6w/btex2eKnTg8Yx4T9k0qNY/I9CppvRN6V3jAcm',
        'admin@mail.ru',
        (SELECT id FROM roles WHERE name = 'ROLE_ADMIN'));
