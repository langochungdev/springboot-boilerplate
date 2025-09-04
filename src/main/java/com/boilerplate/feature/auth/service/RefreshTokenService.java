package com.boilerplate.feature.auth.service;

import com.boilerplate.feature.auth.entity.RefreshToken;

import java.util.UUID;

public interface RefreshTokenService {
    RefreshToken handleLogin(UUID userId, String token, long refreshExpiresIn);
    RefreshToken validateAndRotate(UUID userId, String token, long refreshExpiresIn);
    void revokeToken(String token);
}
