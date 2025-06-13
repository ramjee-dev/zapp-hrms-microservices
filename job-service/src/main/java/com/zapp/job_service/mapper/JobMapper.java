package com.zapp.job_service.mapper;

import com.zapp.job_service.dto.JobRequestDTO;
import com.zapp.job_service.dto.JobResponseDTO;
import com.zapp.job_service.entity.Job;
import org.springframework.stereotype.Component;

@Component
public class JobMapper {

    public Job toEntity(JobRequestDTO dto) {
        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setStatus(dto.getStatus() != null ? dto.getStatus() : Job.JobStatus.OPEN);
        job.setClientId(dto.getClientId());
        return job;
    }

    public JobResponseDTO toDTO(Job job) {
        JobResponseDTO dto = new JobResponseDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        dto.setStatus(job.getStatus());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setClientId(job.getClientId());
        return dto;
    }

    public void updateJobFromDto(Job job, JobRequestDTO dto) {
        if (dto.getTitle() != null) {
            job.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            job.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            job.setStatus(dto.getStatus());
        }
        if (dto.getClientId() != null) {
            job.setClientId(dto.getClientId());
        }
    }
}


