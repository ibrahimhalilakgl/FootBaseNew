package com.footbase.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class MatchDto {
    private Long id;
    private String homeTeam;
    private String awayTeam;
    private String homeTeamLogo;
    private String awayTeamLogo;
    private Integer homeScore;
    private Integer awayScore;
    private Instant kickoffAt;
    private String venue;
    private String status;
    private List<CommentDto> comments;
    private PredictionDto userPrediction;
}
