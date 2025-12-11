package com.footbase.api.patterns.strategy;

import com.footbase.api.domain.MatchFixture;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * En yakın başlayan maçları (varsayılan 3) döndürür.
 */
public class SoonestMatchesStrategy implements MatchSelectionStrategy {
    private final int limit;

    public SoonestMatchesStrategy() {
        this(3);
    }

    public SoonestMatchesStrategy(int limit) {
        this.limit = limit;
    }

    @Override
    public List<MatchFixture> select(List<MatchFixture> matches) {
        return matches.stream()
                .filter(m -> Objects.nonNull(m.getKickoffAt()))
                .sorted(Comparator.comparing(MatchFixture::getKickoffAt))
                .limit(limit)
                .collect(Collectors.toList());
    }
}
