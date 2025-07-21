package com.zapp.candidate_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CandidateDto {
    @NotBlank
    private String fullName;
    private String email;
    private String phone;
    private String remarks;
}
