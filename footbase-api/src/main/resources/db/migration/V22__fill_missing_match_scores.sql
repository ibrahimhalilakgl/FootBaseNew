-- Eksik skorları tamamla ve durum alanını boş bırakma
UPDATE maclar
SET ev_sahibi_skor = 2,
    deplasman_skor = 1,
    durum = COALESCE(durum, 'TAMAMLANDI')
WHERE id = 1
  AND ev_sahibi_skor IS NULL
  AND deplasman_skor IS NULL;

UPDATE maclar
SET ev_sahibi_skor = 1,
    deplasman_skor = 1,
    durum = COALESCE(durum, 'TAMAMLANDI')
WHERE id = 2
  AND ev_sahibi_skor IS NULL
  AND deplasman_skor IS NULL;

UPDATE maclar
SET ev_sahibi_skor = 3,
    deplasman_skor = 2,
    durum = COALESCE(durum, 'TAMAMLANDI')
WHERE id = 3
  AND ev_sahibi_skor IS NULL
  AND deplasman_skor IS NULL;

UPDATE maclar
SET ev_sahibi_skor = 0,
    deplasman_skor = 0,
    durum = COALESCE(durum, 'TAMAMLANDI')
WHERE id = 4
  AND ev_sahibi_skor IS NULL
  AND deplasman_skor IS NULL;

UPDATE maclar
SET ev_sahibi_skor = 4,
    deplasman_skor = 2,
    durum = COALESCE(durum, 'TAMAMLANDI')
WHERE id = 5
  AND ev_sahibi_skor IS NULL
  AND deplasman_skor IS NULL;

UPDATE maclar
SET ev_sahibi_skor = 2,
    deplasman_skor = 3,
    durum = COALESCE(durum, 'TAMAMLANDI')
WHERE id = 6
  AND ev_sahibi_skor IS NULL
  AND deplasman_skor IS NULL;

UPDATE maclar
SET ev_sahibi_skor = 1,
    deplasman_skor = 0,
    durum = COALESCE(durum, 'TAMAMLANDI')
WHERE id = 7
  AND ev_sahibi_skor IS NULL
  AND deplasman_skor IS NULL;

-- Durum alanı boş kalan maçlar için varsayılan planlı değeri
UPDATE maclar
SET durum = 'PLANLI'
WHERE durum IS NULL;
