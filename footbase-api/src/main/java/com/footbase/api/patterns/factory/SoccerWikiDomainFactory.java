package com.footbase.api.patterns.factory;

import com.footbase.api.domain.MatchFixture;
import com.footbase.api.domain.Team;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

/**
 * SoccerWiki benzeri kaynaklardan gelen basit DTO'ları domain modellerine dönüştürür.
 * Eksik verileri bilinen meta haritasıyla tamamlar.
 */
public class SoccerWikiDomainFactory implements DomainFactory {

    private static final Map<String, Meta> WELL_KNOWN_META = Map.of(
            "GS", new Meta("İstanbul", "RAMS Park", "Süper Lig"),
            "FB", new Meta("İstanbul", "Ülker Stadyumu", "Süper Lig"),
            "BJK", new Meta("İstanbul", "Tüpraş Stadyumu", "Süper Lig"),
            "TS", new Meta("Trabzon", "Papara Park", "Süper Lig"),
            "BAS", new Meta("İstanbul", "Başakşehir Fatih Terim Stadyumu", "Süper Lig"),
            "ADS", new Meta("Adana", "Yeni Adana Stadyumu", "Süper Lig"),
            "ANT", new Meta("Antalya", "Corendon Airlines Park", "Süper Lig"),
            "GÖZ", new Meta("İzmir", "Gürsel Aksel Stadyumu", "Süper Lig")
    );

    @Override
    public Team createTeam(ExternalClubDto club) {
        ExternalClubDto enriched = enrichWithMeta(club);
        return Team.builder()
                .externalId(enriched.getExternalId())
                .name(enriched.getName())
                .code(Optional.ofNullable(enriched.getShortName()).orElseGet(() -> "T" + enriched.getExternalId()))
                .shortName(enriched.getShortName())
                .logoUrl(enriched.getLogoUrl())
                .city(enriched.getCity())
                .stadium(enriched.getStadium())
                .league(enriched.getLeague())
                .build();
    }

    @Override
    public MatchFixture createMatch(ExternalClubDto homeClub, ExternalClubDto awayClub, Instant kickoffAt, String venue) {
        return MatchFixture.builder()
                .homeTeam(createTeam(homeClub))
                .awayTeam(createTeam(awayClub))
                .kickoffAt(kickoffAt)
                .venue(venue)
                .status("PLANLI")
                .build();
    }

    private ExternalClubDto enrichWithMeta(ExternalClubDto club) {
        Meta meta = WELL_KNOWN_META.getOrDefault(
                Optional.ofNullable(club.getShortName()).orElse(""),
                null
        );
        if (meta == null) {
            return club;
        }
        return ExternalClubDto.builder()
                .externalId(club.getExternalId())
                .name(club.getName())
                .shortName(club.getShortName())
                .logoUrl(club.getLogoUrl())
                .city(Optional.ofNullable(club.getCity()).orElse(meta.city))
                .stadium(Optional.ofNullable(club.getStadium()).orElse(meta.stadium))
                .league(Optional.ofNullable(club.getLeague()).orElse(meta.league))
                .build();
    }

    private record Meta(String city, String stadium, String league) {}
}
