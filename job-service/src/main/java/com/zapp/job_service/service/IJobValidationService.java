package com.zapp.job_service.service;

import com.zapp.job_service.dto.CreateJobRequestDto;
import com.zapp.job_service.dto.PartialUpdateJobRequestDto;
import com.zapp.job_service.dto.UpdateJobRequestDto;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.enums.JobStatus;

import java.util.UUID;

public interface IJobValidationService {

    void validateCreateJobRequest(CreateJobRequestDto dto);

    void validateUpdateJobRequest(Job existingJob, UpdateJobRequestDto dto);

    void validatePartialUpdateJobRequest(Job existingJob, PartialUpdateJobRequestDto dto);

    void validateStatusTransition(Job existingJob, JobStatus newStatus);

    void validateJobDeletion(Job existingJob);

}
