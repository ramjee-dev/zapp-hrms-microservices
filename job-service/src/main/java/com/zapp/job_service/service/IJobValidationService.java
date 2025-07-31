package com.zapp.job_service.service;

import com.zapp.job_service.dto.CreateJobRequestDto;
import com.zapp.job_service.dto.PartialUpdateJobRequestDto;
import com.zapp.job_service.dto.UpdateJobRequestDto;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.enums.JobStatus;

import java.util.UUID;

public interface IJobValidationService {

    void validateCreateJobRequest(CreateJobRequestDto dto);

    void validateUpdateJobRequest(UUID jobId, UpdateJobRequestDto dto);

    void validatePartialUpdateJobRequest(UUID jobId, PartialUpdateJobRequestDto dto);

    void validateStatusTransition(Job job, JobStatus newStatus);

    void validateJobDeletion(Job job);

}
