package com.footbase.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdminTeamRequest {
    @NotBlank
    private String name;
    
    @NotBlank
    private String code;
}

