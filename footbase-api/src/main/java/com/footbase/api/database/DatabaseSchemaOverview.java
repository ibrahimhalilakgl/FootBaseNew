package com.footbase.api.database;

import java.util.List;

/**
 * Şema ve ana tablolar hakkında hızlı referans.
 * Kod akışını etkilemez; okunabilirliği artırmak için eklendi.
 */
public final class DatabaseSchemaOverview {
    private DatabaseSchemaOverview() {}

    public record Table(String name, String description) {}

    /**
     * Çekirdek tablolar ve kısa açıklamalar.
     */
    public static List<Table> coreTables() {
        return List.of(
                new Table("kullanicilar / users", "Kullanıcı hesapları"),
                new Table("roller / roles", "Yetki/rol kayıtları"),
                new Table("takimlar / teams", "Takım kimlikleri + logo/lig/şehir/stadyum"),
                new Table("oyuncular / players", "Oyuncu kimlikleri ve takım ilişkisi"),
                new Table("maclar / matches", "Fikstür, skor ve durum"),
                new Table("mac_yorumlari / match_comments", "Maç yorumları"),
                new Table("mac_tahminleri / match_predictions", "Kullanıcı maç tahminleri"),
                new Table("kullanici_takipleri / user_follows", "Takip ilişkileri"),
                new Table("yorum_begenileri / comment_likes", "Yorum beğenileri")
        );
    }
}
