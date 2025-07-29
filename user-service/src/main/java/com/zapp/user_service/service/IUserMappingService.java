package com.zapp.user_service.service;

import com.zapp.user_service.dto.*;
import com.zapp.user_service.entity.User;

import java.util.List;

public interface IUserMappingService {

    User toEntity(CreateUserDto dto);

    void updateEntity(User user, UpdateUserDto dto);

    void partialUpdateEntity(User user, PartialUpdateUserDto dto);

    UserResponseDto toResponseDto(User user);

    List<UserResponseDto> toResponseDtoList(List<User> jobs);
}
