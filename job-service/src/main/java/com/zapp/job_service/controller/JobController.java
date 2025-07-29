package com.zapp.job_service.controller;

import com.zapp.job_service.dto.*;
import com.zapp.job_service.enums.JobStatus;
import com.zapp.job_service.service.IJobService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/jobs",produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Job Management",description = "APIs for managing jobs in the HRMS system")
public class JobController {

    private final IJobService jobService;

    @PostMapping
    @Operation(summary = "Create a new job", description = "Creates a new job posting for a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Job created successfully",
                    content = @Content(schema = @Schema(implementation = JobResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid job data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<JobResponseDto> createJob(
            @Valid @RequestBody CreateJobRequestDto createJobRequestDto) {

        log.info("Received request to create job for client: {}", createJobRequestDto.clientId());

        JobResponseDto createdJob = jobService.createJob(createJobRequestDto);

        URI location = URI.create("/api/v1/jobs/" + createdJob.id());

        return ResponseEntity.created(location).body(createdJob);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", description = "Retrieves a specific job by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job found",
                    content = @Content(schema = @Schema(implementation = JobResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Job not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<JobResponseDto> getJobById(
            @Parameter(description = "Job ID") @PathVariable("id") UUID jobId) {

        log.debug("Received request to get job with id: {}", jobId);

        JobResponseDto job = jobService.fetchJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @Operation(summary = "Get all jobs with pagination and filtering",
            description = "Retrieves all jobs with support for pagination, sorting, and filtering")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Jobs retrieved successfully",
                    content = @Content(schema = @Schema(implementation = PagedJobResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping
    public ResponseEntity<PagedJobResponseDto> getAllJobs(@Valid @ParameterObject
                                                              @ModelAttribute JobPageRequestDto requestDto) {

        log.debug("Received request to get all jobs with pagination, requestDto={}", requestDto);

        PagedJobResponseDto jobs = jobService.fetchAllJobs(requestDto);
        return ResponseEntity.ok(jobs);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update job", description = "Updates an existing job completely")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job updated successfully",
                    content = @Content(schema = @Schema(implementation = JobResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Job not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Invalid job data",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<JobResponseDto> updateJob(
            @Parameter(description = "Job ID") @PathVariable("id") UUID jobId,
            @Valid @RequestBody UpdateJobRequestDto updateJobRequestDto) {

        log.info("Received request to update job with id: {}", jobId);

        JobResponseDto updatedJob = jobService.updateJob(jobId, updateJobRequestDto);
        return ResponseEntity.ok(updatedJob);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update job", description = "Updates specific fields of an existing job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job Partially updated successfully",
                    content = @Content(schema = @Schema(implementation = JobResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Job not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<JobResponseDto> partialUpdateJob(
            @Parameter(description = "Job ID") @PathVariable UUID id,
            @Valid @RequestBody PartialUpdateJobRequestDto partialUpdateJobRequestDto) {

        log.info("Received request to partially update job with id: {}", id);

        JobResponseDto updatedJob = jobService.partialUpdateJob(id, partialUpdateJobRequestDto);
        return ResponseEntity.ok(updatedJob);
    }

    @Operation(summary = "Change job status", description = "Changes the status of a job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Job status changed successfully",
                    content = @Content(schema = @Schema(implementation = JobResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Job not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "400", description = "Invalid status transition",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))

    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<JobResponseDto> changeJobStatus(
            @Parameter(description = "Job ID") @PathVariable UUID jobId,
            @Parameter(description = "New job status") @RequestParam JobStatus status) {

        log.info("Received request to change status of job {} to {}", jobId, status);

        JobResponseDto updatedJob = jobService.changeJobStatus(jobId, status);
        return ResponseEntity.ok(updatedJob);

    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job", description = "Deletes a job permanently")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Job deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Job not found",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(schema = @Schema(implementation = ProblemDetail.class)))
    })
    public ResponseEntity<Void> deleteJob(
            @Parameter(description = "Job ID") @PathVariable("id") UUID clientId) {

        log.info("Received request to delete job with id: {}", clientId);

        jobService.deleteJob(clientId);
        return ResponseEntity.noContent().build();
    }
}
