package com.zapp.candidate_service.mapper;

import com.zapp.candidate_service.entity.Candidate;

public class CandidateMapper {

    public static CandidateDto mapToCandidateDto(Candidate candidate, CandidateDto candidateDto) {
        candidateDto.setFullName(candidate.getFullName());
        candidateDto.setEmail(candidate.getEmail());
        candidateDto.setPhone(candidate.getPhone());
        candidateDto.setRemarks(candidate.getRemarks());
        return candidateDto;
    }

    public static Candidate mapToCandidate(CandidateDto dto, Candidate candidate) {
        candidate.setFullName(dto.getFullName());
        candidate.setEmail(dto.getEmail());
        candidate.setPhone(dto.getPhone());
        candidate.setRemarks(dto.getRemarks());
        return candidate;
    }
}

