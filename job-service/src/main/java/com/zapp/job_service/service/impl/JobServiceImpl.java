package com.zapp.job_service.service.impl;

import com.zapp.job_service.dto.JobRequestDTO;
import com.zapp.job_service.dto.JobResponseDTO;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.exception.ResourceNotFoundException;
import com.zapp.job_service.mapper.JobMapper;
import com.zapp.job_service.repository.JobRepository;
import com.zapp.job_service.service.IJobService;
import com.zapp.job_service.service.client.ClientServiceFeignClient;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
public class JobServiceImpl implements IJobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobMapper jobMapper;

    @Autowired
    private ClientServiceFeignClient clientFeignClient;

    @Override
    public JobResponseDTO createJob(JobRequestDTO dto) {
        // Validate clientId via Feign
        try {
            clientFeignClient.getClientById(dto.getClientId());
        } catch (FeignException.NotFound ex) {
            throw new ResourceNotFoundException("Client not found with ID: " + dto.getClientId());
        }

        Job job = jobMapper.toEntity(dto);
        job = jobRepository.save(job);
        return jobMapper.toDTO(job);
    }

    @Override
    public List<JobResponseDTO> getAllJobs(Optional<Long> clientId, Optional<Job.JobStatus> status) {
        List<Job> jobs;

        if (clientId.isPresent() && status.isPresent()) {
            jobs = jobRepository.findByClientIdAndStatus(clientId.get(), status.get());
        } else if (clientId.isPresent()) {
            jobs = jobRepository.findByClientId(clientId.get());
        } else if (status.isPresent()) {
            jobs = jobRepository.findByStatus(status.get());
        } else {
            jobs = jobRepository.findAll();
        }

        return jobs.stream()
                .map(jobMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public JobResponseDTO getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));
        return jobMapper.toDTO(job);
    }

    @Override
    public JobResponseDTO updateJob(Long id, JobRequestDTO dto) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));

        jobMapper.updateJobFromDto(job, dto);
        job = jobRepository.save(job);
        return jobMapper.toDTO(job);
    }

    @Override
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with ID: " + id));
        jobRepository.delete(job);
    }
}


