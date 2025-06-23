package com.zapp.candidate_service.controller;


import com.zapp.candidate_service.dto.CandidateDTO;
import com.zapp.candidate_service.dto.CandidateFilter;
import com.zapp.candidate_service.entity.Candidate;
import com.zapp.candidate_service.service.ICandidateService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class CandidateController {

    private final ICandidateService candidateService;

    @PostMapping("/jobs/{jobId}/candidates")
    public ResponseEntity<Candidate> addCandidate(@PathVariable Long jobId, @RequestBody @Valid CandidateDTO dto) {
        return ResponseEntity.ok(candidateService.addCandidateToJob(jobId, dto));
    }

    @GetMapping("/candidates")
    public ResponseEntity<Page<Candidate>> listCandidates(Pageable pageable, CandidateFilter filter) {
        return ResponseEntity.ok(candidateService.getAllCandidates(pageable, filter));
    }

    @RateLimiter(name = "getCandidateById",fallbackMethod = "getCandidateByIdFallback")
    @GetMapping("/candidates/{id}")
    public ResponseEntity<Candidate> getById(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getCandidateById(id));
    }

    public ResponseEntity<Candidate> getCandidateByIdFallback(Long id, Throwable throwable) {
        log.error("Rate limiter fallback triggered for getCandidateById. Reason: {}", throwable.getMessage());

        // Return a safe fallback (e.g., 429 Too Many Requests)
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .body(null); // or a placeholder Candidate if needed
    }


    @PutMapping("/candidates/{id}")
    public ResponseEntity<Candidate> update(@PathVariable Long id, @RequestBody CandidateDTO dto) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, dto));
    }

    @PatchMapping("/candidates/{id}/status")
    public ResponseEntity<Candidate> updateStatus(@PathVariable Long id, @RequestParam Candidate.Status status) {
        return ResponseEntity.ok(candidateService.updateCandidateStatus(id, status));
    }

    @DeleteMapping("/candidates/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }
}
