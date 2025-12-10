package com.footbase.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PredictionRequest {
    @NotNull
    @Min(0)
    @Max(20)
    private Integer homeScore;

    @NotNull
    @Min(0)
    @Max(20)
    private Integer awayScore;
}

