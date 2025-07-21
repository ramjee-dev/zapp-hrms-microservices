package com.zapp.job_service.controller;

import com.zapp.job_service.constants.JobsConstants;
import com.zapp.job_service.dto.ErrorResponseDto;
import com.zapp.job_service.dto.JobDto;
import com.zapp.job_service.dto.ResponseDto;
import com.zapp.job_service.service.IJobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Validated
@Tag(name = "CRUD REST APIs for Jobs in ZappHRMS",
        description = "CRUD REST APIs in ZappHRMS to CREATE , UPDATE , FETCH , DELETE Job Details")
public class JobController {

    private final IJobService jobService;

    @Operation(
            summary = "Create Job REST API",
            description = "REST API to create new Job inside ZappHrms"
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
    public ResponseEntity<ResponseDto> createJob(@Valid @RequestBody JobDto dto) {
        jobService.createJob(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(JobsConstants.STATUS_201,JobsConstants.MESSAGE_201));
    }

//    // 🔹 Get all jobs (with optional filters)
//    @GetMapping
//    public ResponseEntity<List<JobResponseDTO>> getAllJobs(
//            @RequestParam(required = false) Long clientId,
//            @RequestParam(required = false) Job.JobStatus status) {
//
//        List<JobResponseDTO> jobs = jobService.getAllJobs(Optional.ofNullable(clientId), Optional.ofNullable(status));
//        return ResponseEntity.ok(jobs);
//    }

    @Operation(
            summary = "Fetch Job Details REST API",
            description = "REST API to fetch Job details based on given JobId"
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
    @GetMapping("/fetch/{jobId}")
    public ResponseEntity<JobDto> getJobById(@PathVariable("jobId") Long jobId) {
        JobDto jobDto = jobService.fetchJobById(jobId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobDto);
    }

    @Operation(
            summary = "Update Job details REST API",
            description = "REST API to update Job details based on given JobId"
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
    public ResponseEntity<ResponseDto> updateJobDetails(
            @RequestParam Long jobId,
            @Valid @RequestBody  JobDto jobDto) {
        boolean isUpdated = jobService.updateJob(jobId, jobDto);
        if(isUpdated){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(JobsConstants.STATUS_200,JobsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(JobsConstants.STATUS_417,JobsConstants.MESSAGE_417_UPDATE));
        }
    }

    // 🔹 Delete a job
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDto> deleteJobDetails(@RequestParam Long jobId) {
        boolean isDeleted = jobService.deleteJob(jobId);
        if(isDeleted){
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(JobsConstants.STATUS_200,JobsConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(JobsConstants.STATUS_500,JobsConstants.MESSAGE_500));
        }
    }
}


