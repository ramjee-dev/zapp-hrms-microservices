package com.zapp.message_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JobCreatedEvent {

    private Long jobId;
    private String jobTitle;
    private String clientName;
    private String createdBy; // BD username or full name
    private String createdAt; // ISO timestamp
    private List<String> notifyToRoles; // e.g., ["ADMIN", "BD"]
}

