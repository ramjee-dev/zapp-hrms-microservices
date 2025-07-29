package com.zapp.client_service.dto;

import java.util.List;

public record PagedClientResponseDto(

        int pageNumber,
        int totalPages,
        long totalElements,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious,
        List<ClientResponseDto> data
) {
}
