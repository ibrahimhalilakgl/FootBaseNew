package com.footbase.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomePageDto {
    private List<SimpleMatchDto> upcomingMatches;
    private List<SimpleCommentDto> comments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleMatchDto {
        private Long id;
        private String homeTeam;
        private String awayTeam;
        private String homeTeamLogo;
        private String awayTeamLogo;
        private Integer homeScore;
        private Integer awayScore;
        private String status;
        private Instant kickoffAt;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleCommentDto {
        private Long id;
        private String author;
        private String message;
        private Long matchId;
        private Instant createdAt;
    }
}
