CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    email varchar(50) UNIQUE NOT NULL,
    password varchar(50) NOT NULL,
    first_name varchar(30) NOT NULL,
    last_name varchar(30) NOT NULL,
    birth_date date NOT NULL,
    gender boolean NOT NULL,
    city varchar(50)
);

CREATE TABLE role (
    id SERIAL PRIMARY KEY,
    name varchar(50) UNIQUE NOT NULL
);

CREATE TABLE account_role (
    account_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
);