package com.zapp.candidate_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateStatusChangedEvent {

    private UUID candidateId;
    private String candidateName;
    private String jobTitle;
    private String clientName;
    private String status; // e.g., SELECTED, REJECTED
    private String updatedBy;
    private String updatedAt;
    private List<String> notifyToRoles; // e.g., ["ADMIN", "TAT", "BD"]
}

