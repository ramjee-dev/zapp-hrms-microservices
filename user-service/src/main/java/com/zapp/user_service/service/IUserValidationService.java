package com.zapp.user_service.service;

import com.zapp.user_service.dto.CreateUserRequestDto;
import com.zapp.user_service.dto.PartialUpdateUserRequestDto;
import com.zapp.user_service.dto.UpdateUserRequestDto;
import com.zapp.user_service.entity.User;
import com.zapp.user_service.enums.UserStatus;

import java.util.UUID;

public interface IUserValidationService {

    void validateCreateRequest(CreateUserRequestDto dto);

    void validateUpdateRequest(UUID userId, UpdateUserRequestDto dto);

    void validatePartialUpdateRequest(UUID userId, PartialUpdateUserRequestDto dto);

    void validateStatusTransition(User user, UserStatus newStatus);

    void validateDeletion(User user);

}
