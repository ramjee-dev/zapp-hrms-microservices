package com.zapp.candidate_service.dto;

import lombok.Data;

@Data
public class CandidateFilter {
    private Long jobId;
    private String status;
}
