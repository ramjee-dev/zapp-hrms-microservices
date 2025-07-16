package com.zapp.job_service.dto;

import com.zapp.job_service.entity.Job;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
public class JobDto {

    @NotBlank(message = "Title cannot be null or blank")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    private Job.JobStatus status;

    @NotNull(message = "Client ID is required")
    @Positive(message = "Client ID must be a positive number")
    private Long clientId;

}
