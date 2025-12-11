package com.footbase.api.patterns.strategy;

import com.footbase.api.domain.MatchFixture;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Belirli bir takımın ev sahibi olduğu maçları öne alır, sonrasında tarihi göre sıralar.
 */
public class HomeTeamFirstStrategy implements MatchSelectionStrategy {
    private final Long teamId;

    public HomeTeamFirstStrategy(Long teamId) {
        this.teamId = teamId;
    }

    @Override
    public List<MatchFixture> select(List<MatchFixture> matches) {
        return matches.stream()
                .sorted(Comparator
                        .comparing((MatchFixture m) -> isHomeTeam(m) ? 0 : 1)
                        .thenComparing(MatchFixture::getKickoffAt, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    private boolean isHomeTeam(MatchFixture match) {
        return match.getHomeTeam() != null && Objects.equals(match.getHomeTeam().getId(), teamId);
    }
}
