package com.zapp.job_service.dto;

import com.zapp.job_service.entity.Job;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class JobResponseDTO {
    private Long id;
    private String title;
    private String description;
    private Job.JobStatus status;
    private LocalDateTime createdAt;
    private Long clientId;
}


