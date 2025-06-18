package com.zapp.message_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateStatusAcknowledgedEvent {
    private Long candidateId;
    private String status;
}

