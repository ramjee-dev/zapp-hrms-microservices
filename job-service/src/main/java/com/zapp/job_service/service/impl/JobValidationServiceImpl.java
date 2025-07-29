package com.zapp.job_service.service.impl;

import com.zapp.job_service.dto.CreateJobRequestDto;
import com.zapp.job_service.dto.UpdateJobRequestDto;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.enums.JobStatus;
import com.zapp.job_service.exception.JobValidationException;
import com.zapp.job_service.service.IJobValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class JobValidationServiceImpl implements IJobValidationService {

    @Override
    public void validateCreateJobRequest(CreateJobRequestDto dto) {

        log.debug("Validating job creation for client: {}", dto.clientId());

        List<String> errors = new ArrayList<>();

        // Business validation rules
        if (dto.positionsAvailable() != null && dto.positionsAvailable() <= 0) {
            errors.add("Positions available must be greater than 0");
        }

        // Additional business validations can be added here
        // e.g., validate client exists, check user permissions, etc.

        if (!errors.isEmpty()) {
            log.warn("Job creation validation failed: {}", errors);
            throw new JobValidationException("Job validation failed", errors);
        }

        log.debug("Job creation validation passed for client: {}", dto.clientId());
    }

    @Override
    public void validateUpdateJobRequest(UUID jobId, UpdateJobRequestDto updateJobRequestDto) {

        log.debug("Validating job update for job: {}", jobId);

        List<String> errors = new ArrayList<>();

        // Business validation rules for update
        if (updateJobRequestDto.positionsAvailable() != null && updateJobRequestDto.positionsAvailable() <= 0) {
            errors.add("Positions available must be greater than 0");
        }

        // 2. Business Rule: Do not allow updates to CLOSED jobs
//        if (updateJobDto.getStatus() == Job.JobStatus.CLOSED) {
//            throw new BusinessRuleViolationException("Cannot update a job that is already CLOSED.");
//        }

        // 3. Business Rule: clientId is immutable
//        if (!existingJob.getClientId().equals(jobDto.getClientId())) {
//            throw new BusinessRuleViolationException("Client ID of a job cannot be changed.");
//        }

        // 4. Business Rule: Check for duplicate title under same client (excluding current job)
//        Optional<Job> duplicateJob = jobRepository
//                .findByClientIdAndTitle(jobDto.getClientId(), jobDto.getTitle());
//
//        if (duplicateJob.isPresent() && !duplicateJob.get().getJobId().equals(jobId)) {
//            throw new JobAlreadyExistsException("A job with title '" + jobDto.getTitle()
//                    + "' already exists for client ID " + jobDto.getClientId());
//        }

        if (!errors.isEmpty()) {
            log.warn("Job update validation failed for job {}: {}", jobId, errors);
            throw new JobValidationException("Job update validation failed", errors);
        }

        log.debug("Job update validation passed for job: {}", jobId);
    }



    @Override
    public void validateStatusTransition(Job job, JobStatus newStatus) {

        log.debug("Validating status transition from {} to {} for job: {}",
                job.getStatus(), newStatus, job.getId());

        JobStatus currentStatus = job.getStatus();

        // Define valid status transitions
        boolean isValidTransition = switch (currentStatus) {
            case DRAFT -> newStatus == JobStatus.PUBLISHED || newStatus == JobStatus.CANCELLED;
            case PUBLISHED -> newStatus == JobStatus.ON_HOLD || newStatus == JobStatus.CLOSED ||
                    newStatus == JobStatus.CANCELLED;
            case ON_HOLD -> newStatus == JobStatus.PUBLISHED || newStatus == JobStatus.CLOSED ||
                    newStatus == JobStatus.CANCELLED;
            case CLOSED, CANCELLED -> false; // Terminal states
        };

        if (!isValidTransition) {
            String message = String.format("Invalid status transition from %s to %s",
                    currentStatus, newStatus);
            log.warn("Status transition validation failed for job {}: {}", job.getId(), message);
            throw new JobValidationException(message);
        }

        log.debug("Status transition validation passed for job: {}", job.getId());
    }
}
