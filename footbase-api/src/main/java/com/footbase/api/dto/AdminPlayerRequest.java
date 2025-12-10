package com.footbase.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminPlayerRequest {
    private Long teamId;
    
    @NotBlank
    private String fullName;
    
    private String position;
    
    private Integer shirtNumber;
}

