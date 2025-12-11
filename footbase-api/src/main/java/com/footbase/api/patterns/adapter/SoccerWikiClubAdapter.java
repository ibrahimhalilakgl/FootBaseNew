package com.footbase.api.patterns.adapter;

import com.footbase.api.patterns.factory.ExternalClubDto;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * SoccerWiki JSON formatından (ID, Name, ShortName, ImageURL) iç DTO'ya adaptasyon yapar.
 * Bilinen kulüpler için şehir/stadyum/lig bilgisi ekler.
 */
public class SoccerWikiClubAdapter implements ClubAdapter {

    private static final Map<String, Map<String, String>> WELL_KNOWN_META = new HashMap<>();

    static {
        WELL_KNOWN_META.put("GS", meta("İstanbul", "RAMS Park", "Süper Lig"));
        WELL_KNOWN_META.put("FB", meta("İstanbul", "Ülker Stadyumu", "Süper Lig"));
        WELL_KNOWN_META.put("BJK", meta("İstanbul", "Tüpraş Stadyumu", "Süper Lig"));
        WELL_KNOWN_META.put("TS", meta("Trabzon", "Papara Park", "Süper Lig"));
        WELL_KNOWN_META.put("BAS", meta("İstanbul", "Başakşehir Fatih Terim Stadyumu", "Süper Lig"));
        WELL_KNOWN_META.put("ADS", meta("Adana", "Yeni Adana Stadyumu", "Süper Lig"));
        WELL_KNOWN_META.put("ANT", meta("Antalya", "Corendon Airlines Park", "Süper Lig"));
        WELL_KNOWN_META.put("GÖZ", meta("İzmir", "Gürsel Aksel Stadyumu", "Süper Lig"));
    }

    @Override
    public ExternalClubDto adapt(Map<String, Object> raw) {
        String shortName = stringValue(raw.get("ShortName"));
        Map<String, String> meta = WELL_KNOWN_META.getOrDefault(shortName, Map.of());

        return ExternalClubDto.builder()
                .externalId(longValue(raw.get("ID")))
                .name(stringValue(raw.get("Name")))
                .shortName(shortName)
                .logoUrl(stringValue(raw.get("ImageURL")))
                .city(meta.get("city"))
                .stadium(meta.get("stadium"))
                .league(meta.get("league"))
                .build();
    }

    private static Map<String, String> meta(String city, String stadium, String league) {
        Map<String, String> m = new HashMap<>();
        m.put("city", city);
        m.put("stadium", stadium);
        m.put("league", league);
        return m;
    }

    private String stringValue(Object value) {
        return Optional.ofNullable(value).map(Object::toString).orElse(null);
    }

    private Long longValue(Object value) {
        if (value == null) return null;
        try {
            return Long.parseLong(value.toString());
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
