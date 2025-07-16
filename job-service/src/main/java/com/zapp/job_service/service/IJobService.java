package com.zapp.job_service.service;

import com.zapp.job_service.dto.JobDto;

public interface IJobService {

    /**
     * @param dto - Input JobDto object
     */
    void createJob(JobDto dto);

//    List<JobResponseDTO> getAllJobs(Optional<Long> clientId, Optional<Job.JobStatus> status);

    /**
     * @param jobId - Input JobId
     * @return Job Details based on given JobId
     */
    JobDto fetchJobById(Long jobId);

    /**
     * @param jobId - Input JobId
     * @param jobDto - Input JobDto object
     * @return boolean indicating if the update of Job details is successful or not
     */
    boolean updateJob(Long jobId, JobDto jobDto);

    /**
     * @param jobId - Input JobId
     * @return boolean indicating if the delete of Job details is successful or not
     */
    boolean deleteJob(Long jobId);

    boolean updateCommunicationStatus(Long jobId);
}


