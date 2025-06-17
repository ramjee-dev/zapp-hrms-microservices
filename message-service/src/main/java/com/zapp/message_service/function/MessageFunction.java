package com.zapp.message_service.function;

import com.zapp.message_service.dto.CandidateAddedEvent;
import com.zapp.message_service.dto.CandidateStatusChangedEvent;
import com.zapp.message_service.dto.JobCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;
import java.util.function.Function;

@Configuration
@Slf4j
public class MessageFunction {

    // ===============================
    // 🔹 Job Created Event Functions
    // ===============================

    @Bean(name = "jobCreatedEmail")
    public Function<JobCreatedEvent,JobCreatedEvent> jobCreatedEmail() {
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
    public Function<JobCreatedEvent,Long> jobCreatedSms() {
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

//    @Bean(name = "candidateAddedEmail")
//    public Consumer<CandidateAddedEvent> candidateAddedEmail() {
//        return event -> {
//            log.info("📧 [EMAIL] Candidate Added → '{}' to '{}' @ '{}', By: {}, Notify: {}",
//                    event.getCandidateName(),
//                    event.getJobTitle(),
//                    event.getClientName(),
//                    event.getAddedBy(),
//                    event.getNotifyToRoles());
//        };
//    }
//
//    @Bean(name = "candidateAddedSms")
//    public Consumer<CandidateAddedEvent> candidateAddedSms() {
//        return event -> {
//            log.info("📱 [SMS] Candidate Added → '{}' for '{}' @ '{}' by {}. Notify: {}",
//                    event.getCandidateName(),
//                    event.getJobTitle(),
//                    event.getClientName(),
//                    event.getAddedBy(),
//                    event.getNotifyToRoles());
//        };
//    }
//
//    // ===============================
//    // 🔹 Candidate Status Changed Functions
//    // ===============================
//
//    @Bean(name = "candidateStatusChangedEmail")
//    public Consumer<CandidateStatusChangedEvent> candidateStatusChangedEmail() {
//        return event -> {
//            log.info("📧 [EMAIL] Status Changed → '{}' is now '{}' for '{}' @ '{}' by {}. Notify: {}",
//                    event.getCandidateName(),
//                    event.getStatus(),
//                    event.getJobTitle(),
//                    event.getClientName(),
//                    event.getUpdatedBy(),
//                    event.getNotifyToRoles());
//        };
//    }
//
//    @Bean(name = "candidateStatusChangedSms")
//    public Consumer<CandidateStatusChangedEvent> candidateStatusChangedSms() {
//        return event -> {
//            log.info("📱 [SMS] Status Update → '{}' is now '{}' for '{}' @ '{}' by {}. Notify: {}",
//                    event.getCandidateName(),
//                    event.getStatus(),
//                    event.getJobTitle(),
//                    event.getClientName(),
//                    event.getUpdatedBy(),
//                    event.getNotifyToRoles());
//        };
//    }
}


