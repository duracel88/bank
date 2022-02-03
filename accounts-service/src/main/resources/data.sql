CREATE TABLE ACCOUNT_DETAILS
(
    id                UUID PRIMARY KEY,
    customer_owner_id UUID        NOT NULL,
    account_currency          VARCHAR(3)  NOT NULL,
    owner_first_name        VARCHAR(32) NOT NULL,
    owner_last_name         VARCHAR(32) NOT NULL
);
