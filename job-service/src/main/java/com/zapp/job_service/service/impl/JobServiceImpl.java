package com.zapp.job_service.service.impl;

import com.zapp.job_service.dto.JobCreatedEvent;
import com.zapp.job_service.dto.JobDto;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.exception.BusinessRuleViolationException;
import com.zapp.job_service.exception.JobAlreadyExistsException;
import com.zapp.job_service.exception.ResourceNotFoundException;
import com.zapp.job_service.mapper.JobMapper;
import com.zapp.job_service.repository.JobRepository;
import com.zapp.job_service.service.IJobService;
import com.zapp.job_service.service.client.ClientServiceFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobServiceImpl implements IJobService {

    private JobRepository jobRepository;
    private ClientServiceFeignClient clientFeignClient;
    private StreamBridge streamBridge;

    /**
     *
     * @param dto - Input JobDto object
     */
    @Override
    public void createJob(JobDto dto) {

        // 1. Validate client existence via Feign
        try {
            clientFeignClient.getClientById(dto.getClientId());
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Client","ClientId",dto.getClientId()+"");
        }

        // 2. Check for existing job with same title for the same client
        Optional<Job> existingJob = jobRepository.findByClientIdAndTitle(dto.getClientId(), dto.getTitle());
        if (existingJob.isPresent()) {
            throw new JobAlreadyExistsException("A job with title '" + dto.getTitle() +
                    "' already exists for client ID " + dto.getClientId());
        }

        Job job = JobMapper.mapToJob(dto, new Job());
        Job savedJob = jobRepository.save(job);
        sendCommunication(savedJob);


    }

    private void sendCommunication(Job job){

        JobCreatedEvent jobCreatedEvent = new JobCreatedEvent(job.getJobId(), job.getTitle(), job.getClientId()+"","anonymous",job.getCreatedAt()+"", Arrays.asList("ADMIN","DB"));
        log.info("Sending communication request for the details: {}",jobCreatedEvent);
        boolean send = streamBridge.send("sendCommunication-out-0", jobCreatedEvent);
        log.info("Is the communication request successfully triggered ? : {}",send);

    }

//    @Override
//    public List<JobResponseDTO> getAllJobs(Optional<Long> clientId, Optional<Job.JobStatus> status) {
//        List<Job> jobs;
//
//        if (clientId.isPresent() && status.isPresent()) {
//            jobs = jobRepository.findByClientIdAndStatus(clientId.get(), status.get());
//        } else if (clientId.isPresent()) {
//            jobs = jobRepository.findByClientId(clientId.get());
//        } else if (status.isPresent()) {
//            jobs = jobRepository.findByStatus(status.get());
//        } else {
//            jobs = jobRepository.findAll();
//        }
//
//        return jobs.stream()
//                .map(jobMapper::toDTO)
//                .collect(Collectors.toList());
//    }

    /**
     * @param jobId - Input JobId
     * @return Job Details based on given JobId
     */
    @Override
    public JobDto fetchJobById(Long jobId) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job","JobId",jobId+""));
        return JobMapper.mapToJobDto(job,new JobDto());
    }

    /**
     * @param jobId - Input JobId
     * @param jobDto - Input JobDto object
     * @return boolean indicating if the update of Job details is successful or not
     */
    @Override
    public boolean updateJob(Long jobId, JobDto jobDto) {

        boolean isUpdated = false;

        if(jobId!=null && jobDto!=null) {

            // 1. Fetch existing job
            Job existingJob = jobRepository.findById(jobId)
                    .orElseThrow(() -> new ResourceNotFoundException("Job","JobId",jobId+""));

            // 2. Business Rule: Do not allow updates to CLOSED jobs
            if (existingJob.getStatus() == Job.JobStatus.CLOSED) {
                throw new BusinessRuleViolationException("Cannot update a job that is already CLOSED.");
            }

            // 3. Business Rule: clientId is immutable
            if (!existingJob.getClientId().equals(jobDto.getClientId())) {
                throw new BusinessRuleViolationException("Client ID of a job cannot be changed.");
            }

            // 4. Business Rule: Check for duplicate title under same client (excluding current job)
            Optional<Job> duplicateJob = jobRepository
                    .findByClientIdAndTitle(jobDto.getClientId(), jobDto.getTitle());

            if (duplicateJob.isPresent() && !duplicateJob.get().getJobId().equals(jobId)) {
                throw new JobAlreadyExistsException("A job with title '" + jobDto.getTitle()
                        + "' already exists for client ID " + jobDto.getClientId());
            }

            // 5. Update mutable fields
            existingJob.setTitle(jobDto.getTitle());
            existingJob.setDescription(jobDto.getDescription());
            existingJob.setStatus(jobDto.getStatus() != null ? jobDto.getStatus() : existingJob.getStatus());

            // 6. Save updated job
            jobRepository.save(existingJob);
            isUpdated = true;
        }

        return isUpdated;

    }

    /**
     * @param jobId - Input JobId
     * @return boolean indicating if the delete of Job details is successful or not
     */
    @Override
    public boolean deleteJob(Long jobId) {
        boolean isDeleted = false;
        if(jobId!=null) {
            Job job = jobRepository.findById(jobId)
                    .orElseThrow(() -> new ResourceNotFoundException("Job","jobId",jobId+""));
            jobRepository.delete(job);
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public boolean updateCommunicationStatus(Long jobId) {
        boolean isUpdated = false;
        if(jobId!=null){
            Job job = jobRepository.findById(jobId).orElseThrow(() -> new ResourceNotFoundException("Job","jobId",jobId+""));
            job.setCommunicationSw(true);
            jobRepository.save(job);
            isUpdated=true;
        }
        return isUpdated;
    }
}


