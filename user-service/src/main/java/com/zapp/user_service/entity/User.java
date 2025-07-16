package com.zapp.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Setter@Getter@NoArgsConstructor@AllArgsConstructor
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.ACTIVE;

    public enum Role {
        ADMIN, BD, TAT
    }

    public enum Status {
        ACTIVE, INACTIVE
    }
}
