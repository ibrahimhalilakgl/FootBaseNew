-- Staging verisini ana tablolara aktar
-- Kulüpler
INSERT INTO teams (external_id, name, short_name, logo_url, created_at)
SELECT external_id, name, short_name, logo_url, NOW()
FROM club_staging
ON CONFLICT (external_id) DO UPDATE
SET name = EXCLUDED.name,
    short_name = EXCLUDED.short_name,
    logo_url = EXCLUDED.logo_url;

-- Oyuncular
INSERT INTO players (external_id, full_name, image_url, created_at)
SELECT external_id, full_name, image_url, NOW()
FROM player_staging
ON CONFLICT (external_id) DO UPDATE
SET full_name = EXCLUDED.full_name,
    image_url = EXCLUDED.image_url;

