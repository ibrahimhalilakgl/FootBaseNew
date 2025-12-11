-- Yeni kolonlar: image_url ve external_id'yi oyuncular tablosuna ekle
ALTER TABLE oyuncular
    ADD COLUMN IF NOT EXISTS external_id BIGINT,
    ADD COLUMN IF NOT EXISTS image_url TEXT;

CREATE UNIQUE INDEX IF NOT EXISTS idx_oyuncular_external_id ON oyuncular(external_id);

-- players tablosundaki SoccerWiki verisini oyuncular tablosuna taşı (eksikse ekle, varsa güncelle)
INSERT INTO oyuncular (takim_id, tam_ad, pozisyon, forma_numarasi, external_id, image_url, olusturma_tarihi)
SELECT NULL, p.full_name, p.position, p.shirt_number, p.external_id, p.image_url, COALESCE(p.created_at, NOW())
FROM players p
WHERE p.external_id IS NOT NULL
  AND NOT EXISTS (
    SELECT 1 FROM oyuncular o WHERE o.external_id = p.external_id
  );

-- Var olan kayıtları güncelle (isim / pozisyon / forma / görsel)
UPDATE oyuncular o
SET tam_ad = COALESCE(p.full_name, o.tam_ad),
    pozisyon = COALESCE(p.position, o.pozisyon),
    forma_numarasi = COALESCE(p.shirt_number, o.forma_numarasi),
    image_url = COALESCE(p.image_url, o.image_url)
FROM players p
WHERE o.external_id = p.external_id;
