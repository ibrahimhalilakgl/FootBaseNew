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
    private List<SimplePlayerDto> players;
    private List<SimpleMatchDto> matches;
    private List<SimpleCommentDto> comments;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimplePlayerDto {
        private Long id;
        private String fullName;
        private String position;
        private String team;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SimpleMatchDto {
        private Long id;
        private String homeTeam;
        private String awayTeam;
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

