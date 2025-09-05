package com.boilerplate.feature.user.service;
import com.boilerplate.feature.user.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.UUID;


public interface UserService {
    UserDto updateProfile(UUID userId, UserDto dto);
    Page<UserDto> getAllUsers(int page, int size, String search);
    UserDto getUserById(UUID userId);
    void lockUser(UUID userId);
    void unlockUser(UUID userId);
}
