package com.zapp.candidate_service.controller;


import com.zapp.candidate_service.Constants.CandidatesConstants;
import com.zapp.candidate_service.dto.*;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.service.ICandidateService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/candidates", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Candidate Management", description = "APIs for managing candidates in the Zapp HRMS system")
public class CandidateController {

    private final ICandidateService candidateService;

    @PostMapping
    @Operation(summary = "Create a new candidate", description = "Creates a new candidate and associates with a job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Candidate created successfully",
                    content = @Content(schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid candidate data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    public ResponseEntity<CandidateResponseDto> createCandidate(@Valid @RequestBody CreateCandidateRequestDto createCandidateDto) {
        log.info("Received request to create candidate with email: {}", createCandidateDto.email());

        CandidateResponseDto createdCandidate = candidateService.createCandidate(createCandidateDto);

        URI location = URI.create("/api/v1/candidates/" + createdCandidate.id());
        return ResponseEntity.created(location).body(createdCandidate);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get candidate by ID", description = "Retrieves candidate details by candidate ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate found",
                    content = @Content(schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    @RateLimiter(name = "getCandidateById", fallbackMethod = "getCandidateByIdFallback")
    public ResponseEntity<CandidateResponseDto> getCandidateById(@Parameter(description = "Candidate ID") @PathVariable("id") UUID candidateId) {
        log.debug("Received request to get candidate with id: {}", candidateId);

        CandidateResponseDto candidate = candidateService.fetchCandidateById(candidateId);
        return ResponseEntity.ok(candidate);
    }

    public ResponseEntity<CandidateResponseDto> getCandidateByIdFallback(UUID candidateId, Throwable throwable) {
        log.error("Rate limiter fallback triggered for getCandidateById with id: {}. Reason: {}", candidateId, throwable.getMessage());

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
    }

    @GetMapping
    @Operation(summary = "Get all candidates with pagination and filtering", description = "Retrieves candidates with support for pagination, sorting, and filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidates retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedCandidateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid filter parameters",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    public ResponseEntity<PagedCandidateResponseDto> getAllCandidates(
            @Valid @ParameterObject @ModelAttribute CandidatePageRequestDto pageRequestDto) {

        log.debug("Received request to get all candidates with filters: {}", pageRequestDto);

        PagedCandidateResponseDto candidates = candidateService.fetchAllCandidates(pageRequestDto);
        return ResponseEntity.ok(candidates);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a candidate", description = "Fully updates candidate information. Does NOT change status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate updated successfully",
                    content = @Content(schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid candidate data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    public ResponseEntity<CandidateResponseDto> updateCandidate(
            @Parameter(description = "Candidate ID") @PathVariable("id") UUID candidateId,
            @Valid @RequestBody UpdateCandidateRequestDto updateCandidateDto) {

        log.info("Received request to update candidate with id: {}", candidateId);

        CandidateResponseDto updatedCandidate = candidateService.updateCandidate(candidateId, updateCandidateDto);
        return ResponseEntity.ok(updatedCandidate);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a candidate", description = "Updates specific candidate fields. Does NOT change status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate partially updated successfully",
                    content = @Content(schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid candidate data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    public ResponseEntity<CandidateResponseDto> partialUpdateCandidate(
            @Parameter(description = "Candidate ID") @PathVariable("id") UUID candidateId,
            @Valid @RequestBody PartialUpdateCandidateRequestDto partialUpdateCandidateDto) {

        log.info("Received request to partially update candidate with id: {}", candidateId);

        CandidateResponseDto updatedCandidate = candidateService.partialUpdateCandidate(candidateId, partialUpdateCandidateDto);
        return ResponseEntity.ok(updatedCandidate);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Change candidate status", description = "Changes the status of a candidate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidate status changed successfully",
                    content = @Content(schema = @Schema(implementation = CandidateResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "404", description = "Candidate not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    public ResponseEntity<CandidateResponseDto> changeCandidateStatus(
            @Parameter(description = "Candidate ID") @PathVariable("id") UUID candidateId,
            @Parameter(description = "New candidate status") @RequestParam CandidateStatus newStatus) {

        log.info("Received request to change status of candidate {} to {}", candidateId, newStatus);

        CandidateResponseDto updatedCandidate = candidateService.changeCandidateStatus(candidateId, newStatus);
        return ResponseEntity.ok(updatedCandidate);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete candidate", description = "Deletes a candidate permanently")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Candidate deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Candidate not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
    })
    public ResponseEntity<Void> deleteCandidate(@Parameter(description = "Candidate ID") @PathVariable("id") UUID candidateId) {
        log.info("Received request to delete candidate with id: {}", candidateId);

        candidateService.deleteCandidate(candidateId);
        return ResponseEntity.noContent().build();
    }
}

