package com.zapp.message_service.function;

import com.zapp.message_service.dto.CandidateAddedEvent;
import com.zapp.message_service.dto.CandidateStatusChangedEvent;
import com.zapp.message_service.dto.JobCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

import com.zapp.message_service.dto.*;

@Configuration
@Slf4j
public class MessageFunction {

    // ===============================
    // 🔹 Job Created Event Functions
    // ===============================

    @Bean(name = "jobCreatedEmail")
    public Function<JobCreatedEvent, JobCreatedEvent> jobCreatedEmail() {
        return event -> {
            log.info("📧 [EMAIL] Job Created → Job: '{}', Client: '{}', By: {}, Notify Roles: {}",
                    event.getJobTitle(),
                    event.getClientName(),
                    event.getCreatedBy(),
                    event.getNotifyToRoles());
            return event;
        };
    }

    @Bean(name = "jobCreatedSms")
    public Function<JobCreatedEvent, Long> jobCreatedSms() {
        return event -> {
            log.info("📱 [SMS] Job Created → '{}' @ '{}' by {}. Notify: {}",
                    event.getJobTitle(),
                    event.getClientName(),
                    event.getCreatedBy(),
                    event.getNotifyToRoles());
            return event.getJobId();
        };
    }

    // ===============================
    // 🔹 Candidate Added Event Functions
    // ===============================

    @Bean(name = "candidateAddedEmail")
    public Function<CandidateAddedEvent, CandidateAddedEvent> candidateAddedEmail() {
        return event -> {
            log.info("📧 [EMAIL] Candidate Added → '{}' to '{}' @ '{}', By: {}, Notify: {}",
                    event.getCandidateName(),
                    event.getJobTitle(),
                    event.getClientName(),
                    event.getAddedBy(),
                    event.getNotifyToRoles());
            return event;
        };
    }

    @Bean(name = "candidateAddedSms")
    public Function<CandidateAddedEvent, Long> candidateAddedSms() {
        return event -> {
            log.info("📱 [SMS] Candidate Added → '{}' for '{}' @ '{}' by {}. Notify: {}",
                    event.getCandidateName(),
                    event.getJobTitle(),
                    event.getClientName(),
                    event.getAddedBy(),
                    event.getNotifyToRoles());
            return event.getCandidateId();
        };
    }

    // ===============================
    // 🔹 Candidate Status Changed Event Function
    // ===============================

    @Bean(name = "candidateStatusChanged")
    public Function<CandidateStatusChangedEvent, CandidateStatusAcknowledgedEvent> candidateStatusChanged() {
        return event -> {
            log.info("📧 [EMAIL] + 📱 [SMS] Status Changed → '{}' is now '{}' for '{}' @ '{}' by {}. Notify: {}",
                    event.getCandidateName(),
                    event.getStatus(),
                    event.getJobTitle(),
                    event.getClientName(),
                    event.getUpdatedBy(),
                    event.getNotifyToRoles());

            // Return acknowledgment event
            return new CandidateStatusAcknowledgedEvent(event.getCandidateId(), event.getStatus());
        };
    }
}



