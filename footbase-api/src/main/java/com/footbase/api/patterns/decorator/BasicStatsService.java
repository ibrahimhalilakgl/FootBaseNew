package com.footbase.api.patterns.decorator;

import com.footbase.api.domain.MatchFixture;

import java.util.List;
import java.util.Objects;

/**
 * Temel istatistik hesaplayıcı: skor alanlarını kullanarak kazanma/beraberlik/kaybetme çıkarır.
 */
public class BasicStatsService implements StatsService {
    @Override
    public StatsSummary computeTeamSummary(List<MatchFixture> matches, Long teamId) {
        int wins = 0;
        int draws = 0;
        int losses = 0;

        for (MatchFixture m : matches) {
            if (m.getHomeScore() == null || m.getAwayScore() == null) {
                continue; // skor yoksa atla
            }
            boolean isHome = m.getHomeTeam() != null && Objects.equals(m.getHomeTeam().getId(), teamId);
            boolean isAway = m.getAwayTeam() != null && Objects.equals(m.getAwayTeam().getId(), teamId);
            if (!isHome && !isAway) continue;

            int myScore = isHome ? m.getHomeScore() : m.getAwayScore();
            int oppScore = isHome ? m.getAwayScore() : m.getHomeScore();

            if (myScore > oppScore) wins++;
            else if (myScore == oppScore) draws++;
            else losses++;
        }

        return StatsSummary.builder()
                .wins(wins)
                .draws(draws)
                .losses(losses)
                .build();
    }
}
