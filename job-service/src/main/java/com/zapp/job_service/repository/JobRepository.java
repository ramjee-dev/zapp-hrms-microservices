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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {

    List<Job> findByClientId(UUID clientId);

    List<Job> findByStatus(JobStatus status);

    List<Job> findByClientIdAndStatus(Long clientId, JobStatus status);

    Optional<Job> findByClientIdAndTitle(Long clientId,String title);

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

