package com.zapp.candidate_service.service;

import com.zapp.candidate_service.dto.CreateCandidateDto;
import com.zapp.candidate_service.dto.UpdateCandidateDto;

import java.util.UUID;

public interface ICandidateValidationService {

    void validateCreateCandidate(CreateCandidateDto dto);

    void validateUpdateJob(UUID candidateId, UpdateCandidateDto dto);
}
