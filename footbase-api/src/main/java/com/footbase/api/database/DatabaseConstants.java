package com.footbase.api.database;

/**
 * Uygulamanın veri tabanı katmanı için temel sabitler.
 *
 * Bu sınıf çalışma akışını değiştirmez; sadece
 * bağlanma ve migrasyon yollarını tek yerde görmek için eklendi.
 */
public final class DatabaseConstants {
    private DatabaseConstants() {}

    /**
     * Varsayılan JDBC URL (application.properties ile eşleşir).
     */
    public static final String DEFAULT_JDBC_URL = "jdbc:postgresql://localhost:5432/footbasenew";

    /**
     * Varsayılan kullanıcı (application.properties).
     */
    public static final String DEFAULT_USER = "postgres";

    /**
     * Flyway migration dizini (classpath).
     */
    public static final String MIGRATION_PATH = "db/migration";

    /**
     * Migration dosya prefixi (Flyway varsayılanı).
     */
    public static final String MIGRATION_PREFIX = "V";
}
