-- Seed teams from Takimlar.csv (simplified to id, name, code)
INSERT INTO teams (id, name, code) VALUES
  (1, 'Fenerbahçe', 'FEN'),
  (2, 'Galatasaray', 'GAL'),
  (3, 'Beşiktaş', 'BES')
ON CONFLICT (id) DO NOTHING;

SELECT setval('teams_id_seq', (SELECT MAX(id) FROM teams));

