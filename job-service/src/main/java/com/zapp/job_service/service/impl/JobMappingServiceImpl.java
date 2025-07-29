package com.zapp.job_service.service.impl;

import com.zapp.job_service.dto.CreateJobDto;
import com.zapp.job_service.dto.JobResponseDto;
import com.zapp.job_service.dto.PartialUpdateJobDto;
import com.zapp.job_service.dto.UpdateJobDto;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.service.IJobMappingService;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class JobMappingServiceImpl implements IJobMappingService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public Job toEntity(CreateJobDto dto) {
        Job job = new Job();
        job.setTitle(dto.title());
        job.setDescription(dto.description());
        job.setClientId(dto.clientId());
        job.setLocation(dto.location());
        job.setDepartment(dto.department());
        job.setPriority(dto.priority());
        job.setRequiredSkills(dto.requiredSkills());
        job.setExperienceRequired(dto.experienceRequired());
        job.setSalaryRange(dto.salaryRange());
        job.setEmploymentType(dto.employmentType());
        job.setPositionsAvailable(dto.positionsAvailable());
        return job;
    }

    @Override
    public void updateEntity(Job job, UpdateJobDto dto) {
        job.setTitle(dto.title());
        job.setDescription(dto.description());
        job.setLocation(dto.location());
        job.setDepartment(dto.department());
        job.setPriority(dto.priority());
        job.setRequiredSkills(dto.requiredSkills());
        job.setExperienceRequired(dto.experienceRequired());
        job.setSalaryRange(dto.salaryRange());
        job.setEmploymentType(dto.employmentType());
        job.setPositionsAvailable(dto.positionsAvailable());
    }

    @Override
    public void partialUpdateEntity(Job job, PartialUpdateJobDto dto) {

        if (dto.title() != null) job.setTitle(dto.title());
        if (dto.description() != null) job.setDescription(dto.description());
        if (dto.location() != null) job.setLocation(dto.location());
        if (dto.department() != null) job.setDepartment(dto.department());
        if (dto.priority() != null) job.setPriority(dto.priority());
        if (dto.requiredSkills() != null) job.setRequiredSkills(dto.requiredSkills());
        if (dto.experienceRequired() != null) job.setExperienceRequired(dto.experienceRequired());
        if (dto.salaryRange() != null) job.setSalaryRange(dto.salaryRange());
        if (dto.employmentType() != null) job.setEmploymentType(dto.employmentType());
        if (dto.positionsAvailable() != null) job.setPositionsAvailable(dto.positionsAvailable());
    }

    @Override
    public JobResponseDto toResponseDto(Job job) {
        return new JobResponseDto(
                job.getJobId(),
                job.getTitle(),
                job.getDescription(),
                job.getClientId(),
                job.getLocation(),
                job.getDepartment(),
                job.getStatus(),
                job.getPriority(),
                job.getRequiredSkills(),
                job.getExperienceRequired(),
                job.getSalaryRange(),
                job.getEmploymentType(),
                job.getPositionsAvailable(),
                job.getCreatedAt() != null ? FORMATTER.format(job.getCreatedAt()) : null,
                job.getUpdatedAt() != null ? FORMATTER.format(job.getUpdatedAt()) : null,
                job.getCreatedBy(),
                job.getUpdatedBy()
        );
    }

    @Override
    public List<JobResponseDto> toResponseDtoList(List<Job> jobs) {
        return jobs.stream().map(this::toResponseDto).toList();
    }
}
