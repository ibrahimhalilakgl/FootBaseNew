package com.footbase.api.patterns.factory;

import com.footbase.api.domain.MatchFixture;
import com.footbase.api.domain.Team;

import java.time.Instant;

/**
 * Abstract Factory: farklı veri kaynaklarına göre domain nesneleri üretir.
 */
public interface DomainFactory {
    Team createTeam(ExternalClubDto club);

    MatchFixture createMatch(ExternalClubDto homeClub, ExternalClubDto awayClub, Instant kickoffAt, String venue);
}
