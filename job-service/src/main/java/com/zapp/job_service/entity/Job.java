package com.zapp.job_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Setter@Getter@AllArgsConstructor@NoArgsConstructor
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.OPEN;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    private Boolean communicationSw;

    public enum JobStatus {
        OPEN,
        CLOSED
    }
}


