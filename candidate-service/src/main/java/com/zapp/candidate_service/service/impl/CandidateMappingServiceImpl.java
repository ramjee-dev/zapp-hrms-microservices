package com.zapp.candidate_service.service.impl;

import com.zapp.candidate_service.dto.CandidateResponseDto;
import com.zapp.candidate_service.dto.CreateCandidateRequestDto;
import com.zapp.candidate_service.dto.PartialUpdateCandidateRequestDto;
import com.zapp.candidate_service.dto.UpdateCandidateRequestDto;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.service.ICandidateMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CandidateMappingServiceImpl implements ICandidateMappingService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public Candidate toEntity(CreateCandidateRequestDto dto) {

        log.debug("Mapping CreateCandidateRequestDto to new Candidate entity for email={}", dto.email());

        Candidate candidate = new Candidate();
        candidate.setFirstName(dto.firstName());
        candidate.setLastName(dto.lastName());
        candidate.setEmail(dto.email().trim());
        candidate.setPhone(dto.phone());
        candidate.setAddress(dto.address());
        candidate.setCity(dto.city());
        candidate.setState(dto.state());
        candidate.setPostalCode(dto.postalCode());
        candidate.setCountry(dto.country());
        candidate.setJobId(dto.jobId());
        candidate.setExperienceLevel(dto.experienceLevel());
        candidate.setYearsOfExperience(dto.yearsOfExperience());
        candidate.setSkills(dto.skills());
        candidate.setCurrentPosition(dto.currentPosition());
        candidate.setCurrentCompany(dto.currentCompany());
        candidate.setCurrentSalary(dto.currentSalary());
        candidate.setExpectedSalary(dto.expectedSalary());
        candidate.setNoticePeriod(dto.noticePeriod());
        candidate.setResumeUrl(dto.resumeUrl());
        candidate.setLinkedinProfile(dto.linkedinProfile());
        candidate.setPortfolioUrl(dto.portfolioUrl());
        candidate.setNotes(dto.notes());
        candidate.setStatus(CandidateStatus.APPLIED); // Set to initial status

        log.info("Created new Candidate entity, email : {}, jobId: {}",
                dto.email(), dto.jobId());

        return candidate;
    }

    @Override
    public void updateEntity(Candidate entity, UpdateCandidateRequestDto dto) {

        log.debug("Updating Candidate entity (id={}) from UpdateCandidateRequestDto (email={})",
                entity.getId(), dto.email());

        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setEmail(dto.email().trim());
        entity.setPhone(dto.phone());
        entity.setAddress(dto.address());
        entity.setCity(dto.city());
        entity.setState(dto.state());
        entity.setPostalCode(dto.postalCode());
        entity.setCountry(dto.country());
        // jobId is immutable after creation; omit here.
        entity.setExperienceLevel(dto.experienceLevel());
        entity.setYearsOfExperience(dto.yearsOfExperience());
        entity.setSkills(dto.skills());
        entity.setCurrentPosition(dto.currentPosition());
        entity.setCurrentCompany(dto.currentCompany());
        entity.setCurrentSalary(dto.currentSalary());
        entity.setExpectedSalary(dto.expectedSalary());
        entity.setNoticePeriod(dto.noticePeriod());
        entity.setResumeUrl(dto.resumeUrl());
        entity.setLinkedinProfile(dto.linkedinProfile());
        entity.setPortfolioUrl(dto.portfolioUrl());
        entity.setNotes(dto.notes());
        // Do not update status or assignedTo here—those are managed by dedicated flows.

        log.info("Completed update on Candidate entity with id: {}", entity.getId());
    }

    @Override
    public void partialUpdateEntity(Candidate entity, PartialUpdateCandidateRequestDto dto) {

        log.debug("Partially updating Candidate entity (id={})", entity.getId());

        // Only update present (non-null) fields.
        if (dto.firstName() != null) entity.setFirstName(dto.firstName());
        if (dto.lastName() != null) entity.setLastName(dto.lastName());
        if (dto.email() != null) entity.setEmail(dto.email().trim());
        if (dto.phone() != null) entity.setPhone(dto.phone());
        if (dto.address() != null) entity.setAddress(dto.address());
        if (dto.city() != null) entity.setCity(dto.city());
        if (dto.state() != null) entity.setState(dto.state());
        if (dto.postalCode() != null) entity.setPostalCode(dto.postalCode());
        if (dto.country() != null) entity.setCountry(dto.country());
        // jobId is immutable after creation; omit here.
        if (dto.experienceLevel() != null) entity.setExperienceLevel(dto.experienceLevel());
        if (dto.yearsOfExperience() != null) entity.setYearsOfExperience(dto.yearsOfExperience());
        if (dto.skills() != null) entity.setSkills(dto.skills());
        if (dto.currentPosition() != null) entity.setCurrentPosition(dto.currentPosition());
        if (dto.currentCompany() != null) entity.setCurrentCompany(dto.currentCompany());
        if (dto.currentSalary() != null) entity.setCurrentSalary(dto.currentSalary());
        if (dto.expectedSalary() != null) entity.setExpectedSalary(dto.expectedSalary());
        if (dto.noticePeriod() != null) entity.setNoticePeriod(dto.noticePeriod());
        if (dto.resumeUrl() != null) entity.setResumeUrl(dto.resumeUrl());
        if (dto.linkedinProfile() != null) entity.setLinkedinProfile(dto.linkedinProfile());
        if (dto.portfolioUrl() != null) entity.setPortfolioUrl(dto.portfolioUrl());
        if (dto.notes() != null) entity.setNotes(dto.notes());
        if (dto.assignedTo() != null) entity.setAssignedTo(dto.assignedTo());
        // Do NOT update status field here (should use changeStatus).

        log.info("Completed partial update on Candidate entity with id: {}", entity.getId());
    }

    @Override
    public CandidateResponseDto toResponseDto(Candidate entity) {

        log.trace("Mapping Candidate entity (id={}) to CandidateResponseDto", entity.getId());

        return new CandidateResponseDto(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getAddress(),
                entity.getCity(),
                entity.getState(),
                entity.getPostalCode(),
                entity.getCountry(),
                entity.getJobId(),
                entity.getStatus(),
                entity.getExperienceLevel(),
                entity.getYearsOfExperience(),
                entity.getSkills(),
                entity.getCurrentPosition(),
                entity.getCurrentCompany(),
                entity.getCurrentSalary(),
                entity.getExpectedSalary(),
                entity.getNoticePeriod(),
                entity.getResumeUrl(),
                entity.getLinkedinProfile(),
                entity.getPortfolioUrl(),
                entity.getNotes(),
                entity.getAssignedTo(),
                // Audit fields: ISO-8601 UTC format or null
                entity.getCreatedAt() != null ? FORMATTER.format(entity.getCreatedAt()) : null,
                entity.getUpdatedAt() != null ? FORMATTER.format(entity.getUpdatedAt()) : null,
                entity.getCreatedBy(),
                entity.getUpdatedBy()
        );
    }

    @Override
    public List<CandidateResponseDto> toResponseDtoList(List<Candidate> entities) {

        log.trace("Mapping list of {} Candidate entities to CandidateResponseDtos", entities != null ? entities.size() : 0);

        if (entities == null) return List.of();

        return entities.stream().filter(Objects::nonNull).map(this::toResponseDto).toList();
    }
}
