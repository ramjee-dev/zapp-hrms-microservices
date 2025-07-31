package com.zapp.candidate_service.service;

import com.zapp.candidate_service.dto.CreateCandidateRequestDto;
import com.zapp.candidate_service.dto.CreateCandidateRequestDto;
import com.zapp.candidate_service.dto.PartialUpdateCandidateRequestDto;
import com.zapp.candidate_service.dto.UpdateCandidateRequestDto;
import com.zapp.candidate_service.dto.UpdateCandidateRequestDto;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.enums.CandidateStatus;

import java.util.UUID;

public interface ICandidateValidationService {

    void validateCreateRequest(CreateCandidateRequestDto dto);

    void validateUpdateRequest(UUID candidateId, UpdateCandidateRequestDto dto);

    void validatePartialUpdateRequest(UUID candidateId, PartialUpdateCandidateRequestDto dto);

    void validateStatusTransition(Candidate candidate, CandidateStatus newStatus);

    void validateDeletion(Candidate candidate);
}
