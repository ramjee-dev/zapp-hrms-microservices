package com.zapp.client_service.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@Builder
@AllArgsConstructor@NoArgsConstructor
public class ClientPartialUpdateRequestDto {

    @Size(min = 2, max = 100, message = "Client name must be between 2 and 100 characters")
    private String name;

    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;

    private Status status;

    public enum Status {
        ACTIVE, INACTIVE
    }

}
