CREATE SCHEMA finance;

CREATE TABLE finance.accounts
(
    id UUID NOT NULL PRIMARY KEY,
    client_id UUID NOT NULL,
    currency VARCHAR(255) NOT NULL,
    balance NUMERIC(19,2) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    entity_version BIGINT NOT NULL
);

CREATE INDEX idx_client_id ON finance.accounts(client_id);

CREATE TABLE finance.transactions
(
    id UUID NOT NULL PRIMARY KEY,
    account_id UUID NOT NULL,
    target_account_id UUID NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    currency VARCHAR(255) NOT NULL,
    created_date TIMESTAMP NOT NULL,
    updated_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    entity_version BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL,
    FOREIGN KEY (account_id) REFERENCES finance.accounts(id),
    FOREIGN KEY (target_account_id) REFERENCES finance.accounts(id)
);

CREATE INDEX idx_account_id ON finance.transactions(account_id);
CREATE INDEX idx_target_account_id ON finance.transactions(target_account_id);