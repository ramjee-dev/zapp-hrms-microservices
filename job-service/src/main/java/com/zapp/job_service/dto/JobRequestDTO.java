package com.zapp.job_service.dto;

import com.zapp.job_service.entity.Job;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JobRequestDTO {
    private String title;
    private String description;
    private Job.JobStatus status;
    private Long clientId;
}


