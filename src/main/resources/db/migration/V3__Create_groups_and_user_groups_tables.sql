CREATE TABLE groups
(
    id                 TEXT   NOT NULL UNIQUE,
    name               TEXT   NOT NULL,
    description        TEXT   NOT NULL,
    course_link        TEXT   NOT NULL,
    created_by_id      TEXT   NOT NULL REFERENCES users (id),
    discord_channel_id BIGINT NOT NULL REFERENCES channels (id),
    PRIMARY KEY (id)
);

CREATE TABLE groups_users
(
    user_id  TEXT REFERENCES users (id),
    group_id TEXT REFERENCES groups (id)
)