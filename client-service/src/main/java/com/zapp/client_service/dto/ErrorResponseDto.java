package com.zapp.client_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ErrorResponse",description = "schema to hold Error Response information")
public class ErrorResponseDto {

        private String path;
        private int status;
        private String error;
        private String message;
        private LocalDateTime timestamp;
        private Object details; // Optional: To send validation errors etc.

//    @Schema(description = "API path invoked by Client")
//    private String apiPath;
//    @Schema(description = "Error Code representing the error happened")
//    private HttpStatus errorCode;
//    @Schema(description = "Error message representing the error happened")
//    private String errorMsg;
//    @Schema(description = "Time representing when the error happened")
//    private LocalDateTime errorTime;
}
