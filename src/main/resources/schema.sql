CREATE TABLE IF NOT EXISTS categories (
    id INTEGER NOT NULL AUTO_INCREMENT,
    category VARCHAR(50) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS transactions (
    id INTEGER NOT NULL AUTO_INCREMENT,
    transaction_date TIMESTAMP NOT NULL,
    transaction_type VARCHAR(10) NOT NULL,
    transaction_code VARCHAR(50) NOT NULL,
    transaction_amount DECIMAL NOT NULL,
    category_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE IF NOT EXISTS budgets (
    id INTEGER NOT NULL AUTO_INCREMENT,
    budget_amt DECIMAL NOT NULL,
    budget_month TINYINT NOT NULL,
    budget_year SMALLINT NOT NULL,
    category_id INTEGER NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE UNIQUE INDEX category_budget ON budgets (budget_month, budget_year, category_id);

CREATE TABLE IF NOT EXISTS expenditures (
    id INTEGER NOT NULL AUTO_INCREMENT,
    category_id INTEGER NOT NULL,
    amount DECIMAL NOT NULL,
    expenditure_month TINYINT NOT NULL,
    expenditure_year SMALLINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE UNIQUE INDEX expenditure_date ON expenditures (expenditure_month, expenditure_year, category_id);