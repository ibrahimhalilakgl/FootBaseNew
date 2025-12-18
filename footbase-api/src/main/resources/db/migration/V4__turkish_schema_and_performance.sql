
CREATE TABLE IF NOT EXISTS roller (
    id BIGSERIAL PRIMARY KEY,
    ad VARCHAR(50) UNIQUE NOT NULL,
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_roller_ad ON roller(ad);


CREATE TABLE IF NOT EXISTS kullanicilar (
    id BIGSERIAL PRIMARY KEY,
    eposta VARCHAR(255) UNIQUE NOT NULL,
    sifre VARCHAR(255) NOT NULL,
    gorunen_isim VARCHAR(120) NOT NULL,
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    guncelleme_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_kullanicilar_eposta ON kullanicilar(eposta);
CREATE INDEX idx_kullanicilar_gorunen_isim ON kullanicilar(gorunen_isim);


CREATE TABLE IF NOT EXISTS kullanici_rolleri (
    kullanici_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
    rol_id BIGINT NOT NULL REFERENCES roller (id) ON DELETE CASCADE,
    PRIMARY KEY (kullanici_id, rol_id)
);

CREATE INDEX idx_kullanici_rolleri_kullanici ON kullanici_rolleri(kullanici_id);
CREATE INDEX idx_kullanici_rolleri_rol ON kullanici_rolleri(rol_id);


CREATE TABLE IF NOT EXISTS takimlar (
    id BIGSERIAL PRIMARY KEY,
    ad VARCHAR(150) NOT NULL,
    kod VARCHAR(10) UNIQUE NOT NULL,
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_takimlar_ad ON takimlar(ad);
CREATE INDEX idx_takimlar_kod ON takimlar(kod);

CREATE TABLE IF NOT EXISTS oyuncular (
    id BIGSERIAL PRIMARY KEY,
    takim_id BIGINT REFERENCES takimlar (id) ON DELETE SET NULL,
    tam_ad VARCHAR(150) NOT NULL,
    pozisyon VARCHAR(50),
    forma_numarasi INTEGER,
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_oyuncular_takim ON oyuncular(takim_id);
CREATE INDEX idx_oyuncular_tam_ad ON oyuncular(tam_ad);
CREATE INDEX idx_oyuncular_pozisyon ON oyuncular(pozisyon);

CREATE TABLE IF NOT EXISTS maclar (
    id BIGSERIAL PRIMARY KEY,
    ev_sahibi_takim_id BIGINT REFERENCES takimlar (id) ON DELETE SET NULL,
    deplasman_takim_id BIGINT REFERENCES takimlar (id) ON DELETE SET NULL,
    baslama_tarihi TIMESTAMPTZ NOT NULL,
    saha VARCHAR(150),
    durum VARCHAR(40) NOT NULL DEFAULT 'PLANLI',
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    guncelleme_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_maclar_ev_sahibi ON maclar(ev_sahibi_takim_id);
CREATE INDEX idx_maclar_deplasman ON maclar(deplasman_takim_id);
CREATE INDEX idx_maclar_baslama_tarihi ON maclar(baslama_tarihi);
CREATE INDEX idx_maclar_durum ON maclar(durum);
CREATE INDEX idx_maclar_tarih_durum ON maclar(baslama_tarihi, durum);

CREATE TABLE IF NOT EXISTS mac_tahminleri (
    id BIGSERIAL PRIMARY KEY,
    mac_id BIGINT NOT NULL REFERENCES maclar (id) ON DELETE CASCADE,
    kullanici_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
    ev_sahibi_skor INTEGER NOT NULL CHECK (ev_sahibi_skor >= 0),
    deplasman_skor INTEGER NOT NULL CHECK (deplasman_skor >= 0),
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    guncelleme_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(mac_id, kullanici_id)
);

CREATE INDEX idx_mac_tahminleri_mac ON mac_tahminleri(mac_id);
CREATE INDEX idx_mac_tahminleri_kullanici ON mac_tahminleri(kullanici_id);
CREATE INDEX idx_mac_tahminleri_mac_kullanici ON mac_tahminleri(mac_id, kullanici_id);

CREATE TABLE IF NOT EXISTS oyuncu_puanlamalari (
    id BIGSERIAL PRIMARY KEY,
    oyuncu_id BIGINT NOT NULL REFERENCES oyuncular (id) ON DELETE CASCADE,
    kullanici_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
    puan INTEGER NOT NULL CHECK (puan BETWEEN 1 AND 10),
    yorum TEXT,
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    guncelleme_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(oyuncu_id, kullanici_id)
);

CREATE INDEX idx_oyuncu_puanlamalari_oyuncu ON oyuncu_puanlamalari(oyuncu_id);
CREATE INDEX idx_oyuncu_puanlamalari_kullanici ON oyuncu_puanlamalari(kullanici_id);
CREATE INDEX idx_oyuncu_puanlamalari_puan ON oyuncu_puanlamalari(puan);

CREATE TABLE IF NOT EXISTS mac_yorumlari (
    id BIGSERIAL PRIMARY KEY,
    mac_id BIGINT NOT NULL REFERENCES maclar (id) ON DELETE CASCADE,
    kullanici_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
    mesaj TEXT NOT NULL,
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    guncelleme_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_mac_yorumlari_mac ON mac_yorumlari(mac_id);
CREATE INDEX idx_mac_yorumlari_kullanici ON mac_yorumlari(kullanici_id);
CREATE INDEX idx_mac_yorumlari_tarih ON mac_yorumlari(olusturma_tarihi DESC);
CREATE INDEX idx_mac_yorumlari_mac_tarih ON mac_yorumlari(mac_id, olusturma_tarihi DESC);

CREATE TABLE IF NOT EXISTS kullanici_takipleri (
    takip_eden_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
    takip_edilen_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (takip_eden_id, takip_edilen_id),
    CHECK (takip_eden_id != takip_edilen_id)
);

CREATE INDEX idx_kullanici_takipleri_takip_eden ON kullanici_takipleri(takip_eden_id);
CREATE INDEX idx_kullanici_takipleri_takip_edilen ON kullanici_takipleri(takip_edilen_id);

CREATE TABLE IF NOT EXISTS yorum_begenileri (
    yorum_id BIGINT NOT NULL REFERENCES mac_yorumlari (id) ON DELETE CASCADE,
    kullanici_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (yorum_id, kullanici_id)
);

CREATE INDEX idx_yorum_begenileri_yorum ON yorum_begenileri(yorum_id);
CREATE INDEX idx_yorum_begenileri_kullanici ON yorum_begenileri(kullanici_id);

INSERT INTO roller (id, ad, olusturma_tarihi)
SELECT id, name, NOW() FROM roles
ON CONFLICT DO NOTHING;

INSERT INTO kullanicilar (id, eposta, sifre, gorunen_isim, olusturma_tarihi, guncelleme_tarihi)
SELECT id, email, password, display_name, created_at, updated_at FROM users
ON CONFLICT DO NOTHING;

INSERT INTO kullanici_rolleri (kullanici_id, rol_id)
SELECT user_id, role_id FROM user_roles
ON CONFLICT DO NOTHING;

INSERT INTO takimlar (id, ad, kod, olusturma_tarihi)
SELECT id, name, code, NOW() FROM teams
ON CONFLICT DO NOTHING;

INSERT INTO oyuncular (id, takim_id, tam_ad, pozisyon, forma_numarasi, olusturma_tarihi)
SELECT id, team_id, full_name, position, shirt_number, created_at FROM players
ON CONFLICT DO NOTHING;

INSERT INTO maclar (id, ev_sahibi_takim_id, deplasman_takim_id, baslama_tarihi, saha, durum, olusturma_tarihi, guncelleme_tarihi)
SELECT id, home_team_id, away_team_id, kickoff_at, venue, status, NOW(), NOW() FROM matches
ON CONFLICT DO NOTHING;

INSERT INTO mac_tahminleri (id, mac_id, kullanici_id, ev_sahibi_skor, deplasman_skor, olusturma_tarihi, guncelleme_tarihi)
SELECT id, match_id, user_id, home_score, away_score, created_at, NOW() FROM match_predictions
ON CONFLICT DO NOTHING;

INSERT INTO oyuncu_puanlamalari (id, oyuncu_id, kullanici_id, puan, yorum, olusturma_tarihi, guncelleme_tarihi)
SELECT id, player_id, user_id, score, comment, created_at, NOW() FROM player_ratings
ON CONFLICT DO NOTHING;

INSERT INTO mac_yorumlari (id, mac_id, kullanici_id, mesaj, olusturma_tarihi, guncelleme_tarihi)
SELECT id, match_id, user_id, message, created_at, NOW() FROM match_comments
ON CONFLICT DO NOTHING;

INSERT INTO kullanici_takipleri (takip_eden_id, takip_edilen_id, olusturma_tarihi)
SELECT follower_id, following_id, created_at FROM user_follows
ON CONFLICT DO NOTHING;

INSERT INTO yorum_begenileri (yorum_id, kullanici_id, olusturma_tarihi)
SELECT comment_id, user_id, created_at FROM comment_likes
ON CONFLICT DO NOTHING;

SELECT setval('roller_id_seq', (SELECT MAX(id) FROM roller));
SELECT setval('kullanicilar_id_seq', (SELECT MAX(id) FROM kullanicilar));
SELECT setval('takimlar_id_seq', (SELECT MAX(id) FROM takimlar));
SELECT setval('oyuncular_id_seq', (SELECT MAX(id) FROM oyuncular));
SELECT setval('maclar_id_seq', (SELECT MAX(id) FROM maclar));
SELECT setval('mac_tahminleri_id_seq', (SELECT MAX(id) FROM mac_tahminleri));
SELECT setval('oyuncu_puanlamalari_id_seq', (SELECT MAX(id) FROM oyuncu_puanlamalari));
SELECT setval('mac_yorumlari_id_seq', (SELECT MAX(id) FROM mac_yorumlari));


ANALYZE roller;
ANALYZE kullanicilar;
ANALYZE kullanici_rolleri;
ANALYZE takimlar;
ANALYZE oyuncular;
ANALYZE maclar;
ANALYZE mac_tahminleri;
ANALYZE oyuncu_puanlamalari;
ANALYZE mac_yorumlari;
ANALYZE kullanici_takipleri;
ANALYZE yorum_begenileri;

INSERT INTO roller (ad) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_MODERATOR'),
    ('ROLE_USER')
ON CONFLICT (ad) DO NOTHING;

ALTER TABLE IF EXISTS comment_likes DROP CONSTRAINT IF EXISTS comment_likes_comment_id_fkey;
ALTER TABLE IF EXISTS comment_likes DROP CONSTRAINT IF EXISTS comment_likes_user_id_fkey;
ALTER TABLE IF EXISTS user_follows DROP CONSTRAINT IF EXISTS user_follows_follower_id_fkey;
ALTER TABLE IF EXISTS user_follows DROP CONSTRAINT IF EXISTS user_follows_following_id_fkey;
ALTER TABLE IF EXISTS match_comments DROP CONSTRAINT IF EXISTS match_comments_match_id_fkey;
ALTER TABLE IF EXISTS match_comments DROP CONSTRAINT IF EXISTS match_comments_user_id_fkey;
ALTER TABLE IF EXISTS player_ratings DROP CONSTRAINT IF EXISTS player_ratings_player_id_fkey;
ALTER TABLE IF EXISTS player_ratings DROP CONSTRAINT IF EXISTS player_ratings_user_id_fkey;
ALTER TABLE IF EXISTS match_predictions DROP CONSTRAINT IF EXISTS match_predictions_match_id_fkey;
ALTER TABLE IF EXISTS match_predictions DROP CONSTRAINT IF EXISTS match_predictions_user_id_fkey;
ALTER TABLE IF EXISTS matches DROP CONSTRAINT IF EXISTS matches_home_team_id_fkey;
ALTER TABLE IF EXISTS matches DROP CONSTRAINT IF EXISTS matches_away_team_id_fkey;
ALTER TABLE IF EXISTS players DROP CONSTRAINT IF EXISTS players_team_id_fkey;
ALTER TABLE IF EXISTS user_roles DROP CONSTRAINT IF EXISTS user_roles_user_id_fkey;
ALTER TABLE IF EXISTS user_roles DROP CONSTRAINT IF EXISTS user_roles_role_id_fkey;

DROP TABLE IF EXISTS comment_likes CASCADE;
DROP TABLE IF EXISTS user_follows CASCADE;
DROP TABLE IF EXISTS match_comments CASCADE;
DROP TABLE IF EXISTS player_ratings CASCADE;
DROP TABLE IF EXISTS match_predictions CASCADE;
DROP TABLE IF EXISTS matches CASCADE;
DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

