package com.zapp.job_service.repository;

import com.zapp.job_service.entity.Job;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByClientId(Long clientId);
    List<Job> findByStatus(Job.JobStatus status);
    List<Job> findByClientIdAndStatus(Long clientId, Job.JobStatus status);

    Optional<Job> findByClientIdAndTitle(Long clientId,String title);
}

