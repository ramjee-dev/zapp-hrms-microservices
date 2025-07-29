package com.zapp.client_service.entity;

import com.zapp.client_service.enums.ClientStatus;
import com.zapp.client_service.enums.ClientType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "clients")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
@Builder
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "contact_person", nullable = false)
    private String contactPerson;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "website")
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type", nullable = false)
    private ClientType clientType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClientStatus status = ClientStatus.PENDING_APPROVAL;

    @Column(name = "description")
    private String description;

    @Column(name = "employee_count")
    private Integer employeeCount;

    @Column(name = "industry")
    private String industry;

}
