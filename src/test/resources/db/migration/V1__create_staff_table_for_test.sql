CREATE TABLE staff (
    id SERIAL PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);
