package com.zapp.candidate_service.service.impl;

import com.zapp.candidate_service.dto.CreateCandidateRequestDto;
import com.zapp.candidate_service.dto.PartialUpdateCandidateRequestDto;
import com.zapp.candidate_service.dto.UpdateCandidateRequestDto;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.exception.BusinessValidationException;
import com.zapp.candidate_service.exception.ResourceNotFoundException;
import com.zapp.candidate_service.repository.CandidateRepository;
import com.zapp.candidate_service.service.ICandidateValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map;
import java.util.Set;

import static java.util.Map.entry;

@Service
@Slf4j
@RequiredArgsConstructor
public class CandidateValidationServiceImpl implements ICandidateValidationService {

    private final CandidateRepository candidateRepository;
    // Inject jobRepository/service if needed for job existence validation

    @Override
    public void validateCreateRequest(CreateCandidateRequestDto dto) {
        log.debug("Validating create candidate request: email='{}', jobId={}", dto.email(), dto.jobId());

        List<String> errors = new ArrayList<>();

        // Validate email presence & uniqueness
        String email = dto.email() != null ? dto.email().trim() : null;
        if (email == null || email.isBlank()) {
            errors.add("Email is required.");
        } else if (candidateRepository.existsByEmailIgnoreCase(email)) {
            errors.add("Candidate with the given email already exists.");
        }

        // Validate Job existence - uncomment/add before integration
        /*
        if (dto.jobId() == null || !jobRepository.existsById(dto.jobId())) {
            errors.add("Associated job does not exist with id: " + dto.jobId());
        }
        */

        // Validate yearsOfExperience non-negative
        if (dto.yearsOfExperience() != null && dto.yearsOfExperience().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Years of experience must be non-negative.");
        }

        // Additional domain-specific validations (e.g., firstName & lastName required)
        if (dto.firstName() == null || dto.firstName().isBlank()) {
            errors.add("First name is required.");
        }
        if (dto.lastName() == null || dto.lastName().isBlank()) {
            errors.add("Last name is required.");
        }

        if (!errors.isEmpty()) {
            log.warn("Candidate create validation failed: {}", errors);
            throw new BusinessValidationException("Validation failed for candidate creation.", errors);
        }
        log.info("Candidate create request validation succeeded for email='{}'", email);
    }

    @Override
    public void validateUpdateRequest(UUID candidateId, UpdateCandidateRequestDto dto) {
        log.debug("Validating update for candidate '{}'", candidateId);

        List<String> errors = new ArrayList<>();

        // Validate candidate existence
        if (!candidateRepository.existsById(candidateId)) {
            log.warn("Candidate not found for update, id={}", candidateId);
            throw new ResourceNotFoundException("Candidate", "id", candidateId.toString());
        }

        // Validate email uniqueness (for update, exclude current candidate)
        if (dto.email() != null) {
            String email = dto.email().trim();
            var existing = candidateRepository.findByEmailIgnoreCase(email);
            if (existing.isPresent() && !existing.get().getId().equals(candidateId)) {
                errors.add("Email is already used by another candidate.");
            }
        }

        // Years of experience non-negative
        if (dto.yearsOfExperience() != null && dto.yearsOfExperience().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Years of experience must be non-negative.");
        }

        // Additional validations (e.g., mandatory fields) if needed

        if (!errors.isEmpty()) {
            log.warn("Candidate update validation failed for id={}: {}", candidateId, errors);
            throw new BusinessValidationException("Validation failed for candidate update.", errors);
        }
        log.info("Candidate update validation succeeded for id={}", candidateId);
    }

    @Override
    public void validatePartialUpdateRequest(UUID candidateId, PartialUpdateCandidateRequestDto dto) {
        log.debug("Validating partial update for candidate '{}'", candidateId);

        List<String> errors = new ArrayList<>();

        // Validate candidate existence
        if (!candidateRepository.existsById(candidateId)) {
            log.warn("Candidate not found for partial update, id={}", candidateId);
            throw new ResourceNotFoundException("Candidate", "id", candidateId.toString());
        }

        // Validate email uniqueness if email is present
        if (dto.email() != null) {
            String email = dto.email().trim();
            var existing = candidateRepository.findByEmailIgnoreCase(email);
            if (existing.isPresent() && !existing.get().getId().equals(candidateId)) {
                errors.add("Email is already used by another candidate.");
            }
        }

        // Years of experience validation
        if (dto.yearsOfExperience() != null && dto.yearsOfExperience().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("Years of experience must be non-negative.");
        }

        if (!errors.isEmpty()) {
            log.warn("Candidate partial update validation failed for id={}: {}", candidateId, errors);
            throw new BusinessValidationException("Validation failed for candidate partial update.", errors);
        }
        log.info("Candidate partial update validation succeeded for id={}", candidateId);
    }

    @Override
    public void validateStatusTransition(Candidate candidate, CandidateStatus newStatus) {
        log.debug("Validating status transition for candidate='{}': {} -> {}", candidate.getId(), candidate.getStatus(), newStatus);

        Map<CandidateStatus, Set<CandidateStatus>> validTransitions = Map.ofEntries(
                entry(CandidateStatus.APPLIED, Set.of(CandidateStatus.SCREENING, CandidateStatus.REJECTED, CandidateStatus.WITHDRAWN, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.SCREENING, Set.of(CandidateStatus.INTERVIEW_SCHEDULED, CandidateStatus.REJECTED, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.INTERVIEW_SCHEDULED, Set.of(CandidateStatus.INTERVIEW_CONDUCTED, CandidateStatus.REJECTED, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.INTERVIEW_CONDUCTED, Set.of(CandidateStatus.TECHNICAL_ASSESSMENT, CandidateStatus.SECOND_INTERVIEW, CandidateStatus.REFERENCE_CHECK, CandidateStatus.REJECTED, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.TECHNICAL_ASSESSMENT, Set.of(CandidateStatus.OFFER_EXTENDED, CandidateStatus.REJECTED, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.SECOND_INTERVIEW, Set.of(CandidateStatus.OFFER_EXTENDED, CandidateStatus.REJECTED, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.REFERENCE_CHECK, Set.of(CandidateStatus.OFFER_EXTENDED, CandidateStatus.REJECTED, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.BACKGROUND_CHECK, Set.of(CandidateStatus.OFFER_EXTENDED, CandidateStatus.REJECTED, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.OFFER_EXTENDED, Set.of(CandidateStatus.OFFER_ACCEPTED, CandidateStatus.OFFER_DECLINED, CandidateStatus.REJECTED, CandidateStatus.ON_HOLD)),
                entry(CandidateStatus.OFFER_ACCEPTED, Set.of(CandidateStatus.SELECTED)),
                entry(CandidateStatus.SELECTED, Set.of()),
                entry(CandidateStatus.REJECTED, Set.of()),
                entry(CandidateStatus.WITHDRAWN, Set.of()),
                entry(CandidateStatus.ON_HOLD, Set.of()),
                entry(CandidateStatus.OFFER_DECLINED, Set.of())
        );


        CandidateStatus current = candidate.getStatus();
        Set<CandidateStatus> allowedNextStatuses = validTransitions.getOrDefault(current, Set.of());

        if (!allowedNextStatuses.contains(newStatus)) {
            String message = String.format(
                    "Invalid status transition from %s to %s",
                    current, newStatus
            );
            log.warn("Candidate [{}] - {}", candidate.getId(), message);
            throw new BusinessValidationException(
                    message,
                    List.of("Status transition not allowed: " + current + " -> " + newStatus)
            );
        }

        log.info("Candidate '{}' status transition from {} to {} validated.", candidate.getId(), current, newStatus);
    }

    @Override
    public void validateDeletion(Candidate candidate) {
        log.debug("Validating deletion for candidate '{}'", candidate.getId());

        List<String> errors = new ArrayList<>();

        // Business rules preventing deletion of candidates in important statuses
        if (EnumSet.of(CandidateStatus.SELECTED, CandidateStatus.OFFER_ACCEPTED).contains(candidate.getStatus())) {
            errors.add("Cannot delete a candidate who has been selected or accepted an offer.");
        }

        // TODO: Add checks for related entities / audit logs if necessary

        if (!errors.isEmpty()) {
            log.warn("Candidate deletion validation failed for id={}: {}", candidate.getId(), errors);
            throw new BusinessValidationException("Validation failed for candidate deletion.", errors);
        }
        log.info("Candidate deletion validation succeeded for id={}", candidate.getId());
    }
}
