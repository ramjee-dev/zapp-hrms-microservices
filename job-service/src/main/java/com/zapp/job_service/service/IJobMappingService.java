package com.zapp.job_service.service;

import com.zapp.job_service.dto.CreateJobRequestDto;
import com.zapp.job_service.dto.JobResponseDto;
import com.zapp.job_service.dto.PartialUpdateJobRequestDto;
import com.zapp.job_service.dto.UpdateJobRequestDto;
import com.zapp.job_service.entity.Job;

import java.util.List;

public interface IJobMappingService {

    Job toEntity(CreateJobRequestDto dto);

    void updateEntity(Job existingJob, UpdateJobRequestDto dto);

    void partialUpdateEntity(Job job, PartialUpdateJobRequestDto dto);

    JobResponseDto toResponseDto(Job job);

    List<JobResponseDto> toResponseDtoList(List<Job> jobs);
}
