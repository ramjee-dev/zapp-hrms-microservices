package com.zapp.user_service.service;

import com.zapp.user_service.dto.UserDto;

import java.util.List;

public interface IUserService {
    UserDto createUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
}
