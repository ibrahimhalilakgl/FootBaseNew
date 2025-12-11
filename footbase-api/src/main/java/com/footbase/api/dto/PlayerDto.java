package com.footbase.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerDto {
    private Long id;
    private String fullName;
    private String position;
    private Integer shirtNumber;
    private String team;
    private Long teamId;
    private String imageUrl;
    private Long externalId;
    private Double averageRating;
    private Long ratingCount;
}
