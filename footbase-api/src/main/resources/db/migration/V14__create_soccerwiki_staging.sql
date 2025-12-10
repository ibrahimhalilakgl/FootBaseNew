-- Staging tabloları (geçici import için)
CREATE TABLE IF NOT EXISTS club_staging (
    external_id BIGINT PRIMARY KEY,
    name        TEXT,
    short_name  TEXT,
    logo_url    TEXT
);

CREATE TABLE IF NOT EXISTS player_staging (
    external_id BIGINT PRIMARY KEY,
    full_name   TEXT,
    image_url   TEXT
);

