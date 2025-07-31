package com.zapp.candidate_service.service;

import com.zapp.candidate_service.dto.*;
import com.zapp.candidate_service.entity.Candidate;

import java.util.List;


public interface ICandidateMappingService {

    Candidate toEntity(CreateCandidateRequestDto dto);

    void updateEntity(Candidate existingCandidate, UpdateCandidateRequestDto dto);

    void partialUpdateEntity(Candidate existingCandidate, PartialUpdateCandidateRequestDto dto);

    CandidateResponseDto toResponseDto(Candidate candidate);

    List<CandidateResponseDto> toResponseDtoList(List<Candidate> jobs);
}
