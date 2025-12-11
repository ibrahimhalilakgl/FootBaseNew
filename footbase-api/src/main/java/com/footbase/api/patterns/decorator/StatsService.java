package com.footbase.api.patterns.decorator;

import com.footbase.api.domain.MatchFixture;

import java.util.List;

public interface StatsService {
    StatsSummary computeTeamSummary(List<MatchFixture> matches, Long teamId);
}
