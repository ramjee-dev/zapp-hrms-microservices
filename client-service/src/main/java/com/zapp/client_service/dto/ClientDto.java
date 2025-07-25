package com.zapp.client_service.dto;

import com.zapp.client_service.entity.Client;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor@NoArgsConstructor
@Schema(
        name = "Client",
        description = "schema to hold client information"
)
public class ClientDto {

    private long clientId;

    @Schema(description = "Name of the Client",example = "Facebook")
    @NotBlank(message = "Client name cannot be null or blank")
    @Size(min = 4, max = 30, message = "Client name must be between 4 and 30 characters")
    private String name;

    @Schema(description = "Location of the Client",example = "California")
    @NotBlank(message = "Location cannot be null or blank")
    @Size(max = 100, message = "Location must not exceed 100 characters")
    private String location;

    @Schema(description = "Status of the Client",example = "Active or InActive")
    private Client.Status status;
}
