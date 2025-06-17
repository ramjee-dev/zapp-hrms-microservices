package com.zapp.job_service.functions;

import com.zapp.job_service.service.IJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
@Slf4j
public class JobsFunctions {

    @Bean
    public Consumer<Long> updateCommunication(IJobService jobService){
        return jobId -> {
            log.info("updating communication status for Job with ID: {}",jobId);
            jobService.updateCommunicationStatus(jobId);
        };
    }

}
