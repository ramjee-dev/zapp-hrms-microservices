package com.zapp.client_service.controller;

import com.zapp.client_service.constants.ClientsConstants;
import com.zapp.client_service.dto.*;
import com.zapp.client_service.entity.Client;
import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.service.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/clients", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Client Management", description = "APIs for managing ZAPP HRMS Clients")
public class ClientController {

    private final IClientService clientService;

    @PostMapping
    // @PreAuthorize("hasRole('ADMIN')") // Uncomment to enable security
    @Operation(summary = "Create a new client", description = "Creates a new client (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully",
                    content = @Content(schema = @Schema(implementation = ClientResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid client data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ClientResponseDto> createClient(
            @Valid @RequestBody CreateClientRequestDto createClientDto) {

        log.info("Received request to create client: companyName='{}', email='{}'",
                createClientDto.companyName(), createClientDto.email());

        ClientResponseDto createdClient = clientService.createClient(createClientDto);

        URI location = URI.create("/api/v1/clients/" + createdClient.id());

        return ResponseEntity.created(location).body(createdClient);
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasAnyRole('ADMIN', 'BD', 'TAT')") // Uncomment for access control
    @Operation(summary = "Get client by ID", description = "Retrieves a specific client by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client found",
                    content = @Content(schema = @Schema(implementation = ClientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ClientResponseDto> getClientById(
            @Parameter(description = "Client ID") @PathVariable("id") UUID clientId) {

        log.debug("Received request to get client with id: {}", clientId);

        ClientResponseDto client = clientService.fetchClientById(clientId);
        return ResponseEntity.ok(client);
    }

    @GetMapping
    // @PreAuthorize("hasAnyRole('ADMIN', 'BD', 'TAT')")
    @Operation(summary = "Get all clients with pagination and filtering",
            description = "Retrieves all clients with support for pagination, sorting, and filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedClientResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<PagedClientResponseDto> getAllClients(
            @Valid @ParameterObject @ModelAttribute ClientPageRequestDto requestDto) {

        log.debug("Received request to get all clients with filters: {}", requestDto);

        PagedClientResponseDto response = clientService.fetchAllClients(requestDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update client", description = "Updates an existing client completely (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully",
                    content = @Content(schema = @Schema(implementation = ClientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Invalid client data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ClientResponseDto> updateClient(
            @Parameter(description = "Client ID") @PathVariable("id") UUID clientId,
            @Valid @RequestBody UpdateClientRequestDto updateClientDto) {

        log.info("Received request to update client with id: {}", clientId);

        ClientResponseDto updatedClient = clientService.updateClient(clientId, updateClientDto);
        return ResponseEntity.ok(updatedClient);
    }

    @PatchMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Partially update client", description = "Updates specific fields of an existing client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client partially updated successfully",
                    content = @Content(schema = @Schema(implementation = ClientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Invalid client data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ClientResponseDto> partiallyUpdateClient(
            @Parameter(description = "Client ID") @PathVariable("id") UUID clientId,
            @Valid @RequestBody PartialUpdateClientRequestDto partialUpdateClientDto) {

        log.info("Received request to partially update client with id: {}", clientId);

        ClientResponseDto updatedClient = clientService.partialUpdateClient(clientId, partialUpdateClientDto);
        return ResponseEntity.ok(updatedClient);
    }

    @PatchMapping("/{id}/status")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Change client status", description = "Changes the status of a client (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client status changed successfully",
                    content = @Content(schema = @Schema(implementation = ClientResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<ClientResponseDto> changeClientStatus(
            @Parameter(description = "Client ID") @PathVariable("id") UUID clientId,
            @Parameter(description = "New client status") @RequestParam ClientStatus status) {

        log.info("Received request to change status of client {} to {}", clientId, status);

        ClientResponseDto updatedClient = clientService.changeClientStatus(clientId, status);
        return ResponseEntity.ok(updatedClient);
    }

    @DeleteMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete client", description = "Deletes a client permanently (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Client deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Client not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> deleteClient(
            @Parameter(description = "Client ID") @PathVariable("id") UUID clientId) {

        log.info("Received request to delete client with id: {}", clientId);

        clientService.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }
}


