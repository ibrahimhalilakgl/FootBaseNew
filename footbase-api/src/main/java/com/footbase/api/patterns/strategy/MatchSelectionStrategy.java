package com.footbase.api.patterns.strategy;

import com.footbase.api.domain.MatchFixture;

import java.util.List;

/**
 * Strategy arayüzü: farklı maç seçme/sıralama kurallarını soyutlar.
 */
public interface MatchSelectionStrategy {
    List<MatchFixture> select(List<MatchFixture> matches);
}
