package com.zapp.job_service.repository;

import com.zapp.job_service.entity.Job;
import com.zapp.job_service.enums.JobStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID>, JpaSpecificationExecutor<Job> {

    /**
     * Finds all jobs for a given client.
     * Consider adding Pageable for large result sets.
     */
    List<Job> findByClientId(UUID clientId);  // Consider Page<Job> + Pageable

    /**
     * Finds all jobs with the given status.
     * Consider adding Pageable for large result sets.
     */
    List<Job> findByStatus(JobStatus status);  // Consider Page<Job> + Pageable

    /**
     * Finds jobs by client ID and status.
     */
    List<Job> findByClientIdAndStatus(UUID clientId, JobStatus status);  // consider pagination

    /**
     * Finds a job by client ID and title (case insensitive).
     */
    Optional<Job> findByClientIdAndTitleIgnoreCase(UUID clientId, String title);

    /**
     * Checks if a job exists for given client ID and title (case insensitive).
     */
    boolean existsByClientIdAndTitleIgnoreCase(UUID clientId, String title);

    /**
     * Dynamic filtering of jobs with optional criteria.
     * Supports pagination.
     */
    @Query("SELECT j FROM Job j WHERE " +
            "(:clientId IS NULL OR j.clientId = :clientId) AND " +
            "(:status IS NULL OR j.status = :status) AND " +
            "(:department IS NULL OR LOWER(j.department) LIKE LOWER(CONCAT('%', :department, '%'))) AND " +
            "(:location IS NULL OR LOWER(j.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:title IS NULL OR LOWER(j.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    Page<Job> findJobsWithFilters(@Param("clientId") UUID clientId,
                                  @Param("status") JobStatus status,
                                  @Param("department") String department,
                                  @Param("location") String location,
                                  @Param("title") String title,
                                  Pageable pageable);
}
