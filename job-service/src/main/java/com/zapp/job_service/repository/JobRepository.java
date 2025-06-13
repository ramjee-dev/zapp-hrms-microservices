package com.zapp.job_service.repository;

import com.zapp.job_service.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByClientId(Long clientId);
    List<Job> findByStatus(Job.JobStatus status);
    List<Job> findByClientIdAndStatus(Long clientId, Job.JobStatus status);
}

