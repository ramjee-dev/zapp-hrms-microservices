package com.zapp.candidate_service.repository;

import com.zapp.candidate_service.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CandidateRepository extends JpaRepository<Candidate, Long>, JpaSpecificationExecutor<Candidate> {

    // ✅ Checks if a candidate with the same email exists for a given job (case-insensitive)
    boolean existsByJobIdAndEmailIgnoreCase(Long jobId, String email);
}


