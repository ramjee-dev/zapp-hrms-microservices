package com.zapp.client_service.controller;

import com.zapp.client_service.constants.ClientsConstants;
import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.service.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Client Management",description = "APIs for managing ZAPP HRMS Clients")
public class ClientController {

    private final IClientService service;

    @Operation(summary = "Create a new client",description = "Creates a new client with the provided details")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Client created successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Client already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping
    public ResponseEntity<ResponseDto> createClient(@Valid @RequestBody ClientCreateRequestDto dto,
                                                    HttpServletRequest request) {
        log.info("Received request to create client: name='{}', requestId='{}'",
                dto.getName(), request.getHeader("X-Request-ID"));

        ClientResponseDto createdClient = service.createClient(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{clientId}")
                .buildAndExpand(createdClient.getClientId())
                .toUri();

        ResponseDto response = new ResponseDto(
                ClientsConstants.STATUS_201,
                ClientsConstants.MESSAGE_201,
                createdClient);

        log.info("Successfully created client: id={}, name='{}', requestId='{}'",createdClient.getClientId(),
                createdClient.getName(),request.getHeader("X-Request-ID"));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

//    @Retry(name = "getAllClients",fallbackMethod = "getAllClientsFallback")
//    @GetMapping
//    public Page<ClientDto> getAll(Pageable pageable) {
//        log.debug("getALLClients API invoked");
//        return service.getAllClients(pageable);
//    }

//    public Page<ClientDto> getAllClientsFallback(Pageable pageable, Throwable throwable) {
//        log.error("Fallback triggered for getAllClients due to: {}", throwable.getMessage());
//
//        List<ClientDto> fallbackList = Collections.emptyList(); // or pre-defined backup list
//        return new PageImpl<>(fallbackList, pageable, 0);
//    }

    @Operation(summary = "Get Client by ID",description = "Retrieves a client by their unique identifier")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Client retrieved successfully" ,
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found" ,
                    content =@Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/{clientId}")
    public ResponseEntity<ResponseDto> fetchClientDetails(@PathVariable("clientId") @Min(value = 1,
            message = "Client ID must be positive") Long clientId , HttpServletRequest request) {

        log.info("Received request to get client: id={}, requestId='{}'",
                clientId, request.getHeader("X-Request-ID"));

        ClientResponseDto clientDto = service.fetchClientById(clientId);

        ResponseDto response = new ResponseDto(
                ClientsConstants.STATUS_200,
                ClientsConstants.MESSAGE_200,
                clientDto);

        log.info("Successfully retrieved client: id={}, name='{}', requestId='{}'",
                clientDto.getClientId(), clientDto.getName(), request.getHeader("X-Request-ID"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieves a paginated list of clients with optional filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or filter parameters"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<ResponseDto> getAllClients(@Valid ClientPageRequestDto dto , HttpServletRequest request){

        log.info("Received request to get all clients: page={}, size={}, requestId='{}'",
                dto.getPage(), dto.getSize(), request.getHeader("X-Request-ID"));

        ClientPageResponseDto clients = service.getAllClients(dto);

        ResponseDto response = new ResponseDto(
                ClientsConstants.STATUS_200,
                ClientsConstants.MESSAGE_200,
                clients);

        log.info("Successfully retrieved {} clients, requestId='{}'",
                clients.getContent().size(), request.getHeader("X-Request-ID"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @Operation(summary = "Get clients by status", description = "Retrieves clients filtered by their status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDto> getClientsByStatus(
            @PathVariable @Pattern(regexp = "^(ACTIVE|INACTIVE)$", message = "Status must be ACTIVE or INACTIVE") String status,
            HttpServletRequest request) {

        log.info("Received request to get clients by status: status={}, requestId='{}'",
                status, request.getHeader("X-Request-ID"));

        List<ClientResponseDto> clients = service.getClientByStatus(Client.Status.valueOf(status));

        ResponseDto response = new ResponseDto(
                ClientsConstants.STATUS_200,
                ClientsConstants.MESSAGE_200,
                clients);

        log.info("Successfully retrieved {} clients with status={}, requestId='{}'",
                clients.size(), status, request.getHeader("X-Request-ID"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Update Client",description = "Updates all fields of an existing client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Client Updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found" ,
                    content =@Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Client already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PutMapping("/{clientId}")
    public ResponseEntity<ResponseDto> updateClientDetails(
            @PathVariable("clientId") @Min(value = 1,message = "Client ID must be positive") Long clientId,
            @Valid @RequestBody ClientUpdateRequestDto clientDto,HttpServletRequest request) {

        log.info("Received request to update client: id={}, name='{}', requestId='{}'",
                clientId, clientDto.getName(), request.getHeader("X-Request-ID"));

        ClientResponseDto updatedClient = service.updateClient(clientId, clientDto);

        ResponseDto response = new ResponseDto(
                ClientsConstants.STATUS_200,
                ClientsConstants.MESSAGE_200_UPDATE,
                updatedClient);

        log.info("Successfully updated client: id={}, name='{}', requestId='{}'",
                updatedClient.getClientId(), updatedClient.getName(), request.getHeader("X-Request-ID"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
            }

    @Operation(summary = "Partially update client",description = "Updates specific fields of an existing client")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Client Partially Updated successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found" ,
                    content =@Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Client already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PatchMapping("/{clientId}")
    public ResponseEntity<ResponseDto> partialUpdateClientDetails(
            @PathVariable("clientId") @Min(value = 1,message = "Client ID must be positive") Long clientId,
            @Valid @RequestBody ClientPartialUpdateRequestDto clientDto,HttpServletRequest request) {

        log.info("Received request to Partially update client: id={}, name='{}', requestId='{}'",
                clientId, clientDto.getName(), request.getHeader("X-Request-ID"));

        ClientResponseDto updatedClient = service.partialUpdateClient(clientId, clientDto);

        ResponseDto response = new ResponseDto(
                ClientsConstants.STATUS_200,
                ClientsConstants.MESSAGE_200_UPDATE,
                updatedClient);

        log.info("Successfully Partially updated client: id={}, name='{}', requestId='{}'",
                updatedClient.getClientId(), updatedClient.getName(), request.getHeader("X-Request-ID"));

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Delete client",description = "Removes a client from the system")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Client Deleted successfully",
                    content = @Content(schema = @Schema(implementation = ResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Client not found" ,
                    content =@Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @DeleteMapping("/{clientId}")
    public ResponseEntity<ResponseDto> deleteClientDetails(
            @PathVariable("clientId") @Min(value = 1,message = "Client ID must be positive")
            Long clientId , HttpServletRequest request) {

        log.info("Received request to delete client: id={}, requestId='{}'",
                clientId, request.getHeader("X-Request-ID"));

        service.deleteClient(clientId);

        ResponseDto response = new ResponseDto(
                ClientsConstants.STATUS_204,
                ClientsConstants.MESSAGE_204,
                null);

        log.info("Successfully deleted client: id={}, requestId='{}'",
                clientId, request.getHeader("X-Request-ID"));

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(response);
    }

}

