package com.zapp.user_service.service.impl;

import com.zapp.user_service.dto.*;
import com.zapp.user_service.entity.User;
import com.zapp.user_service.enums.UserStatus;
import com.zapp.user_service.exception.ResourceNotFoundException;
import com.zapp.user_service.mapper.UserMapper;
import com.zapp.user_service.respository.UserRepository;
import com.zapp.user_service.service.IUserMappingService;
import com.zapp.user_service.service.IUserService;
import com.zapp.user_service.service.IUserValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private  final UserRepository userRepository;
    private final IUserMappingService mappingService;
    private final IUserValidationService validationService;


    @Override
    public UserResponseDto createUser(CreateUserRequestDto dto) {
        log.info("Creating user with username: {}", dto.username());

        // Perform all validations, including uniqueness, inside validationService
        validationService.validateCreateRequest(dto);

        // If validation passes, map DTO to entity
        User user = mappingService.toEntity(dto);

        // Save entity in repository
        User savedUser = userRepository.save(user);

        log.info("User created with ID: {}", savedUser.getId());

        // Map saved entity to Response DTO and return
        return mappingService.toResponseDto(user);
    }

    @Override
    public UserResponseDto fetchUserById(UUID userId) {
        log.info("Fetching user with ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User","Id",userId+""));

        return mappingService.toResponseDto(user);
    }

    @Override
    public PagedUserResponseDto fetchAllUsers(UserPageRequestDto pageRequestDto) {

        log.info("Fetching users with filters: {}",pageRequestDto, pageRequestDto.page(), pageRequestDto.size());


        return null;
    }

    @Override
    public UserResponseDto updateUser(UUID userId, UpdateUserRequestDto dto) {
        return null;
    }

    @Override
    public UserResponseDto partialUpdateUser(UUID userId, PartialUpdateUserRequestDto dto) {
        return null;
    }

    @Override
    public void deleteUser(UUID userId) {

    }

    @Override
    public UserResponseDto changeUserStatus(UUID userId, UserStatus newStatus) {
        return null;
    }
}
