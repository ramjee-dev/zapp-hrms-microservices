package com.zapp.job_service.service;

import com.zapp.job_service.dto.CreateJobDto;
import com.zapp.job_service.dto.JobResponseDto;
import com.zapp.job_service.dto.PartialUpdateJobDto;
import com.zapp.job_service.dto.UpdateJobDto;
import com.zapp.job_service.entity.Job;

import java.util.List;

public interface IJobMappingService {

    Job toEntity(CreateJobDto dto);

    void updateEntity(Job job, UpdateJobDto dto);

    void partialUpdateEntity(Job job, PartialUpdateJobDto dto);

    JobResponseDto toResponseDto(Job job);

    List<JobResponseDto> toResponseDtoList(List<Job> jobs);
}
