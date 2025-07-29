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

        log.info("Created new Job entity, title: {}, clientId: {}",
                job.getTitle(), job.getClientId());

        return job;
    }

    @Override
    public void updateEntity(Job job, UpdateJobRequestDto dto) {

        log.debug("Updating Job entity, id: {}, title: {}, clientId: {}",
                job.getId(), dto.title(), job.getClientId());

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
        log.debug("Performing partial update on Job entity, id: {}", job.getId());

        if (dto.title() != null) {
            log.debug("Updating title from {} to {}",
                    job.getTitle(), dto.title());
            job.setTitle(dto.title());
        }
        if (dto.description() != null) {
            log.debug("Updating description");
            job.setDescription(dto.description());
        }
        if (dto.location() != null) {
            log.debug("Updating location from {} to {}",
                    job.getLocation(), dto.location());
            job.setLocation(dto.location());
        }
        if (dto.department() != null) {
            log.debug("Updating department from {} to {}",
                    job.getDepartment(), dto.department());
            job.setDepartment(dto.department());
        }
        if (dto.priority() != null) {
            log.debug("Updating priority from {} to {}",
                    job.getPriority(), dto.priority());
            job.setPriority(dto.priority());
        }
        if (dto.requiredSkills() != null) {
            log.debug("Updating required skills");
            job.setRequiredSkills(dto.requiredSkills());
        }
        if (dto.experienceRequired() != null) {
            log.debug("Updating experience required from {} to {}",
                    job.getExperienceRequired(), dto.experienceRequired());
            job.setExperienceRequired(dto.experienceRequired());
        }
        if (dto.salaryRange() != null) {
            log.debug("Updating salary range from {} to {}",
                    job.getSalaryRange(), dto.salaryRange());
            job.setSalaryRange(dto.salaryRange());
        }
        if (dto.employmentType() != null) {
            log.debug("Updating employment type from {} to {}",
                    job.getEmploymentType(), dto.employmentType());
            job.setEmploymentType(dto.employmentType());
        }
        if (dto.positionsAvailable() != null) {
            log.debug("Updating positions available from {} to {}",
                    job.getPositionsAvailable(), dto.positionsAvailable());
            job.setPositionsAvailable(dto.positionsAvailable());
        }

        log.info("Partially updated Job entity, id: {}", job.getId());
    }

    @Override
    public JobResponseDto toResponseDto(Job job) {

        log.debug("Mapping Job entity to JobResponseDto, id: {}", job.getId());

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

        log.debug("Converting list of {} Job entities to JobResponseDto list", jobs.size());

        return jobs.stream().map(this::toResponseDto).toList();
    }
}
