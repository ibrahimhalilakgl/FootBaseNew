package com.footbase.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;

@Data
public class AdminMatchRequest {
    @NotNull
    private Long homeTeamId;
    
    @NotNull
    private Long awayTeamId;
    
    @NotNull
    private Instant kickoffAt;
    
    private String venue;
    
    private String status;

    private Integer homeScore;
    private Integer awayScore;
}
