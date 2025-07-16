package com.zapp.job_service.controller;

import com.zapp.job_service.constants.JobsConstants;
import com.zapp.job_service.dto.JobDto;
import com.zapp.job_service.dto.ResponseDto;
import com.zapp.job_service.service.IJobService;
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
public class JobController {

    private final IJobService jobService;


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

    // 🔹 Get a job by ID
    @GetMapping("/fetch/{jobId}")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long jobId) {
        JobDto jobDto = jobService.fetchJobById(jobId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(jobDto);
    }

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
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDto(JobsConstants.STATUS_500,JobsConstants.MESSAGE_500));
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


