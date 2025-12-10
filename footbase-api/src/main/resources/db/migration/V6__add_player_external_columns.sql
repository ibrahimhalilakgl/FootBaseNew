ALTER TABLE players
    ADD COLUMN IF NOT EXISTS external_id BIGINT,
    ADD COLUMN IF NOT EXISTS image_url TEXT;

CREATE UNIQUE INDEX IF NOT EXISTS idx_players_external_id ON players(external_id);

