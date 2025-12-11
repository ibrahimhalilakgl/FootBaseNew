# FootBaseNew Çalıştırma Rehberi

## Önkoşullar
- Java 21 (JAVA_HOME ayarlı olmalı, örn. `C:\Program Files\Java\jdk-21`)
- Node.js (18+ önerilir) ve npm
- PostgreSQL (örnek varsayılanlar: veritabanı `footbasenew`, kullanıcı `postgres`, şifre `12345`)

> Varsayılan DB bağlantısı: `jdbc:postgresql://localhost:5432/footbasenew`  
> Değiştirmek için `footbase-api/src/main/resources/application.properties` içinde `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` ortam değişkenlerini geçebilirsiniz.

## Veritabanı
1) PostgreSQL’de veritabanını oluşturun:
```sql
CREATE DATABASE footbasenew;
```
2) Tüm tablolar ve seed veriler Flyway migrasyonlarıyla otomatik kuruluyor. Backend’i ilk çalıştırdığınızda `src/main/resources/db/migration` altındaki V01…V22 skriptleri uygulanır (oyuncular, takımlar, maçlar vb. tabloları açar ve verileri doldurur).

## Backend (Spring Boot)
```bash
cd footbase-api
set JAVA_HOME="C:\Program Files\Java\jdk-21"   # Windows örneği
./mvnw.cmd spring-boot:run
```
- Uygulama 8080 portunda açılır (`http://localhost:8080`).
- Sağlık kontrolü: `http://localhost:8080/actuator/health`

## Frontend (React)
```bash
cd frontend
npm install
npm start
```
- Geliştirme sunucusu 3000 portunda açılır (`http://localhost:3000/app`).
- Auth token ve tema bilgisi localStorage’da saklanır; giriş yaptıktan sonra alt sayfalarda oturum korunur.

## Notlar
- Migrasyonlar çalışmazsa, veritabanı erişim bilgilerini ortam değişkeniyle geçin:
  - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
- Port 8080 doluysa başka PID’i kapatın veya `server.port` için ortam değişkeni `PORT` verin.
- Tüm yeni tablolar (`oyuncular`, `takimlar`, `maclar`, kullanıcı/roller/follow/yorum vb.) Flyway ile otomatik oluşturulur; ayrıca staging tabloları da veriyi taşımak için kullanılır.
