package com.zapp.user_service.entity;

import com.zapp.user_service.enums.UserRole;
import com.zapp.user_service.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity@Builder
@Table(name = "users", indexes = {
        @Index(name = "idx_users_email", columnList = "email"),
        @Index(name = "idx_users_username", columnList = "username"),
        @Index(name = "idx_users_status", columnList = "status")
})
@Getter@Setter@NoArgsConstructor@AllArgsConstructor
public class User extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The Keycloak internal UUID referencing the user in the Keycloak realm.
     */
    @Column(name = "keycloak_user_id", unique = true, nullable = false, length = 36)
    private String keycloakUserId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "time_zone", length = 50)
    private String timeZone;

    @Column(name = "locale", length = 10)
    private String locale;

    /**
     * Status of the user account.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private UserStatus status;

    /**
     * Primary application-level role for the user.
     * The actual authorization in token comes from Keycloak,
     * but this is for internal role mapping or quick reference.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified = false;

    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified = false;

    @Column(name = "last_password_reset_at")
    private Instant lastPasswordResetAt;

    @Column(name = "failed_login_attempts", nullable = false)
    private int failedLoginAttempts = 0;

    @Column(name = "account_locked_until")
    private Instant accountLockedUntil;

    @Column(name = "description", length = 500)
    private String description;

    // Additional fields relevant for your HRMS app can be added here following the same approach

}

