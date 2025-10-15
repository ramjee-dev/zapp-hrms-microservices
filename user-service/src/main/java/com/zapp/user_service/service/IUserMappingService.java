package com.zapp.user_service.service;

import com.zapp.user_service.dto.*;
import com.zapp.user_service.entity.User;

import java.util.List;

public interface IUserMappingService {

    User toEntity(CreateUserRequestDto dto);

    void updateEntity(User user, UpdateUserRequestDto dto);

    void partialUpdateEntity(User user, PartialUpdateUserRequestDto dto);

    UserResponseDto toResponseDto(User user);

    List<UserResponseDto> toResponseDtoList(List<User> users);
}
