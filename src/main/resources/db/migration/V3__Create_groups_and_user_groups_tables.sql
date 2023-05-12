CREATE TABLE study_group
(
    id                 TEXT   NOT NULL UNIQUE,
    name               TEXT   NOT NULL,
    description        TEXT   NOT NULL,
    course_link        TEXT   NOT NULL,
    created_by_id      TEXT   NOT NULL REFERENCES discord_user (id),
    discord_channel_id BIGINT NOT NULL REFERENCES discord_channel (id),
    PRIMARY KEY (id)
);

CREATE TABLE group_users
(
    user_id  TEXT REFERENCES study_group (id),
    group_id TEXT REFERENCES study_group (id)
)