package com.zapp.candidate_service.controller;


import com.zapp.candidate_service.Constants.CandidatesConstants;
import com.zapp.candidate_service.dto.CandidateDto;
import com.zapp.candidate_service.dto.CandidateFilter;
import com.zapp.candidate_service.dto.ErrorResponseDto;
import com.zapp.candidate_service.dto.ResponseDto;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.service.ICandidateService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "CRUD REST APIs for Candidates in Zapp HRMS",
        description ="CRUD REST APIs in Zapp HRMS to CREATE , UPDATE , FETCH , DELETE Candidate Details" )
public class CandidateController {

    private final ICandidateService candidateService;

    @Operation(
            summary = "Add Candidate To Job  REST API",
            description = "REST API to add Candidate to a Job inside ZappHrms"
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
    @PostMapping("/create/{jobId}")
    public ResponseEntity<ResponseDto> addCandidate(@PathVariable("jobId") Long jobId, @Valid @RequestBody CandidateDto dto) {
        candidateService.addCandidateToJob(jobId,dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(CandidatesConstants.STATUS_201,CandidatesConstants.MESSAGE_201));
    }

    @GetMapping("/candidates")
    public ResponseEntity<Page<Candidate>> listCandidates(Pageable pageable, CandidateFilter filter) {
        return ResponseEntity.ok(candidateService.getAllCandidates(pageable, filter));
    }

    @Operation(
            summary = "Fetch Candidate Details REST API",
            description = "REST API to fetch Candidate details based on given CandidateId"
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
    @RateLimiter(name = "getCandidateById",fallbackMethod = "getCandidateByIdFallback")
    @GetMapping("/fetch/{candidateId}")
    public ResponseEntity<CandidateDto> getById(@PathVariable("candidateId") Long candidateId) {
        CandidateDto candidateDto = candidateService.getCandidateById(candidateId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(candidateDto);
    }

    public ResponseEntity<Candidate> getCandidateByIdFallback(Long id, Throwable throwable) {
        log.error("Rate limiter fallback triggered for getCandidateById. Reason: {}", throwable.getMessage());

        // Return a safe fallback (e.g., 429 Too Many Requests)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(null); // or a placeholder Candidate if needed
    }


    @Operation(
            summary = "Update candidate details REST API",
            description = "Replaces candidate's personal information. Does NOT change status."
    )
    @PutMapping("/update/{candidateId}")
    public ResponseEntity<Candidate> update(@PathVariable("candidateId") Long id,@Valid @RequestBody CandidateDto dto) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, dto));
    }

    @Operation(
            summary = "Change candidate status REST API",
            description = "Transitions candidate to next pipeline stage and triggers notification event."
    )
    @PatchMapping("/update/status/{candidateId}")
    public ResponseEntity<Candidate> updateStatus(@PathVariable("candidateId") Long id, @RequestParam Candidate.Status status) {
        return ResponseEntity.ok(candidateService.updateCandidateStatus(id, status));
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}
