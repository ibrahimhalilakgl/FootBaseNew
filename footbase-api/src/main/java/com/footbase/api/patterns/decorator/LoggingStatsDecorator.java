package com.footbase.api.patterns.decorator;

import com.footbase.api.domain.MatchFixture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Decorator: İstatistik hesaplamasına log ve süre ölçümü ekler.
 */
public class LoggingStatsDecorator implements StatsService {
    private static final Logger log = LoggerFactory.getLogger(LoggingStatsDecorator.class);
    private final StatsService delegate;

    public LoggingStatsDecorator(StatsService delegate) {
        this.delegate = delegate;
    }

    @Override
    public StatsSummary computeTeamSummary(List<MatchFixture> matches, Long teamId) {
        long start = System.currentTimeMillis();
        try {
            return delegate.computeTeamSummary(matches, teamId);
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("Stats computed for team {} in {} ms", teamId, duration);
        }
    }
}
