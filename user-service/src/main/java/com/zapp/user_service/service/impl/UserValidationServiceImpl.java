package com.zapp.user_service.service.impl;

import com.zapp.user_service.dto.CreateUserRequestDto;
import com.zapp.user_service.dto.PartialUpdateUserRequestDto;
import com.zapp.user_service.dto.UpdateUserRequestDto;
import com.zapp.user_service.entity.User;
import com.zapp.user_service.enums.UserStatus;
import com.zapp.user_service.exception.BusinessValidationException;
import com.zapp.user_service.exception.ResourceNotFoundException;
import com.zapp.user_service.respository.UserRepository;
import com.zapp.user_service.service.IUserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserValidationServiceImpl implements IUserValidationService {

    private final UserRepository userRepository;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]{3,50}$");

    @Override
    public void validateCreateRequest(CreateUserRequestDto dto) {

        log.debug("Validating create user request: username='{}', email='{}'", dto.username(), dto.email());

        List<String> errors = new ArrayList<>();

        // Trim inputs safely
        String username = dto.username() != null ? dto.username().trim() : null;
        String email = dto.email() != null ? dto.email().trim() : null;

        if (username == null || username.isBlank()) {
            errors.add("Username is required.");
        } else if (!USERNAME_PATTERN.matcher(username).matches()) {
            errors.add("Username must be 3-50 characters, alphanumeric with ., _, or -.");
        } else if (userRepository.existsByUsernameIgnoreCase(username)) {
            errors.add("Username already exists.");
        }

        if (email == null || email.isBlank()) {
            errors.add("Email is required.");
        } else if (userRepository.existsByEmailIgnoreCase(email)) {
            errors.add("Email already exists.");
        }

        if (dto.firstName() == null || dto.firstName().isBlank()) {
            errors.add("First name is required.");
        }

        if (dto.role() == null) {
            errors.add("User role is required.");
        }

        if (dto.status() == null) {
            errors.add("User status is required.");
        }

        // Additional business rules can be added here
        // Example: Disallow certain role/status combinations, or reserved usernames

        if (!errors.isEmpty()) {
            log.warn("User create validation failed: {}", errors);
            throw new BusinessValidationException("Validation failed for user creation.", errors);
        }
        log.info("User create request validation succeeded for username='{}', email='{}'", username, email);
    }

    @Override
    public void validateUpdateRequest(UUID userId, UpdateUserRequestDto dto) {
        log.debug("Validating update user request for email='{}'", dto.email());
        List<String> errors = new ArrayList<>();

        // Validate user existence
        if (!userRepository.existsById(userId)) {
            log.warn("User not found for update, id={}", userId);
            throw new ResourceNotFoundException("User", "id", userId.toString());
        }

        // Email uniqueness if changed
        if (dto.email() != null) {
            String email = dto.email().trim();
            var existing = userRepository.findByEmailIgnoreCase(email);
            if (existing.isPresent()) {
                errors.add("Email '" + email + "' is already used by another user.");
            }
        }

        if (dto.firstName() == null || dto.firstName().isBlank()) {
            errors.add("First name is required.");
        }

        if (dto.role() == null) {
            errors.add("User role is required.");
        }

        if (dto.status() == null) {
            errors.add("User status is required.");
        }

        // Add further business rules if necessary

        if (!errors.isEmpty()) {
            log.warn("User update validation failed: {}", errors);
            throw new BusinessValidationException("Validation failed for user update.", errors);
        }
        log.info("User update validation passed for email='{}'", dto.email());
    }

    @Override
    public void validatePartialUpdateRequest(UUID userId, PartialUpdateUserRequestDto dto) {

        log.debug("Validating partial update user request");

        List<String> errors = new ArrayList<>();

        // Validate user existence
        if (!userRepository.existsById(userId)) {
            log.warn("User not found for update, id={}", userId);
            throw new ResourceNotFoundException("User", "id", userId.toString());
        }

        if (dto.username() != null) {
            String username = dto.username().trim();
            if (username.isBlank()) {
                errors.add("Username cannot be blank.");
            } else if (!USERNAME_PATTERN.matcher(username).matches()) {
                errors.add("Username must be 3-50 characters, alphanumeric with ., _, or -.");
            }
            // Additional uniqueness check is done in service where userId is known
        }

        if (dto.email() != null) {
            String email = dto.email().trim();
            if (email.isBlank()) {
                errors.add("Email cannot be blank.");
            }
            // Uniqueness check with ID must be done in service impl
        }

        if (dto.firstName() != null && dto.firstName().isBlank()) {
            errors.add("First name cannot be blank.");
        }

        // Role and status are enums, no direct validation needed here, null means no change

        if (!errors.isEmpty()) {
            log.warn("User partial update validation failed: {}", errors);
            throw new BusinessValidationException("Validation failed for user partial update.", errors);
        }
        log.info("User partial update validation passed.");
    }

    @Override
    public void validateStatusTransition(User existingUser, UserStatus newStatus) {
        log.debug("Validating status transition for user '{}': newStatus='{}'", existingUser.getId(), newStatus);

        User user = userRepository.findById(existingUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", existingUser.getId().toString()));

        UserStatus currentStatus = user.getStatus();
        // Define allowed transitions - example
        Map<UserStatus, Set<UserStatus>> allowedTransitions = Map.of(
                UserStatus.PENDING_ACTIVATION, Set.of(UserStatus.ACTIVE, UserStatus.INACTIVE, UserStatus.SUSPENDED),
                UserStatus.ACTIVE, Set.of(UserStatus.INACTIVE, UserStatus.SUSPENDED, UserStatus.LOCKED),
                UserStatus.INACTIVE, Set.of(UserStatus.ACTIVE),
                UserStatus.SUSPENDED, Set.of(UserStatus.ACTIVE, UserStatus.INACTIVE),
                UserStatus.LOCKED, Set.of(UserStatus.ACTIVE),
                UserStatus.EXPIRED, Set.of(UserStatus.ACTIVE)
        );

        Set<UserStatus> validNextStatuses = allowedTransitions.getOrDefault(currentStatus, Set.of());

        if (!validNextStatuses.contains(newStatus)) {
            String message = String.format("Invalid status transition from '%s' to '%s'", currentStatus, newStatus);
            log.warn("User [{}] status transition violation: {}", existingUser.getId(), message);
            throw new BusinessValidationException(
                    message,
                    List.of("Status transition not allowed: " + currentStatus + " -> " + newStatus)
            );
        }

        log.info("User '{}' status transition validated from '{}' to '{}'.", existingUser.getId(), currentStatus, newStatus);
    }

    @Override
    public void validateDeletion(User existingUser) {
        log.debug("Validating deletion for user '{}'", existingUser);

        User user = userRepository.findById(existingUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", existingUser.getId().toString()));

        List<String> errors = new ArrayList<>();

        // Example business rule: do not delete active users or locked users
        if (user.getStatus() == UserStatus.ACTIVE || user.getStatus() == UserStatus.LOCKED) {
            errors.add("Cannot delete a user with status " + user.getStatus());
        }

        // TODO: add checks for dependencies or audit constraints if required

        if (!errors.isEmpty()) {
            log.warn("User deletion validation failed for id={}: {}", existingUser.getId(), errors);
            throw new BusinessValidationException("Validation failed for user deletion.", errors);
        }

        log.info("User deletion validation succeeded for id={}", existingUser.getId());
    }
}