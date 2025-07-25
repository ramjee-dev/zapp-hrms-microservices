package com.zapp.client_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Client",description = "schema to hold client information")
@Data@Builder
@NoArgsConstructor@AllArgsConstructor
public class ClientCreateRequestDto {

    @Schema(description = "Name of the Client",example = "Facebook")
    @NotBlank(message = "Client name is required")
    @Size(min = 2, max = 100, message = "Client name must be between 2 and 100 characters")
    private String name;

    @Schema(description = "Location of the Client",example = "California")
    @Size(max = 100, message = "Location cannot exceed 100 characters")
    private String location;
}
