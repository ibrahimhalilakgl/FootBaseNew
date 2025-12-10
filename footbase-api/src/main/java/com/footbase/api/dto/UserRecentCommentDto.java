package com.footbase.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecentCommentDto {
    private Long commentId;
    private Long matchId;
    private String matchTitle;
    private String message;
    private Instant createdAt;
}
