package com.footbase.api.patterns.strategy;

import com.footbase.api.domain.MatchFixture;

import java.util.List;

/**
 * Strategy context: dışarıya tek çağrı noktası sağlar.
 */
public class MatchStrategyContext {
    private MatchSelectionStrategy strategy;

    public MatchStrategyContext(MatchSelectionStrategy strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(MatchSelectionStrategy strategy) {
        this.strategy = strategy;
    }

    public List<MatchFixture> execute(List<MatchFixture> matches) {
        return strategy.select(matches);
    }
}
