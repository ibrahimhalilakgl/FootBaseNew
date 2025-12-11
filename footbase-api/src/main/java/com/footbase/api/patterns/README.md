# Tasarım Desenleri (demo)

Bu klasör, uygulama koduna dokunmadan referans amaçlı beş yaygın deseni içerir. Her biri Spring bağlamına bağımlı değildir; örnekler kolayca entegre edilebilir.

- `strategy`: Farklı maç seçme/sıralama kuralları (ör. en yakın maçlar, belirli takım ev sahibi).
- `factory`: Harici (SoccerWiki) kulüp verilerini domain nesnesine dönüştüren abstract factory.
- `adapter`: SoccerWiki kulüp formatını iç DTO’ya çeviren adaptör, bilinen meta (şehir/stadyum/lig) ekler.
- `decorator`: İstatistik servisine logging gibi enjekte edilebilir davranış ekleyen süsleyici.
- `observer`: Basit event-bus ve takım güncelleme olayı ile publish/subscribe örneği.
