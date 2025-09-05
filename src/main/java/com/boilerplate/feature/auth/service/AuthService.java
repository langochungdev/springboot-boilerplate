package com.boilerplate.feature.auth.service;
import com.boilerplate.feature.auth.dto.AuthRequest;
import com.boilerplate.feature.auth.dto.AuthResponse;
import com.boilerplate.feature.auth.dto.RegisterRequest;
import com.boilerplate.feature.user.dto.UserDto;

import java.util.UUID;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    void logout(String token, UUID deviceId);
    UserDto register(RegisterRequest request);
    AuthResponse refresh(String refreshToken);
}
