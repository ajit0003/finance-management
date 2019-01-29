CREATE TABLE IF NOT EXISTS transactions (
    id INTEGER NOT NULL AUTO_INCREMENT,
    transaction_date TIMESTAMP NOT NULL,
    transaction_type VARCHAR(10) NOT NULL,
    transaction_code VARCHAR(50) NOT NULL,
    transaction_amount DECIMAL NOT NULL,
    PRIMARY KEY(id)
);