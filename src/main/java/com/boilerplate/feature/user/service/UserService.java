package com.boilerplate.feature.user.service;
import com.boilerplate.feature.user.dto.UserDto;

import java.util.UUID;


public interface UserService {
    UserDto checkStatus(UUID userId);
    UserDto updateProfile(UUID userId, UserDto dto);

}
