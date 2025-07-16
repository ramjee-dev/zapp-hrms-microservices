package com.zapp.client_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
@Builder
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false, unique = true)
    private String name;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
