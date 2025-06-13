package com.zapp.job_service.controller;

import com.zapp.job_service.dto.JobRequestDTO;
import com.zapp.job_service.dto.JobResponseDTO;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.service.IJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final IJobService jobService;

    // 🔹 Create a job
    @PostMapping
    public ResponseEntity<JobResponseDTO> createJob(@RequestBody @Valid JobRequestDTO dto) {
        JobResponseDTO response = jobService.createJob(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // 🔹 Get all jobs (with optional filters)
    @GetMapping
    public ResponseEntity<List<JobResponseDTO>> getAllJobs(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Job.JobStatus status) {

        List<JobResponseDTO> jobs = jobService.getAllJobs(Optional.ofNullable(clientId), Optional.ofNullable(status));
        return ResponseEntity.ok(jobs);
    }

    // 🔹 Get a job by ID
    @GetMapping("/{id}")
    public ResponseEntity<JobResponseDTO> getJobById(@PathVariable Long id) {
        JobResponseDTO job = jobService.getJobById(id);
        return ResponseEntity.ok(job);
    }

    // 🔹 Update a job
    @PutMapping("/{id}")
    public ResponseEntity<JobResponseDTO> updateJob(
            @PathVariable Long id,
            @RequestBody @Valid JobRequestDTO dto) {

        JobResponseDTO updated = jobService.updateJob(id, dto);
        return ResponseEntity.ok(updated);
    }

    // 🔹 Delete a job
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return ResponseEntity.noContent().build();
    }
}


