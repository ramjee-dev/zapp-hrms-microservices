package com.zapp.user_service.service;

import com.zapp.user_service.dto.CreateUserDto;
import com.zapp.user_service.dto.UpdateUserDto;

import java.util.UUID;

public interface IUserValidationService {

    void validateCreateUser(CreateUserDto dto);

    void validateUpdateUser(UUID jobId, UpdateUserDto dto);

}
