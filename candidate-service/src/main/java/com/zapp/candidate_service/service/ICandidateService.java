package com.zapp.candidate_service.service;

import com.zapp.candidate_service.dto.CandidateDto;
import com.zapp.candidate_service.dto.CandidateDto;
import com.zapp.candidate_service.dto.CandidateFilter;
import com.zapp.candidate_service.entity.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICandidateService {

    /**
     * @param jobId - Input JobId
     * @param dto - Input CandidateDto object
     */
    void addCandidateToJob(Long jobId, CandidateDto dto);

    Page<Candidate> getAllCandidates(Pageable pageable, CandidateFilter filter);

    /**
     * @param candidateId - Input CandidateId
     * @return Candidate Details based on given CandidateId
     */
    CandidateDto getCandidateById(Long candidateId);

    Candidate updateCandidate(Long id, CandidateDto dto);

    Candidate updateCandidateStatus(Long id, Candidate.Status status);

    void deleteCandidate(Long id);

    boolean updateCommunicationStatus(Long candidateId);

    public boolean updateStatusCommunicationMap(Long id, String status);
}

