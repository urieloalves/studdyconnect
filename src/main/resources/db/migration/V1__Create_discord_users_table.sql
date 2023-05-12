CREATE TABLE users
(
    id       TEXT NOT NULL UNIQUE,
    username TEXT NOT NULL,
    email    TEXT NOT NULL UNIQUE,
    PRIMARY KEY (id)
);