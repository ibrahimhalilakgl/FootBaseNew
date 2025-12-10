package com.footbase.api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class PlayerRatingDto {
    private Long id;
    private Integer score;
    private String comment;
    private String author;
    private Instant createdAt;
}

