package com.footbase.api.database;

import java.util.List;

/**
 * İlk defa kurulum yapan ortamlar için minimum tablo oluşturma SQL'leri.
 * Normalde Flyway migrasyonları çalıştırılır; bu içerik yalnızca referans/otomasyon
 * amaçlıdır ve uygulama akışını değiştirmez.
 */
public final class DatabaseBootstrapSql {
    private DatabaseBootstrapSql() {}

    /**
     * CREATE TABLE IF NOT EXISTS komutlarını tek string olarak döner.
     */
    public static String initialSchemaSql() {
        return String.join("\n\n", initialStatements());
    }

    /**
     * Her tablo için ayrı CREATE ifadeleri.
     */
    public static List<String> initialStatements() {
        return List.of(
                """
                CREATE TABLE IF NOT EXISTS roller (
                    id BIGSERIAL PRIMARY KEY,
                    ad VARCHAR(50) UNIQUE NOT NULL,
                    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS kullanicilar (
                    id BIGSERIAL PRIMARY KEY,
                    eposta VARCHAR(255) UNIQUE NOT NULL,
                    sifre VARCHAR(255) NOT NULL,
                    gorunen_isim VARCHAR(120) NOT NULL,
                    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                    guncelleme_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS kullanici_rolleri (
                    kullanici_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
                    rol_id BIGINT NOT NULL REFERENCES roller (id) ON DELETE CASCADE,
                    PRIMARY KEY (kullanici_id, rol_id)
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS takimlar (
                    id BIGSERIAL PRIMARY KEY,
                    ad VARCHAR(150) NOT NULL,
                    kod VARCHAR(10) UNIQUE NOT NULL,
                    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                    external_id BIGINT,
                    short_name VARCHAR(50),
                    logo_url TEXT,
                    sehir VARCHAR(120),
                    stadyum VARCHAR(150),
                    lig VARCHAR(120)
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS oyuncular (
                    id BIGSERIAL PRIMARY KEY,
                    takim_id BIGINT REFERENCES takimlar (id) ON DELETE SET NULL,
                    tam_ad VARCHAR(150) NOT NULL,
                    pozisyon VARCHAR(50),
                    forma_numarasi INTEGER,
                    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                    external_id BIGINT,
                    image_url TEXT
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS maclar (
                    id BIGSERIAL PRIMARY KEY,
                    ev_sahibi_takim_id BIGINT REFERENCES takimlar (id) ON DELETE SET NULL,
                    deplasman_takim_id BIGINT REFERENCES takimlar (id) ON DELETE SET NULL,
                    baslama_tarihi TIMESTAMPTZ NOT NULL,
                    saha VARCHAR(150),
                    durum VARCHAR(40) NOT NULL DEFAULT 'PLANLI',
                    ev_sahibi_skor INTEGER,
                    deplasman_skor INTEGER,
                    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                    guncelleme_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
                );
                """,
                """
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
                """,
                """
                CREATE TABLE IF NOT EXISTS mac_yorumlari (
                    id BIGSERIAL PRIMARY KEY,
                    mac_id BIGINT NOT NULL REFERENCES maclar (id) ON DELETE CASCADE,
                    kullanici_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
                    mesaj TEXT NOT NULL,
                    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                    guncelleme_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW()
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS yorum_begenileri (
                    yorum_id BIGINT NOT NULL REFERENCES mac_yorumlari (id) ON DELETE CASCADE,
                    kullanici_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
                    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                    PRIMARY KEY (yorum_id, kullanici_id)
                );
                """,
                """
                CREATE TABLE IF NOT EXISTS kullanici_takipleri (
                    takip_eden_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
                    takip_edilen_id BIGINT NOT NULL REFERENCES kullanicilar (id) ON DELETE CASCADE,
                    olusturma_tarihi TIMESTAMPTZ NOT NULL DEFAULT NOW(),
                    PRIMARY KEY (takip_eden_id, takip_edilen_id),
                    CHECK (takip_eden_id != takip_edilen_id)
                );
                """
        );
    }
}
