package com.zapp.candidate_service.service;

import com.zapp.candidate_service.dto.CandidateDTO;
import com.zapp.candidate_service.dto.CandidateFilter;
import com.zapp.candidate_service.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICandidateService {

    Candidate addCandidateToJob(Long jobId, CandidateDTO dto);

    Page<Candidate> getAllCandidates(Pageable pageable, CandidateFilter filter);

    Candidate getCandidateById(Long id);

    Candidate updateCandidate(Long id, CandidateDTO dto);

    Candidate updateCandidateStatus(Long id, Candidate.Status status);

    void deleteCandidate(Long id);
}

