package com.zapp.client_service.controller;

import com.ramjee.common.dto.ErrorResponseDto;
import com.zapp.client_service.constants.ClientsConstants;
import  com.zapp.client_service.dto.ClientDto;
import com.zapp.client_service.dto.ResponseDto;
import com.zapp.client_service.service.IClientService;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "CRUD REST APIs for Clients in EazyBank",
        description = "CRUD REST APIs in ZappHrms to CREATE , UPDATE , FETCH , DELETE Client Details")
public class ClientController {

    private final IClientService service;

    @Operation(
            summary = "Create Client REST API",
            description = "REST API to create new Client inside ZappHrms"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createClient(@Valid @RequestBody ClientDto dto) {
        service.createClient(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ClientsConstants.STATUS_201,ClientsConstants.MESSAGE_201));
    }

    @Retry(name = "getAllClients",fallbackMethod = "getAllClientsFallback")
    @GetMapping
    public Page<ClientDto> getAll(Pageable pageable) {
        log.debug("getALLClients API invoked");
        return service.getAllClients(pageable);
    }

    public Page<ClientDto> getAllClientsFallback(Pageable pageable, Throwable throwable) {
        log.error("Fallback triggered for getAllClients due to: {}", throwable.getMessage());

        List<ClientDto> fallbackList = Collections.emptyList(); // or pre-defined backup list
        return new PageImpl<>(fallbackList, pageable, 0);
    }

    @Operation(
            summary = "Fetch Client Details REST API",
            description = "REST API to fetch Client details based on given ClientId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @GetMapping("/fetch/{clientId}")
    public ResponseEntity<ClientDto> fetchClientDetails(@PathVariable Long clientId) {
        ClientDto clientDto = service.fetchClientById(clientId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientDto);
    }

    @Operation(
            summary = "Update Client details REST API",
            description = "REST API to update Client details based on given ClientId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "HTTP Status Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDto> updateClientDetails(@RequestParam Long clientId,
                                              @Valid @RequestBody ClientDto clientDto) {
        boolean isUpdated = service.updateClient(clientId, clientDto);
        if(isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ClientsConstants.STATUS_200,ClientsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ClientsConstants.STATUS_417,ClientsConstants.MESSAGE_417_UPDATE));
        }
    }

    @Operation(
            summary = "Delete Client details REST API",
            description = "REST API to Delete Client details based on given ClientId"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Http status Expectation Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteClientDetails(@RequestParam Long clientId) {
        boolean isDeleted = service.deleteClient(clientId);
        if(isDeleted){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ClientsConstants.STATUS_200,ClientsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ClientsConstants.STATUS_417,ClientsConstants.MESSAGE_417_DELETE));
        }
    }
}

