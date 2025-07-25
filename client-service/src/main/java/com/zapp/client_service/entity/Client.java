package com.zapp.client_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients")
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
@Builder
public class Client extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false, unique = true,length = 100)
    private String name;

    @Column(nullable = false,length = 100)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false,length = 20)
    private Status status = Status.ACTIVE;

    public enum Status {
        ACTIVE,
        INACTIVE
    }
}
