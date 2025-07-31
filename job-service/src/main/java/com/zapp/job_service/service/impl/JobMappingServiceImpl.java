package com.zapp.job_service.service.impl;

import com.zapp.job_service.dto.CreateJobRequestDto;
import com.zapp.job_service.dto.JobResponseDto;
import com.zapp.job_service.dto.PartialUpdateJobRequestDto;
import com.zapp.job_service.dto.UpdateJobRequestDto;
import com.zapp.job_service.entity.Job;
import com.zapp.job_service.service.IJobMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class JobMappingServiceImpl implements IJobMappingService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public Job toEntity(CreateJobRequestDto dto) {

        log.debug("Mapping CreateJobRequestDto to Job entity, title: {}, clientId: {}",
                dto.title(), dto.clientId());
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
        // need to set default status
        log.info("Created new Job entity, title: {}, clientId: {}",
                job.getTitle(), job.getClientId());
        return job;
    }

    @Override
    public void updateEntity(Job job, UpdateJobRequestDto dto) {

        log.debug("Updating Job entity, id: {}, title: {}, clientId: {}",
                job.getId(), dto.title(), job.getClientId());
// omit field that are immutable after job creation like clientId
// also omit field that have dedicated work flow like status
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

        log.info("Updated Job entity, id: {}, previous title: {}, new title: {}",
                job.getId(), job.getTitle(),dto.title());

    }

    @Override
    public void partialUpdateEntity(Job job, PartialUpdateJobRequestDto dto) {

        log.debug("Starting partial update for Job entity with id: {}", job.getId());
// omit field that are immutable after job creation like clientId
// also omit field that have dedicated work flow like status
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

        log.info("Partial update completed for Job entity with id: {}", job.getId());
    }

    @Override
    public JobResponseDto toResponseDto(Job job) {

        log.trace("Mapping Job entity to JobResponseDto, id: {}", job.getId());

        return new JobResponseDto(
                job.getId(),
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

        int count = (jobs == null) ? 0 : jobs.size();
        log.trace("Mapping list of {} Job entities to JobResponseDto list", count);
        if (jobs == null) return List.of();
        return jobs.stream()
                .filter(Objects::nonNull)
                .map(this::toResponseDto)
                .toList();
    }
}
