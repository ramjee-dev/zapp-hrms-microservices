package com.zapp.client_service.dto;

import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.enums.ClientType;

import java.util.UUID;

public record ClientResponseDto(
        UUID id,
        String companyName,
        String contactPerson,
        String email,
        String phone,
        String address,
        String city,
        String state,
        String postalCode,
        String country,
        String website,
        ClientType clientType,
        ClientStatus status,
        String description,
        Integer employeeCount,
        String industry,
        String createdAt,
        String updatedAt,
        String createdBy,
        String updatedBy
) {}
