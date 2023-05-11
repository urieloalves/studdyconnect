CREATE TABLE groups
(
    id                 TEXT   NOT NULL UNIQUE,
    name               TEXT   NOT NULL,
    description        TEXT   NOT NULL,
    course_link        TEXT   NOT NULL,
    created_by_id      TEXT   NOT NULL REFERENCES discord_users (id),
    discord_channel_id BIGINT NOT NULL REFERENCES discord_channels (id),
    PRIMARY KEY (id)
);

CREATE TABLE group_users
(
    user_id  TEXT REFERENCES groups (id),
    group_id TEXT REFERENCES groups (id)
)