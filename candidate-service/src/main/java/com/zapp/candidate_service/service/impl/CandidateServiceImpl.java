package com.zapp.candidate_service.service.impl;

import com.zapp.candidate_service.dto.CandidateAddedEvent;
import com.zapp.candidate_service.dto.CandidateDTO;
import com.zapp.candidate_service.dto.CandidateFilter;
import com.zapp.candidate_service.dto.CandidateStatusChangedEvent;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.exception.ResourceNotFoundException;
import com.zapp.candidate_service.mapper.CandidateMapper;
import com.zapp.candidate_service.repository.CandidateRepository;
import com.zapp.candidate_service.service.ICandidateService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateServiceImpl implements ICandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;
    private final StreamBridge streamBridge;

    @Override
    public Candidate addCandidateToJob(Long jobId, CandidateDTO dto) {
        Candidate candidate = candidateMapper.toEntity(dto);
        candidate.setJobId(jobId);
        candidate.setStatus(Candidate.Status.APPLIED);
        Candidate savedCandidate = candidateRepository.save(candidate);
        sendCommunication(savedCandidate);
        return savedCandidate;
    }

    private void sendCommunication(Candidate candidate) {
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
        log.info("CandidateAddedEvent sent? {}", sent);
    }

    @Override
    public Page<Candidate> getAllCandidates(Pageable pageable, CandidateFilter filter) {
        Specification<Candidate> spec = buildSpecification(filter);
        return candidateRepository.findAll(spec, pageable);
    }

    @Override
    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with id " + id));
    }

    @Override
    public Candidate updateCandidate(Long id, CandidateDTO dto) {
        Candidate existing = getCandidateById(id);
        existing.setFullName(dto.getFullName());
        existing.setEmail(dto.getEmail());
        existing.setPhone(dto.getPhone());
        existing.setRemarks(dto.getRemarks());
        return candidateRepository.save(existing);
    }

    @Override
    public Candidate updateCandidateStatus(Long id, Candidate.Status newStatus) {
        Candidate candidate = getCandidateById(id);

        // 1. Update the status
        candidate.setStatus(newStatus);

        // 2. Update the communication map (VERY IMPORTANT)
        Map<String, Boolean> commMap = candidate.getStatusCommunicationMap();
        if (commMap == null) {
            commMap = new HashMap<>();
        }
        commMap.put(newStatus.name(), true); // e.g., SELECTED → true
        candidate.setStatusCommunicationMap(commMap); // update the field

        // 3. Save the updated entity
        Candidate updated = candidateRepository.save(candidate);

        // 4. Send communication event
        sendStatusChangedCommunication(updated);

        return updated;
    }


    private void sendStatusChangedCommunication(Candidate candidate) {
        CandidateStatusChangedEvent event = new CandidateStatusChangedEvent(
                candidate.getId(),
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
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate not found with id " + id);
        }
        candidateRepository.deleteById(id);
    }

    @Override
    public boolean updateCommunicationStatus(Long id) {
        boolean isUpdated = false;
        if(id!=null){
            Candidate candidate = candidateRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Candidate not found with given ID: " + id));
            candidate.setCommunicationSw(true);
            candidateRepository.save(candidate);
            isUpdated = true;
        }
        return isUpdated;
    }

    @Override
    public boolean updateStatusCommunicationMap(Long id, String status) {
        if (id == null || status == null || status.isBlank()) {
            return false;
        }

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found with ID: " + id));

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

