ALTER TABLE teams
    ADD COLUMN IF NOT EXISTS external_id BIGINT,
    ADD COLUMN IF NOT EXISTS short_name VARCHAR(50),
    ADD COLUMN IF NOT EXISTS logo_url TEXT;

CREATE UNIQUE INDEX IF NOT EXISTS idx_teams_external_id ON teams(external_id);

