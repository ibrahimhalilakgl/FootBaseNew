-- Temel Süper Lig takımları için şehir, stadyum, lig bilgisi
UPDATE takimlar SET sehir = 'İstanbul', stadyum = 'RAMS Park', lig = 'Süper Lig'
WHERE kod = 'GS';

UPDATE takimlar SET sehir = 'İstanbul', stadyum = 'Ülker Stadyumu', lig = 'Süper Lig'
WHERE kod = 'FB';

UPDATE takimlar SET sehir = 'İstanbul', stadyum = 'Tüpraş Stadyumu', lig = 'Süper Lig'
WHERE kod = 'BJK';

UPDATE takimlar SET sehir = 'Trabzon', stadyum = 'Papara Park', lig = 'Süper Lig'
WHERE kod = 'TS';

UPDATE takimlar SET sehir = 'İstanbul', stadyum = 'Rams Başakşehir Fatih Terim Stadyumu', lig = 'Süper Lig'
WHERE kod = 'BAS';

UPDATE takimlar SET sehir = 'Adana', stadyum = 'Yeni Adana Stadyumu', lig = 'Süper Lig'
WHERE kod = 'ADS';

UPDATE takimlar SET sehir = 'Antalya', stadyum = 'Corendon Airlines Park', lig = 'Süper Lig'
WHERE kod = 'ANT';

UPDATE takimlar SET sehir = 'İzmir', stadyum = 'Bornova Aziz Kocaoğlu Stadyumu', lig = 'Süper Lig'
WHERE kod = 'GÖZ' OR short_name = 'GÖZ';

-- Eğer lig bilgisi boşsa ve kod/short_name biliniyorsa Süper Lig olarak işaretle
UPDATE takimlar SET lig = 'Süper Lig'
WHERE lig IS NULL AND kod IN ('GS','FB','BJK','TS','BAS','ADS','ANT','GÖZ');
