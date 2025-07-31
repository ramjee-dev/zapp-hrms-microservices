package com.zapp.candidate_service.repository;

import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.enums.ExperienceLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, UUID> {

    // Check if a candidate with the same email already exists for a particular job (case-insensitive)
    boolean existsByJobIdAndEmailIgnoreCase(UUID jobId, String email);

    Optional<Candidate> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    // Find all candidates assigned to a particular job with pagination and filters
    @Query("""
        SELECT c FROM Candidate c
        WHERE (:jobId IS NULL OR c.jobId = :jobId)
          AND (:status IS NULL OR c.status = :status)
          AND (:experienceLevel IS NULL OR c.experienceLevel = :experienceLevel)
          AND (:skills IS NULL OR LOWER(c.skills) LIKE LOWER(CONCAT('%', :skills, '%')))
          AND (:country IS NULL OR LOWER(c.country) LIKE LOWER(CONCAT('%', :country, '%')))
          AND (:firstName IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :firstName, '%')))
          AND (:lastName IS NULL OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :lastName, '%')))
        """)
    Page<Candidate> findCandidatesWithFilters(
            @Param("jobId") UUID jobId,
            @Param("status") CandidateStatus status,
            @Param("experienceLevel") ExperienceLevel experienceLevel,
            @Param("skills") String skills,
            @Param("country") String country,
            @Param("firstName") String firstName,
            @Param("lastName") String lastName,
            Pageable pageable);

    // Find candidates by status (non-paginated, for limited result use)
    List<Candidate> findByStatus(CandidateStatus status);

    // Find candidates assigned to a TAT user
    List<Candidate> findByAssignedTo(UUID userId);

    // Count candidates assigned to a job, useful for validations
    long countByJobId(UUID jobId);

    // Find candidates by assigned user and status with pagination (optional)
    @Query("""
        SELECT c FROM Candidate c
        WHERE (:userId IS NULL OR c.assignedTo = :userId)
          AND (:status IS NULL OR c.status = :status)
        """)
    Page<Candidate> findByAssignedToAndStatus(
            @Param("userId") UUID userId,
            @Param("status") CandidateStatus status,
            Pageable pageable);

    // Find candidates by partial name match (first or last name) with pagination
    @Query("""
        SELECT c FROM Candidate c
        WHERE (:keyword IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
              OR LOWER(c.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """)
    Page<Candidate> findByNameKeyword(
            @Param("keyword") String keyword,
            Pageable pageable);
}


