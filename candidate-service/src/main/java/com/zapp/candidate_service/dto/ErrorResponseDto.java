package com.zapp.candidate_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data@AllArgsConstructor@NoArgsConstructor
@Schema(name = "ErrorResponse",description = "schema to hold Error Response information")
public class ErrorResponseDto {

    @Schema(description = "API path invoked by Client")
    private String apiPath;
    @Schema(description = "Error Code representing the error happened")
    private HttpStatus errorCode;
    @Schema(description = "Error message representing the error happened")
    private String errorMsg;
    @Schema(description = "Time representing when the error happened")
    private LocalDateTime errorTime;
}
