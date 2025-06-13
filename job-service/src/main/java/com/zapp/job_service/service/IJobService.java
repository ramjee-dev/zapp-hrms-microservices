package com.zapp.job_service.service;

import com.zapp.job_service.dto.JobRequestDTO;
import com.zapp.job_service.dto.JobResponseDTO;
import com.zapp.job_service.entity.Job;

import java.util.List;
import java.util.Optional;

public interface IJobService {

    JobResponseDTO createJob(JobRequestDTO dto);

    List<JobResponseDTO> getAllJobs(Optional<Long> clientId, Optional<Job.JobStatus> status);

    JobResponseDTO getJobById(Long id);

    JobResponseDTO updateJob(Long id, JobRequestDTO dto);

    void deleteJob(Long id);
}


