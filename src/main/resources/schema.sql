CREATE TABLE IF NOT EXISTS transactions (
    id INTEGER NOT NULL AUTO_INCREMENT,
    transaction_date TIMESTAMP NOT NULL,
    transaction_type VARCHAR(10) NOT NULL,
    transaction_code VARCHAR(50) NOT NULL,
    transaction_amount DECIMAL NOT NULL,
    transaction_category VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS budgets (
    id INTEGER NOT NULL AUTO_INCREMENT,
    budget_amt DECIMAL NOT NULL,
    budget_month TINYINT NOT NULL,
    budget_year SMALLINT NOT NULL,
    budget_category VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX category_budget ON budgets (budget_month, budget_year, budget_category);