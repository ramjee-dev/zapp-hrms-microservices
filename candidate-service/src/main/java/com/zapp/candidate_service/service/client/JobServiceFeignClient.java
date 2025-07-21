package com.zapp.candidate_service.service.client;

import com.zapp.candidate_service.dto.JobDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "job-service")
public interface JobServiceFeignClient {

    @GetMapping(value = "/fetch/{jobId}",consumes = "application/json")
    ResponseEntity<JobDto> getJobById(@PathVariable("jobId") Long jobId);
}
