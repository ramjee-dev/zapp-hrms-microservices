package com.zapp.job_service.service.impl;

import com.zapp.job_service.dto.*;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.enums.JobStatus;
import com.zapp.job_service.exception.ResourceNotFoundException;
import com.zapp.job_service.repository.JobRepository;
import com.zapp.job_service.service.IJobMappingService;
import com.zapp.job_service.service.IJobService;
import com.zapp.job_service.service.IJobValidationService;
import com.zapp.job_service.service.client.ClientServiceFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor@Slf4j
@Transactional(readOnly = true)
public class JobServiceImpl implements IJobService {

    private final JobRepository jobRepository;
    private final IJobValidationService validationService;
    private final IJobMappingService mappingService;
    private ClientServiceFeignClient clientFeignClient;
    private StreamBridge streamBridge;


    @Override
    @Transactional
    public JobResponseDto createJob(CreateJobRequestDto dto) {
        log.info("Creating new job with title '{}' for client '{}'", dto.title(), dto.clientId());
        validationService.validateCreateJobRequest(dto);
        Job job = mappingService.toEntity(dto);
        Job saved = jobRepository.save(job);
        log.info("Created job with id '{}'", saved.getId());
        return mappingService.toResponseDto(saved);
    }

    private void sendCommunication(Job job){
        JobCreatedEvent jobCreatedEvent = new JobCreatedEvent(job.getId(), job.getTitle(), job.getClientId()+"","anonymous",job.getCreatedAt()+"", Arrays.asList("ADMIN","DB"));
        log.info("Sending communication request for the details: {}",jobCreatedEvent);
        boolean send = streamBridge.send("sendCommunication-out-0", jobCreatedEvent);
        log.info("Is the communication request successfully triggered ? : {}",send);
    }

    @Override
    public JobResponseDto fetchJobById(UUID jobId) {
        log.debug("Fetching job by id '{}'", jobId);
        Job job = findJobOrThrow(jobId);
        return mappingService.toResponseDto(job);
    }

    private Job findJobOrThrow(UUID jobId) {
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "jobId", jobId.toString()));
    }

    @Override
    public PagedJobResponseDto fetchAllJobs(JobPageRequestDto requestDto) {

        log.debug("Fetching jobs with filters - {}", requestDto);

        Sort sort = Sort.by(Sort.Direction.fromString(requestDto.sortDir()), requestDto.sortBy());
        Pageable pageable = PageRequest.of(requestDto.page(), requestDto.size(), sort);

        Page<Job> jobPage = jobRepository.findJobsWithFilters(
                requestDto.clientId(),
                requestDto.status(),
                requestDto.department(),
                requestDto.location(),
                requestDto.title(),
                pageable
        );

        return new PagedJobResponseDto(
                jobPage.getNumber(),
                jobPage.getTotalPages(),
                jobPage.getTotalElements(),
                jobPage.isFirst(),
                jobPage.isLast(),
                jobPage.hasNext(),
                jobPage.hasPrevious(),
                mappingService.toResponseDtoList(jobPage.getContent())
        );
    }

    @Override
    @Transactional
    public JobResponseDto updateJob(UUID jobId, UpdateJobRequestDto updateJobRequestDto) {

        log.info("Updating job with id: {}", jobId);

        validationService.validateUpdateJobRequest(jobId, updateJobRequestDto);
        Job existingJob = findJobOrThrow(jobId);

        mappingService.updateEntity(existingJob, updateJobRequestDto);

        Job updatedJob = jobRepository.save(existingJob);

        log.info("Successfully updated job with id: {}", jobId);

        return mappingService.toResponseDto(updatedJob);
    }

    @Override
    @Transactional
    public JobResponseDto partialUpdateJob(UUID jobId, PartialUpdateJobRequestDto partialUpdateJobRequestDto) {

        log.info("Partially updating job with id: {}", jobId);

        validationService.validatePartialUpdateJobRequest(jobId,partialUpdateJobRequestDto);

        Job existingJob = findJobOrThrow(jobId);

        mappingService.partialUpdateEntity(existingJob, partialUpdateJobRequestDto);

        Job updatedJob = jobRepository.save(existingJob);

        log.info("Successfully partially updated job with id: {}", jobId);

        return mappingService.toResponseDto(updatedJob);
    }

    @Override
    @Transactional
    public JobResponseDto changeJobStatus(UUID jobId, JobStatus status) {

        log.info("Changing status of job {} to {}", jobId, status);

        Job existingJob = findJobOrThrow(jobId);

        validationService.validateStatusTransition(existingJob,status);
        existingJob.setStatus(status);
        Job updatedJob = jobRepository.save(existingJob);

        log.info("Successfully changed status of job {} to {}", jobId, status);

        return mappingService.toResponseDto(updatedJob);
    }

    @Transactional
    @Override
    public void deleteJob(UUID jobId) {

        log.info("Deleting job with id: {}", jobId);

        Job existingJob = findJobOrThrow(jobId);
        validationService.validateJobDeletion(existingJob);

        jobRepository.delete(existingJob);

        log.info("Successfully deleted job with id: {}", jobId);
    }

    @Override
    public boolean updateCommunicationStatus(UUID jobId) {
        return false;
    }

//    @Override
//    public boolean updateCommunicationStatus(UUID jobId) {
//        boolean isUpdated = false;
//        if(jobId!=null){
//            Job job = jobRepository.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("Job","jobId",jobId+""));
//            job.setCommunicationSw(true);
//            jobRepository.save(job);
//            isUpdated=true;
//        }
//        return isUpdated;
//    }
}


