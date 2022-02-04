CREATE TABLE  if not exists TRANSACTIONS
(
    id                         UUID PRIMARY KEY,
    transaction_correlation_id UUID,
    account_id                 UUID           NOT NULL,
    order_id                   BIGINT         NOT NULL,
    is_deposit                 BOOLEAN        NOT NULL,
    currency                   VARCHAR(3)     NOT NULL,
    transaction_amount         NUMERIC(10, 2) NOT NULL,
    amount_after               NUMERIC(10, 2) NOT NULL,
    timestamp                  TIMESTAMP NOT NULL,
    UNIQUE (account_id, order_id),
    CHECK ( order_id > 0 )
);
