package com.zapp.job_service.service;

import com.zapp.job_service.dto.*;
import com.zapp.job_service.enums.JobStatus;

import java.util.UUID;

public interface IJobService {


    JobResponseDto createJob(CreateJobDto dto);

    JobResponseDto fetchJobById(UUID jobId);

    PagedJobResponseDto fetchAllJobs(JobPageRequestDto requestDto);

    JobResponseDto updateJob(UUID jobId, UpdateJobDto updateJobDto);

    JobResponseDto partialUpdateJob(UUID jobId, PartialUpdateJobDto partialUpdateJobDto);

    JobResponseDto changeJobStatus (UUID jobId, JobStatus status);

    void deleteJob(UUID jobId);

    boolean updateCommunicationStatus(Long jobId);
}


