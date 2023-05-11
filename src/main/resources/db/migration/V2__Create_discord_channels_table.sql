CREATE TABLE discord_channels
(
    id       BIGINT NOT NULL UNIQUE,
    guild_id BIGINT NOT NULL,
    PRIMARY KEY (id)
);