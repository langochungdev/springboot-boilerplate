package com.boilerplate.feature.auth.service;
import com.boilerplate.feature.auth.dto.AuthRequest;
import com.boilerplate.feature.auth.dto.AuthResponse;
import com.boilerplate.feature.auth.dto.RegisterRequest;
import com.boilerplate.feature.user.dto.UserDto;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    void logout(String token, String fingerprint);
    UserDto register(RegisterRequest request);
    AuthResponse refresh(String refreshToken);
}
