package com.zapp.candidate_service.service;

import com.zapp.candidate_service.dto.*;
import com.zapp.candidate_service.entity.Candidate;

import java.util.List;


public interface ICandidateMappingService {

    Candidate toEntity(CreateCandidateDto dto);

    void updateEntity(Candidate candidate, UpdateCandidateDto dto);

    void partialUpdateEntity(Candidate candidate, PartialUpdateCandidateDto dto);

    CandidateResponseDto toResponseDto(Candidate candidate);

    List<CandidateResponseDto> toResponseDtoList(List<Candidate> jobs);
}
