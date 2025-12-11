package com.footbase.api.patterns.decorator;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class StatsSummary {
    int wins;
    int draws;
    int losses;
}
