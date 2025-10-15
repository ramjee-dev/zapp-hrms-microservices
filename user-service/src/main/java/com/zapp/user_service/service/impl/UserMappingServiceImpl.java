package com.zapp.user_service.service.impl;

import com.zapp.user_service.dto.CreateUserRequestDto;
import com.zapp.user_service.dto.PartialUpdateUserRequestDto;
import com.zapp.user_service.dto.UpdateUserRequestDto;
import com.zapp.user_service.dto.UserResponseDto;
import com.zapp.user_service.entity.User;
import com.zapp.user_service.service.IUserMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class UserMappingServiceImpl implements IUserMappingService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public User toEntity(CreateUserRequestDto dto) {
        log.debug("Mapping CreateUserRequestDto to User entity, username: {}, email: {}",
                dto.username(), dto.email());

        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());
        user.setPhoneNumber(dto.phoneNumber());
        user.setAvatarUrl(dto.avatarUrl());
        user.setTimeZone(dto.timeZone());
        user.setLocale(dto.locale());
        user.setDescription(dto.description());
        user.setRole(dto.role());
        user.setStatus(dto.status());

        // other fields like id, timestamps etc. are handled by JPA or service layer

        log.info("Created new User entity, username: {}, email: {}",
                user.getUsername(), user.getEmail());

        return user;
    }

    @Override
    public void updateEntity(User entity, UpdateUserRequestDto dto) {

        log.debug("Updating User entity from UpdateUserRequestDto");
        // if any field is immutable after creation; omit here.
        entity.setEmail(dto.email());
        entity.setFirstName(dto.firstName());
        entity.setLastName(dto.lastName());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setAvatarUrl(dto.avatarUrl());
        entity.setTimeZone(dto.timeZone());
        entity.setLocale(dto.locale());
        entity.setDescription(dto.description());
        entity.setRole(dto.role());
        // Do not update status here—those are managed by dedicated flows.
    }

    @Override
    public void partialUpdateEntity(User entity, PartialUpdateUserRequestDto dto) {

        log.debug("Partially updating User entity from PartialUpdateUserRequestDto");

        // Only update present (non-null) fields.
        // if any field is immutable after creation; omit here.

        if (dto.username() != null) entity.setUsername(dto.username());
        if (dto.email() != null) entity.setEmail(dto.email());
        if (dto.firstName() != null) entity.setFirstName(dto.firstName());
        if (dto.lastName() != null) entity.setLastName(dto.lastName());
        if (dto.phoneNumber() != null) entity.setPhoneNumber(dto.phoneNumber());
        if (dto.avatarUrl() != null) entity.setAvatarUrl(dto.avatarUrl());
        if (dto.timeZone() != null) entity.setTimeZone(dto.timeZone());
        if (dto.locale() != null) entity.setLocale(dto.locale());
        if (dto.description() != null) entity.setDescription(dto.description());
        if (dto.role() != null) entity.setRole(dto.role());
        // Do NOT update status field here (should use changeStatus).

    }

    @Override
    public UserResponseDto toResponseDto(User entity) {

        log.trace("Mapping User entity (id={}) to UserResponseDto", entity.getId());

        return new UserResponseDto(
                entity.getId(),
                entity.getKeycloakUserId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getPhoneNumber(),
                entity.getAvatarUrl(),
                entity.getTimeZone(),
                entity.getLocale(),
                entity.getRole(),
                entity.getStatus(),
                entity.isEmailVerified(),
                entity.isPhoneVerified(),
                entity.getLastPasswordResetAt(),
                entity.getFailedLoginAttempts(),
                entity.getAccountLockedUntil(),
                entity.getDescription(),
                // Audit fields: ISO-8601 UTC format or null
                entity.getCreatedAt() != null ? FORMATTER.format(entity.getCreatedAt()) : null,
                entity.getUpdatedAt() != null ? FORMATTER.format(entity.getUpdatedAt()) : null,
                entity.getCreatedBy(),
                entity.getUpdatedBy()
        );
    }

    @Override
    public List<UserResponseDto> toResponseDtoList(List<User> entities) {

        log.trace("Mapping list of {} User entities to UserResponseDtos", entities != null ? entities.size() : 0);

        if (entities == null) return List.of();

        return entities.stream().filter(Objects::nonNull).map(this::toResponseDto).toList();
    }
}
