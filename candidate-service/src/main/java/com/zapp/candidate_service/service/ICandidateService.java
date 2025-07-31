package com.zapp.candidate_service.service;

import com.zapp.candidate_service.dto.*;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.enums.CandidateStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ICandidateService {

    CandidateResponseDto createCandidate(CreateCandidateRequestDto dto);

    CandidateResponseDto fetchCandidateById(UUID candidateId);

    PagedCandidateResponseDto fetchAllCandidates(CandidatePageRequestDto pageRequestDto);

    CandidateResponseDto updateCandidate(UUID candidateId, UpdateCandidateRequestDto dto);

    CandidateResponseDto partialUpdateCandidate(UUID candidateId, PartialUpdateCandidateRequestDto dto);

    CandidateResponseDto changeCandidateStatus(UUID candidateId, CandidateStatus status);

    void deleteCandidate(UUID candidateId);

    boolean updateCommunicationStatus(UUID candidateId);

    public boolean updateStatusCommunicationMap(UUID candidateId, String status);
}

