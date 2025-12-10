package com.footbase.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CommentDto {
    private Long id;
    private String author;
    private Long authorId;
    private String message;
    private Instant createdAt;
    private Long likesCount;
    private Boolean isLiked;
    private Boolean canDelete;
    private Boolean canEdit;
}
