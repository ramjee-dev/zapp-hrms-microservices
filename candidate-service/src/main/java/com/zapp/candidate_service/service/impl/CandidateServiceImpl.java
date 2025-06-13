package com.zapp.candidate_service.service.impl;

import com.zapp.candidate_service.dto.CandidateDTO;
import com.zapp.candidate_service.dto.CandidateFilter;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.exception.ResourceNotFoundException;
import com.zapp.candidate_service.mapper.CandidateMapper;
import com.zapp.candidate_service.repository.CandidateRepository;
import com.zapp.candidate_service.service.ICandidateService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements ICandidateService {

    private final CandidateRepository candidateRepository;
    private final CandidateMapper candidateMapper;

    @Override
    public Candidate addCandidateToJob(Long jobId, CandidateDTO dto) {
        Candidate candidate = candidateMapper.toEntity(dto);
        candidate.setJobId(jobId);
        candidate.setStatus(Candidate.Status.APPLIED);
        return candidateRepository.save(candidate);
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
    public Candidate updateCandidateStatus(Long id, Candidate.Status status) {
        Candidate candidate = getCandidateById(id);
        candidate.setStatus(status);
        return candidateRepository.save(candidate);
    }

    @Override
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate not found with id " + id);
        }
        candidateRepository.deleteById(id);
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

