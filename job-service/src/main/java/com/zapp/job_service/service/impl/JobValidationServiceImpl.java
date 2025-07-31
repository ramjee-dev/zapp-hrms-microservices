package com.zapp.job_service.service.impl;

import com.zapp.job_service.dto.CreateJobRequestDto;
import com.zapp.job_service.dto.PartialUpdateJobRequestDto;
import com.zapp.job_service.dto.UpdateJobRequestDto;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.enums.JobStatus;
import com.zapp.job_service.exception.BusinessValidationException;
import com.zapp.job_service.exception.JobAlreadyExistsException;
import com.zapp.job_service.exception.ResourceNotFoundException;
import com.zapp.job_service.repository.JobRepository;
import com.zapp.job_service.service.IJobValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobValidationServiceImpl implements IJobValidationService {

    private final JobRepository jobRepository;
    private final ClientRepository clientRepository;
    // Inject other services or repos as needed for cross-entity validations

    @Override
    public void validateCreateJobRequest(CreateJobRequestDto dto) {
        log.debug("Validating job creation request for clientId={}", dto.clientId());

        List<String> errors = new ArrayList<>();

        // Positions available must be positive
        if (dto.positionsAvailable() != null && dto.positionsAvailable() <= 0) {
            errors.add("Positions available must be greater than 0");
        }

        // Validate Client Existence
        if (dto.clientId() == null || !clientRepository.existsById(dto.clientId())) {
            errors.add("Client does not exist for clientId: " + dto.clientId());
        }

        // Validate duplicate job title for same client
        if (dto.title() != null && dto.clientId() != null) {
            boolean duplicateExists = jobRepository.existsByClientIdAndTitleIgnoreCase(dto.clientId(), dto.title().trim());
            if (duplicateExists) {
                throw new JobAlreadyExistsException("Job with title '" + dto.title().trim() + "' already exists for client " + dto.clientId());
            }
        }

        // Add more validations: e.g., validate status initial value if included in DTO

        if (!errors.isEmpty()) {
            log.warn("Job creation validation failed for clientId {}: {}", dto.clientId(), errors);
            throw new BusinessValidationException("Job validation failed on creation", errors);
        }

        log.info("Job creation validation passed for clientId={}", dto.clientId());
    }

    @Override
    public void validateUpdateJobRequest(UUID jobId, UpdateJobRequestDto dto) {
        log.debug("Validating job update for jobId={}", jobId);

        List<String> errors = new ArrayList<>();

        // Check job existence
        Job existingJob = jobRepository.findById(jobId).orElseThrow(() ->
                new ResourceNotFoundException("Job", "id", jobId.toString())
        );

        // Positions available must be positive if present
        if (dto.positionsAvailable() != null && dto.positionsAvailable() <= 0) {
            errors.add("Positions available must be greater than 0");
        }

        // Do not allow updates to CLOSED jobs
        if (existingJob.getStatus() == JobStatus.CLOSED) {
            errors.add("Cannot update a job that is already CLOSED.");
        }

        // clientId must not change (immutable)
//        if (dto.clientId() != null && !dto.clientId().equals(existingJob.getClientId())) {
//            errors.add("Client ID of a job cannot be changed.");
//        } // client Id already removed from updateRequestDto

        // Check for duplicate title under same client (case-insensitive) excluding current job
        if (dto.title() != null && existingJob.getClientId() != null) {
            Optional<Job> duplicateJob = jobRepository.findByClientIdAndTitleIgnoreCase(existingJob.getClientId(), dto.title().trim());
            if (duplicateJob.isPresent() && !duplicateJob.get().getId().equals(jobId)) {
                throw new JobAlreadyExistsException("A job with title '" + dto.title().trim()
                        + "' already exists for client ID " + existingJob.getClientId());
            }
        }

        // Add additional update validations here...

        if (!errors.isEmpty()) {
            log.warn("Job update validation failed for jobId {}: {}", jobId, errors);
            throw new BusinessValidationException("Job update validation failed", errors);
        }

        log.info("Job update validation passed for jobId={}", jobId);
    }

    /**
     * OPTIONAL:
     * Add validatePartialUpdateJobRequest if your service supports PATCH
     */
    public void validatePartialUpdateJobRequest(UUID jobId, PartialUpdateJobRequestDto dto) {

        log.debug("Validating partial update for jobId={}", jobId);

        List<String> errors = new ArrayList<>();

        Job existingJob = jobRepository.findById(jobId).orElseThrow(() ->
                new ResourceNotFoundException("Job", "id", jobId.toString())
        );

        if (dto.positionsAvailable() != null && dto.positionsAvailable() <= 0) {
            errors.add("Positions available must be greater than 0");
        }

//        if (dto.clientId() != null && !dto.clientId().equals(existingJob.getClientId())) {
//            errors.add("Client ID of a job cannot be changed.");
//        }  // client Id already removed from updateRequestDto

        if (dto.title() != null) {
            Optional<Job> duplicateJob = jobRepository.findByClientIdAndTitleIgnoreCase(existingJob.getClientId(), dto.title().trim());
            if (duplicateJob.isPresent() && !duplicateJob.get().getId().equals(jobId)) {
                throw new JobAlreadyExistsException("A job with title '" + dto.title().trim()
                        + "' already exists for client ID " + existingJob.getClientId());
            }
        }

        if (!errors.isEmpty()) {
            log.warn("Job partial update validation failed for jobId {}: {}", jobId, errors);
            throw new BusinessValidationException("Job partial update validation failed", errors);
        }

        log.info("Job partial update validation passed for jobId={}", jobId);
    }

    @Override
    public void validateStatusTransition(Job job, JobStatus newStatus) {
        log.debug("Validating status transition from {} to {} for jobId={}",
                job.getStatus(), newStatus, job.getId());

        JobStatus currentStatus = job.getStatus();

        Map<JobStatus, Set<JobStatus>> validTransitions = Map.of(
                JobStatus.DRAFT, Set.of(JobStatus.PUBLISHED, JobStatus.CANCELLED),
                JobStatus.PUBLISHED, Set.of(JobStatus.ON_HOLD, JobStatus.CLOSED, JobStatus.CANCELLED),
                JobStatus.ON_HOLD, Set.of(JobStatus.PUBLISHED, JobStatus.CLOSED, JobStatus.CANCELLED)
                // CLOSED and CANCELLED are terminal statuses, no transitions allowed
        );

        boolean isValid = validTransitions.getOrDefault(currentStatus, Collections.emptySet()).contains(newStatus);

        if (!isValid) {
            String message = String.format("Invalid status transition from %s to %s", currentStatus, newStatus);
            log.warn("Status transition validation failed for jobId {}: {}", job.getId(), message);
            throw new BusinessValidationException(message);
        }

        log.info("Status transition validation passed for jobId={} to status={}", job.getId(), newStatus);
    }

    @Override
    public void validateJobDeletion(Job job) {
        log.debug("Validating job deletion for jobId={}", job.getId());

        List<String> errors = new ArrayList<>();

        // Example business rule: Disallow deletion if job is in active or published state
        if (job.getStatus() == JobStatus.PUBLISHED || job.getStatus() == JobStatus.ON_HOLD) {
            errors.add("Cannot delete a job that is currently published or on hold.");
        }

        // Example: Disallow deletion if candidates are assigned to this job
        // (Assuming you have a candidateRepository or candidateService with count method)
    /*
    long candidateCount = candidateRepository.countByJobId(job.getId());
    if (candidateCount > 0) {
        errors.add("Cannot delete a job with assigned candidates.");
    }
    */

        // Add any additional business rules here (e.g., checking related entities)

        if (!errors.isEmpty()) {
            log.warn("Job deletion validation failed for jobId {}: {}", job.getId(), errors);
            throw new BusinessValidationException("Job deletion validation failed", errors);
        }

        log.info("Job deletion validation passed for jobId={}", job.getId());
    }
}
