package com.zapp.job_service.service;

import com.zapp.job_service.dto.CreateJobDto;
import com.zapp.job_service.dto.UpdateJobDto;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.enums.JobStatus;

import java.util.UUID;

public interface IJobValidationService {

    void validateCreateJob(CreateJobDto dto);

    void validateUpdateJob(UUID jobId, UpdateJobDto dto);

    void validateStatusTransition(Job job, JobStatus newStatus);


}
