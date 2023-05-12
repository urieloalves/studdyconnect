CREATE TABLE users
(
    id       TEXT NOT NULL UNIQUE,
    username TEXT NOT NULL,
    email    TEXT NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE groups
(
    id          TEXT   NOT NULL UNIQUE,
    name        TEXT   NOT NULL,
    description TEXT   NOT NULL,
    course_link TEXT   NOT NULL,
    created_by  TEXT   NOT NULL REFERENCES users (id),
    channel_id  BIGINT NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE groups_users
(
    user_id  TEXT REFERENCES users (id),
    group_id TEXT REFERENCES groups (id)
);