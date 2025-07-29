package com.zapp.client_service.dto;

import com.zapp.client_service.enums.ClientType;
import jakarta.validation.constraints.*;

public record UpdateClientRequestDto(

        @NotBlank(message = "Company name is required")
        @Size(min = 2, max = 200, message = "Company name must be between 2 and 200 characters")
        String companyName,

        @NotBlank(message = "Contact person is required")
        @Size(max = 100, message = "Contact person must not exceed 100 characters")
        String contactPerson,

        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
        String phone,

        @Size(max = 500, message = "Address must not exceed 500 characters")
        String address,

        @Size(max = 100, message = "City must not exceed 100 characters")
        String city,

        @Size(max = 100, message = "State must not exceed 100 characters")
        String state,

        @Size(max = 20, message = "Postal code must not exceed 20 characters")
        String postalCode,

        @Size(max = 100, message = "Country must not exceed 100 characters")
        String country,

        @Size(max = 200, message = "Website must not exceed 200 characters")
        String website,

        @NotNull(message = "Client type is required")
        ClientType clientType,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Min(value = 1, message = "Employee count must be at least 1")
        Integer employeeCount,

        @Size(max = 100, message = "Industry must not exceed 100 characters")
        String industry
) {}

