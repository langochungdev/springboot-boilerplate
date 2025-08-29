package com.boilerplate.feature.auth.service.impl;
import com.boilerplate.common.exception.BusinessException;
import com.boilerplate.common.exception.errorcode.AuthError;
import com.boilerplate.common.service.TokenBlacklistService;
import com.boilerplate.common.util.CurrentUserUtil;
import com.boilerplate.common.util.JwtUtil;
import com.boilerplate.feature.auth.dto.RegisterRequest;
import com.boilerplate.feature.auth.dto.AuthRequest;
import com.boilerplate.feature.auth.dto.AuthResponse;
import com.boilerplate.feature.auth.service.AuthService;
import com.boilerplate.feature.device.entity.Device;
import com.boilerplate.feature.device.repository.DeviceRepository;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.dto.UserDto;
import com.boilerplate.feature.user.mapper.UserMapper;
import com.boilerplate.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final TokenBlacklistService tokenBlacklistService;
    private final CurrentUserUtil currentUserUtil;
    private final DeviceRepository deviceRepository;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(AuthError.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(AuthError.PASSWORD_NOT_MATCH);
        }

        Device device = deviceRepository
                .findByUserIdAndFingerprint(user.getId(), request.getFingerprint())
                .orElse(Device.builder()
                        .user(user)
                        .fingerprint(request.getFingerprint())
                        .build());

        device.setDeviceToken(request.getDeviceToken());
        device.setDeviceName(request.getDeviceName());
        device.setLastLogin(LocalDateTime.now());
        device.setIsActive(true);
        deviceRepository.save(device);

        long expiresIn = jwtUtil.getExpiration();

        AuthResponse.User dto = AuthResponse.User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        log.info("[AUTH] Login thành công: user={} id={} role={} device={}",
                user.getUsername(), user.getId(), user.getRole(), request.getDeviceName());

        return AuthResponse.builder()
                .expiresIn(expiresIn)
                .user(dto)
                .build();
    }

    @Override
    public void logout(String token) {
        try {
            if (token != null && jwtUtil.validateToken(token)) {
                UUID userId = jwtUtil.extractUserId(token);

                deviceRepository.findAllByUserId(userId).forEach(d -> {
                    d.setIsActive(false);
                    deviceRepository.save(d);
                });

                long ttlMs = jwtUtil.getExpirationFromToken(token) - System.currentTimeMillis();
                if (ttlMs > 0) {
                    tokenBlacklistService.blacklistToken(token, ttlMs);
                }

                log.info("[AUTH] Logout thành công user={}, devices deactivated={}, blacklisted={}",
                        userId, true, ttlMs > 0);
            }
        } catch (Exception e) {
            log.error("[AUTH] Lỗi khi logout: {}", e.getMessage(), e);
        }
    }

    @Override
    public UserDto register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);

        log.info("[AUTH] User mới đăng ký: username={} email={} id={}",
                user.getUsername(), user.getEmail(), user.getId());

        return userMapper.toDto(user);
    }
}
