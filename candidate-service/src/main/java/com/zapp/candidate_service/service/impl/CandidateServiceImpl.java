package com.zapp.candidate_service.service.impl;

import com.zapp.candidate_service.dto.*;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.exception.ResourceNotFoundException;
import com.zapp.candidate_service.repository.CandidateRepository;
import com.zapp.candidate_service.service.ICandidateMappingService;
import com.zapp.candidate_service.service.ICandidateService;
import com.zapp.candidate_service.service.ICandidateValidationService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor@Slf4j
@Transactional(readOnly = true)
public class CandidateServiceImpl implements ICandidateService {

    private final CandidateRepository candidateRepository;
    private final ICandidateValidationService validationService;
    private final ICandidateMappingService mappingService;
    private final StreamBridge streamBridge;

    @Override
    @Transactional
    public CandidateResponseDto createCandidate(CreateCandidateRequestDto dto) {
        log.info("Creating candidate with email '{}', for job '{}'", dto.email(), dto.jobId());

        validationService.validateCreateRequest(dto);

        Candidate candidate = mappingService.toEntity(dto);
        Candidate saved = candidateRepository.save(candidate);

        log.info("Candidate created successfully with id '{}'", saved.getId());
        return mappingService.toResponseDto(saved);
    }

    private void publishCandidateAddedEvent(Candidate candidate) {
        CandidateAddedEvent event = new CandidateAddedEvent(
                candidate.getId(),
                candidate.getFullName(),
                candidate.getJobId() + "",
                "default client name",
                "Added By TAT user",
                candidate.getCreatedAt() + "",
                Arrays.asList("ADMIN", "TAT")
        );
        log.info("Sending CandidateAddedEvent: {}", event);
        boolean sent = streamBridge.send("sendCommunication-out-0", event);
        log.info("Is the communication request successfully triggered ? : {}",sent);
    }

    @Override
    public CandidateResponseDto fetchCandidateById(UUID candidateId) {
        log.debug("Fetching candidate with id '{}'", candidateId);

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", candidateId.toString()));

        return mappingService.toResponseDto(candidate);
    }

    @Override
    public PagedCandidateResponseDto fetchAllCandidates(CandidatePageRequestDto pageRequestDto) {
        log.debug("Fetching candidates with filters: {} (page: {}, size: {})",
                pageRequestDto, pageRequestDto.page(), pageRequestDto.size());

        // Parse and validate sort direction, default to DESC if invalid
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(pageRequestDto.sortDir());
        } catch (IllegalArgumentException e) {
            direction = Sort.Direction.DESC;
            log.warn("Invalid sort direction '{}', defaulting to DESC", pageRequestDto.sortDir());
        }

        Sort sort = Sort.by(direction, pageRequestDto.sortBy());
        Pageable pageable = PageRequest.of(pageRequestDto.page(), pageRequestDto.size(), sort);

        // Assuming candidateRepository extends JpaSpecificationExecutor<Candidate> or has a custom query method
        Page<Candidate> candidatePage = candidateRepository.findCandidatesWithFilters(
                pageRequestDto.jobId(),
                pageRequestDto.status(),
                pageRequestDto.experienceLevel(),
                pageRequestDto.skills(),
                pageRequestDto.country(),
                pageRequestDto.firstName(),
                pageRequestDto.lastName(),
                pageable
        );

        return new PagedCandidateResponseDto(
                candidatePage.getNumber(),
                candidatePage.getTotalPages(),
                candidatePage.getTotalElements(),
                candidatePage.isFirst(),
                candidatePage.isLast(),
                candidatePage.hasNext(),
                candidatePage.hasPrevious(),
                mappingService.toResponseDtoList(candidatePage.getContent())
        );
    }

    @Override
    @Transactional
    public CandidateResponseDto updateCandidate(UUID candidateId, UpdateCandidateRequestDto dto) {
        log.info("Updating candidate with id '{}'", candidateId);

        validationService.validateUpdateRequest(candidateId, dto);

        Candidate existing = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", candidateId.toString()));

        mappingService.updateEntity(existing, dto);
        Candidate updated = candidateRepository.save(existing);

        log.info("Candidate with id '{}' updated successfully", candidateId);
        return mappingService.toResponseDto(updated);
    }

    @Override
    @Transactional
    public CandidateResponseDto partialUpdateCandidate(UUID candidateId, PartialUpdateCandidateRequestDto dto) {
        log.info("Partially updating candidate with id '{}'", candidateId);

        validationService.validatePartialUpdateRequest(candidateId, dto);

        Candidate existing = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", candidateId.toString()));

        mappingService.partialUpdateEntity(existing, dto);
        Candidate updated = candidateRepository.save(existing);

        log.info("Candidate with id '{}' partially updated successfully", candidateId);
        return mappingService.toResponseDto(updated);
    }

    @Override
    @Transactional
    public CandidateResponseDto changeCandidateStatus(UUID candidateId, CandidateStatus newStatus) {
        log.info("Changing status for candidate '{}' to '{}'", candidateId, newStatus);

        Candidate existing = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", candidateId.toString()));

        validationService.validateStatusTransition(existing, newStatus);

        existing.setStatus(newStatus);
        Candidate updated = candidateRepository.save(existing);

        log.info("Status changed successfully for candidate '{}'", candidateId);
        return mappingService.toResponseDto(updated);
    }


    private void sendStatusChangedCommunication(Candidate candidate) {
        CandidateStatusChangedEvent event = new CandidateStatusChangedEvent(
                candidate.getCandidateId(),
                candidate.getFullName(),
                candidate.getJobId() + "",
                "default client name",
                candidate.getStatus().name(),
                "Updated by TAT",
                new Date().toString(),  // Ideally use formatter
                Arrays.asList("ADMIN", "TAT", "BD")
        );
        log.info("Sending CandidateStatusChangedEvent: {}", event);
        boolean sent = streamBridge.send("sendCommunication-out-1", event);
        log.info("CandidateStatusChangedEvent sent? {}", sent);
    }
    @Override
    @Transactional
    public void deleteCandidate(UUID candidateId) {
        log.info("Deleting candidate with id '{}'", candidateId);

        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate", "id", candidateId.toString()));

        validationService.validateDeletion(candidate);

        candidateRepository.delete(candidate);

        log.info("Candidate with id '{}' deleted successfully", candidateId);
    }

    @Override
    public boolean updateCommunicationStatus(UUID candidateId) {
        return false;
    }

//    @Override
//    public boolean updateCommunicationStatus(Long candidateId) {
//        boolean isUpdated = false;
//        if(candidateId!=null){
//            Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(
//                    () -> new ResourceNotFoundException("Candidate","CandidateId",candidateId+""));
//            candidate.setCommunicationSw(true);
//            candidateRepository.save(candidate);
//            isUpdated = true;
//        }
//        return isUpdated;
//    }

    @Override
    public boolean updateStatusCommunicationMap(UUID id, String status) {
        if (id == null || status == null || status.isBlank()) {
            return false;
        }

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate","CandidateId",id+""));

        Map<String, Boolean> statusMap = candidate.getStatusCommunicationMap();
        if (statusMap == null) {
            statusMap = new HashMap<>();
        }

        statusMap.put(status.toUpperCase(), true);
        candidate.setStatusCommunicationMap(statusMap);

        candidateRepository.save(candidate);
        return true;
    }


    private Specification<Candidate> buildSpecification(CandidateFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.getJobId() != null) {
                predicates.add(cb.equal(root.get("jobId"), filter.getJobId()));
            }
            if (filter.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.getStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

