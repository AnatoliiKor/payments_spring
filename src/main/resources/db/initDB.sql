DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS usr;
DROP TABLE IF EXISTS credit_card;
DROP SEQUENCE IF EXISTS usr_id_seq;
DROP SEQUENCE IF EXISTS account_id_seq;
DROP SEQUENCE IF EXISTS payment_id_seq;

CREATE SEQUENCE usr_id_seq START WITH 1;
CREATE SEQUENCE transaction_id_seq START WITH 30;
CREATE SEQUENCE payment_id_seq START WITH 1;
CREATE SEQUENCE card_id_seq START WITH 1000000000000000;

create table usr
(
    id           bigint    default nextval('usr_id_seq'::regclass) not null
        constraint usr_pkey
            primary key,
    last_name    varchar(255)                                      not null,
    name         varchar(255)                                      not null,
    middle_name  varchar(255),
    password     varchar(255)                                      not null,
    email        varchar(255)                                      not null
        constraint usr_email_key
            unique,
    phone_number bigint
        constraint usr_phone_number_key
            unique,
    registered   timestamp default now()                           not null,
    active       boolean   default true                            not null
);

create table user_role
(
    user_id bigint       not null,
    role    varchar(255) not null,
    FOREIGN KEY (user_id) REFERENCES usr (id) ON DELETE CASCADE
);

create table account
(
    id           bigint    default nextval('account_id_seq'::regclass) not null
        constraint account_pkey
            primary key,
    number       bigint                                                not null
        constraint account_number_key
            unique,
    balance      bigint                                                not null
        constraint account_balance_check
            check (balance >= 0),
    account_name varchar(255)                                          not null,
    currency     varchar(255)                                          not null,
    registered   timestamp default now()                               not null,
    active       boolean   default false                               not null,
    user_id      bigint
        constraint account_user_id_fkey
            references usr
            on delete restrict,
    action       integer
);

create table credit_card
(
    card_id    bigint default nextval('card_id_seq'::regclass) not null
        constraint credit_card_pkey
            primary key,
    account_id bigint                                          not null
        constraint credit_card_account_id_fkey
            references account
            on delete cascade
);

create table payment
(
    id             BIGINT       NOT NULL PRIMARY KEY DEFAULT nextval('payment_id_seq'),
    account_number BIGINT       not null,
    account_name   varchar(255) not null,
    receiver       BIGINT       not null,
    registered     timestamp                         default now() not null,
    destination    varchar(255),
    amount         INT          not null,
    currency       varchar(255) not null,
    FOREIGN KEY (account_number) REFERENCES account (number) ON DELETE CASCADE
);

create table transaction
(
    id           BIGINT       NOT NULL PRIMARY KEY DEFAULT nextval('transaction_id_seq'),
    payer        BIGINT       not null,
    receiver     BIGINT       not null,
    registered   timestamp                         default now() not null,
    destination  varchar(255),
    amount       INT          not null,
    currency     varchar(255) not null,
    FOREIGN KEY (payer) REFERENCES account (number),
    FOREIGN KEY (receiver) REFERENCES account (number)
);