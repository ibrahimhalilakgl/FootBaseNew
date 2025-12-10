INSERT INTO teams (name, code) VALUES
    ('İstanbul FK', 'IST'),
    ('Anadolu SK', 'ANA')
ON CONFLICT (code) DO NOTHING;

INSERT INTO players (team_id, full_name, position, shirt_number)
SELECT t.id, 'Emre Yılmaz', 'FW', 9 FROM teams t WHERE t.code = 'IST' LIMIT 1
ON CONFLICT DO NOTHING;
INSERT INTO players (team_id, full_name, position, shirt_number)
SELECT t.id, 'Mert Kaya', 'GK', 1 FROM teams t WHERE t.code = 'IST' LIMIT 1
ON CONFLICT DO NOTHING;
INSERT INTO players (team_id, full_name, position, shirt_number)
SELECT t.id, 'Kerem Demir', 'MF', 8 FROM teams t WHERE t.code = 'ANA' LIMIT 1
ON CONFLICT DO NOTHING;

INSERT INTO matches (home_team_id, away_team_id, kickoff_at, venue, status)
SELECT ht.id, at.id, NOW() + INTERVAL '2 day', 'Atatürk Stadyumu', 'SCHEDULED'
FROM teams ht, teams at
WHERE ht.code = 'IST' AND at.code = 'ANA'
ON CONFLICT DO NOTHING;

