package com.zapp.candidate_service.entity;

import com.zapp.candidate_service.converter.CommunicationStatusConverter;
import com.zapp.candidate_service.enums.CandidateStatus;
import com.zapp.candidate_service.enums.ExperienceLevel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "candidates")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class Candidate extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID Id;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "job_id", nullable = false)
    private UUID jobId; // FK to job-service

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CandidateStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "experience_level")
    private ExperienceLevel experienceLevel;

    @Column(name = "years_of_experience", precision = 4, scale = 1)
    private BigDecimal yearsOfExperience;

    @Column(name = "skills", length = 1000)
    private String skills;

    @Column(name = "current_position", length = 200)
    private String currentPosition;

    @Column(name = "current_company", length = 200)
    private String currentCompany;

    @Column(name = "current_salary", precision = 12, scale = 2)
    private BigDecimal currentSalary;

    @Column(name = "expected_salary", precision = 12, scale = 2)
    private BigDecimal expectedSalary;

    @Column(name = "notice_period", length = 200)
    private String noticePeriod;

    @Column(name = "resume_url", length = 500)
    private String resumeUrl;

    @Column(name = "linkedin_profile", length = 200)
    private String linkedinProfile;

    @Column(name = "portfolio_url", length = 200)
    private String portfolioUrl;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "assigned_to")
    private UUID assignedTo; // FK to user-service (TAT user)

    // ✅ For tracking communication after candidate is added to job
    @Column(name = "communication_sw")
    private Boolean communicationSw = false;

    // ✅ For tracking communication for each status update (e.g., SELECTED, REJECTED)
    @Convert(converter = CommunicationStatusConverter.class)
    @Lob
    @Column(name = "status_communication_map")
    private Map<String, Boolean> statusCommunicationMap = new HashMap<>();

}



