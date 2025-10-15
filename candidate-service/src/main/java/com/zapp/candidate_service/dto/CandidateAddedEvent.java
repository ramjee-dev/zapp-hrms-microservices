package com.zapp.candidate_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateAddedEvent {

    private UUID id;
    private String candidateName;
    private String jobTitle;
    private String clientName;
    private String addedBy; // TAT user
    private String createdAt;
    private List<String> notifyToRoles; // e.g., ["ADMIN", "TAT"]

}

