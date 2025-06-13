package com.zapp.candidate_service.mapper;

import com.zapp.candidate_service.dto.CandidateDTO;
import com.zapp.candidate_service.entity.Candidate;
import org.springframework.stereotype.Component;

@Component
public class CandidateMapper {

    public Candidate toEntity(CandidateDTO dto) {
        return Candidate.builder()
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .remarks(dto.getRemarks())
                .build();
    }
}
