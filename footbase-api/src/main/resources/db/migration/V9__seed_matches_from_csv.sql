-- Seed matches into maclar (Turkish table used by entities)
-- Columns: id;aciklama;deplasman_skor;ev_sahibi_skor;saat;tarih;yer;deplasman_id;ev_sahibi_id

-- Ensure base teams exist for FK
INSERT INTO takimlar (id, ad, kod)
VALUES
    (1, 'Fenerbahçe', 'FEN'),
    (2, 'Galatasaray', 'GAL'),
    (3, 'Beşiktaş', 'BES')
ON CONFLICT (id) DO NOTHING;

SELECT setval('takimlar_id_seq', GREATEST(COALESCE((SELECT MAX(id) FROM takimlar), 0), 3));

WITH rows (id, aciklama, deplasman_skor, ev_sahibi_skor, saat, tarih, yer, deplasman_id, ev_sahibi_id) AS (
    VALUES
        (1, 'Süper Lig - Hafta 14', 1, 1, '20:00:00', '01.12.2025', 'Kadıköy', 2, 1),
        (2, 'ZTK Çeyrek Final',    2, 1, '20:00:00', '02.04.2025', 'Kadıköy', 2, 1),
        (3, 'Süper Lig - Hafta 25', 0, 0, '20:00:00', '24.02.2025', 'Ali Sami Yen', 1, 2),
        (4, 'Süper Lig - Hafta 11', 3, 2, '20:00:00', '02.11.2025', 'Vodafone Park', 1, 3),
        (5, 'Süper Lig - Hafta 34', 1, 0, '20:00:00', '04.05.2025', 'Kadıköy', 3, 1),
        (6, 'Süper Lig - Hafta 8',  1, 1, '20:00:00', '04.10.2025', 'Ali Sami Yen', 3, 2),
        (7, 'Süper Lig - Hafta 29', 1, 2, '20:00:00', '29.03.2025', 'Vodafone Park', 2, 3)
)
INSERT INTO maclar (id, ev_sahibi_takim_id, deplasman_takim_id, baslama_tarihi, saha, durum)
SELECT id,
       ev_sahibi_id,
       deplasman_id,
       to_timestamp(tarih || ' ' || saat, 'DD.MM.YYYY HH24:MI:SS'),
       yer,
       'FINISHED'
FROM rows
ON CONFLICT (id) DO NOTHING;

SELECT setval('maclar_id_seq', (SELECT MAX(id) FROM maclar));

