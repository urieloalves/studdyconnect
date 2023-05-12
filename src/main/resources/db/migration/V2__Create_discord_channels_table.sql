CREATE TABLE discord_channel
(
    id       BIGINT NOT NULL UNIQUE,
    guild_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);