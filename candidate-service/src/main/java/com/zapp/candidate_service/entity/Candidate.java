package com.zapp.candidate_service.entity;

import com.zapp.candidate_service.converter.CommunicationStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
@Builder
public class Candidate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;

    @Column(nullable = false)
    private String fullName;

    private String email;
    private String phone;

    @Column(nullable = false)
    private Long jobId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.APPLIED;

    @Lob
    private String remarks;

    // ✅ For tracking communication after candidate is added to job
    @Column(name = "communication_sw")
    private Boolean communicationSw = false;

    // ✅ For tracking communication for each status update (e.g., SELECTED, REJECTED)
    @Convert(converter = CommunicationStatusConverter.class)
    @Lob
    @Column(name = "status_communication_map")
    private Map<String, Boolean> statusCommunicationMap = new HashMap<>();

    public enum Status {
        APPLIED, INTERVIEWED, SELECTED, REJECTED
    }
}



