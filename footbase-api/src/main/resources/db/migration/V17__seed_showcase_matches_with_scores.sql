-- V17: Örnek maçlar (skor ve logo destekli içerik için)

-- Galatasaray - Fenerbahçe (yakında)
WITH gs AS (SELECT id FROM takimlar WHERE kod = 'GS' LIMIT 1),
     fb AS (SELECT id FROM takimlar WHERE kod = 'FB' LIMIT 1)
INSERT INTO maclar (ev_sahibi_takim_id, deplasman_takim_id, baslama_tarihi, saha, durum, ev_sahibi_skor, deplasman_skor, olusturma_tarihi, guncelleme_tarihi)
SELECT gs.id, fb.id, NOW() + INTERVAL '2 days', 'Rams Park', 'PLANLI', 0, 0, NOW(), NOW()
FROM gs, fb
WHERE gs.id IS NOT NULL AND fb.id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM maclar m
    WHERE m.ev_sahibi_takim_id = gs.id AND m.deplasman_takim_id = fb.id
          AND m.baslama_tarihi::date = (NOW() + INTERVAL '2 days')::date
  );

-- Trabzonspor - Beşiktaş (yakında)
WITH ts AS (SELECT id FROM takimlar WHERE kod = 'TS' LIMIT 1),
     bjk AS (SELECT id FROM takimlar WHERE kod = 'BJK' LIMIT 1)
INSERT INTO maclar (ev_sahibi_takim_id, deplasman_takim_id, baslama_tarihi, saha, durum, ev_sahibi_skor, deplasman_skor, olusturma_tarihi, guncelleme_tarihi)
SELECT ts.id, bjk.id, NOW() + INTERVAL '3 days', 'Papara Park', 'PLANLI', 0, 0, NOW(), NOW()
FROM ts, bjk
WHERE ts.id IS NOT NULL AND bjk.id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM maclar m
    WHERE m.ev_sahibi_takim_id = ts.id AND m.deplasman_takim_id = bjk.id
          AND m.baslama_tarihi::date = (NOW() + INTERVAL '3 days')::date
  );

-- Beşiktaş - Fenerbahçe (yakında)
WITH bjk2 AS (SELECT id FROM takimlar WHERE kod = 'BJK' LIMIT 1),
     fb2 AS (SELECT id FROM takimlar WHERE kod = 'FB' LIMIT 1)
INSERT INTO maclar (ev_sahibi_takim_id, deplasman_takim_id, baslama_tarihi, saha, durum, ev_sahibi_skor, deplasman_skor, olusturma_tarihi, guncelleme_tarihi)
SELECT bjk2.id, fb2.id, NOW() + INTERVAL '5 days', 'Tüpraş Stadyumu', 'PLANLI', 0, 0, NOW(), NOW()
FROM bjk2, fb2
WHERE bjk2.id IS NOT NULL AND fb2.id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM maclar m
    WHERE m.ev_sahibi_takim_id = bjk2.id AND m.deplasman_takim_id = fb2.id
          AND m.baslama_tarihi::date = (NOW() + INTERVAL '5 days')::date
  );
