package com.zapp.job_service.service;

import com.zapp.job_service.dto.*;
import com.zapp.job_service.enums.JobStatus;

import java.util.UUID;

public interface IJobService {

    JobResponseDto createJob(CreateJobRequestDto dto);

    JobResponseDto fetchJobById(UUID jobId);

    PagedJobResponseDto fetchAllJobs(JobPageRequestDto requestDto);

    JobResponseDto updateJob(UUID jobId, UpdateJobRequestDto updateJobRequestDto);

    JobResponseDto partialUpdateJob(UUID jobId, PartialUpdateJobRequestDto partialUpdateJobRequestDto);

    JobResponseDto changeJobStatus (UUID jobId, JobStatus status);

    void deleteJob(UUID jobId);

    boolean updateCommunicationStatus(UUID jobId);
}


