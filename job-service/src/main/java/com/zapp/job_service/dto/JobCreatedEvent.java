package com.zapp.job_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCreatedEvent {

    private UUID jobId;
    private String jobTitle;
    private String clientName;
    private String createdBy; // BD username or full name
    private String createdAt; // ISO timestamp
    private List<String> notifyToRoles; // e.g., ["ADMIN", "BD"]

    // Getters, setters, constructor
}

