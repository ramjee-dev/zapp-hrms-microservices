package com.zapp.candidate_service.function;

import com.zapp.candidate_service.dto.CandidateStatusAcknowledgedEvent;
import com.zapp.candidate_service.service.ICandidateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class CandidateFunctions {

    // For Candidate Added Event → updates communicationSw = true
//    @Bean
//    public Consumer<Long> updateCommunication(ICandidateService candidateService) {
//        return candidateId -> {
//            log.info("✅ [Candidate Added] Updating communicationSw for Candidate ID: {}", candidateId);
//            candidateService.updateCommunicationStatus(candidateId);
//        };
//    }

    // For Candidate Status Change Event → updates statusCommunicationMap
//    @Bean
//    public Consumer<CandidateStatusAcknowledgedEvent> updateStatusCommunication(ICandidateService candidateService) {
//        return event -> {
//            log.info("✅ [Status Changed] Updating statusCommunicationMap for Candidate ID: {}, Status: {}",
//                    event.getCandidateId(), event.getStatus());
//            candidateService.updateStatusCommunicationMap(event.getCandidateId(), event.getStatus());
//        };
//    }
}

