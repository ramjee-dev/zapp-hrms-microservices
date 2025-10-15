package com.zapp.user_service.service;

import com.zapp.user_service.dto.*;
import com.zapp.user_service.enums.UserStatus;

import java.util.List;
import java.util.UUID;

public interface IUserService {

    UserResponseDto createUser(CreateUserRequestDto dto);

    UserResponseDto fetchUserById(UUID userId);

    PagedUserResponseDto fetchAllUsers(UserPageRequestDto pageRequestDto);

    UserResponseDto updateUser(UUID userId, UpdateUserRequestDto dto);

    UserResponseDto partialUpdateUser(UUID userId, PartialUpdateUserRequestDto dto);

    void deleteUser(UUID userId);

    UserResponseDto changeUserStatus(UUID userId, UserStatus newStatus);

}
