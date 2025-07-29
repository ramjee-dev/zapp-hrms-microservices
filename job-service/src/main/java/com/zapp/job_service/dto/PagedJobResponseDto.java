package com.zapp.job_service.dto;

import java.util.List;

public record PagedJobResponseDto(

        int pageNumber,
        int totalPages,
        long totalElements,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious,
        List<JobResponseDto> data
) {
}
