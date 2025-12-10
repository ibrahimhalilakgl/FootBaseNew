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
public class FeedItemDto {
    private Long id;
    private String type; // "COMMENT" or "RATING"
    private String author;
    private String content;
    private Instant createdAt;
    private Long matchId;
    private String matchInfo;
    private Long playerId;
    private String playerName;
    private Integer rating;
    private Long likesCount;
    private Boolean isLiked;
}

