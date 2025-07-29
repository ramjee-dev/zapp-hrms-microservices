package com.zapp.user_service.service.impl;

import com.zapp.user_service.dto.CreateUserDto;
import com.zapp.user_service.dto.UpdateUserDto;
import com.zapp.user_service.service.IUserValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class UserValidationService implements IUserValidationService {

    @Override
    public void validateCreateUser(CreateUserDto dto) {

    }

    @Override
    public void validateUpdateUser(UUID jobId, UpdateUserDto dto) {

    }
}
