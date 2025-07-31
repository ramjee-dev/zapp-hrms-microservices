package com.zapp.candidate_service.dto;

import java.util.List;

public record PagedCandidateResponseDto(
        int pageNumber,
        int totalPages,
        long totalElements,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious,
        List<CandidateResponseDto> data
) {}
