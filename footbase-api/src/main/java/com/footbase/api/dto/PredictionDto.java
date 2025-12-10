package com.footbase.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PredictionDto {
    private Long id;
    private Integer homeScore;
    private Integer awayScore;
}

