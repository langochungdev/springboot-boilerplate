package com.boilerplate.feature.auth.service.impl;
import com.boilerplate.common.exception.BusinessException;
import com.boilerplate.common.exception.errorcode.AuthError;
import com.boilerplate.common.util.JwtUtil;
import com.boilerplate.feature.auth.dto.AuthRequest;
import com.boilerplate.feature.auth.dto.AuthResponse;
import com.boilerplate.feature.auth.dto.RegisterRequest;
import com.boilerplate.feature.auth.entity.RefreshToken;
import com.boilerplate.feature.auth.service.AuthService;
import com.boilerplate.feature.auth.service.RefreshTokenService;
import com.boilerplate.feature.user.service.DeviceService;
import com.boilerplate.feature.user.dto.UserDto;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.mapper.UserMapper;
import com.boilerplate.feature.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final DeviceService deviceService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BusinessException(AuthError.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(AuthError.PASSWORD_NOT_MATCH);
        }

        deviceService.handleLoginDevice(
                user.getId(),
                request.getDeviceId(),
                request.getDeviceName(),
                request.getPushToken()
        );

        Set<String> roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());

        AuthResponse.User dto = AuthResponse.User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(roles)
                .build();

        long accessExpiresIn = jwtUtil.getAccessExpiration();
        long refreshExpiresIn = jwtUtil.getRefreshExpiration();

        String accessToken = jwtUtil.createToken(dto.getUsername(), dto.getId(), roles);
        String refreshToken = jwtUtil.createRefreshToken(dto.getId());

        refreshTokenService.handleLogin(user.getId(), refreshToken, refreshExpiresIn);

        return AuthResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(accessExpiresIn)
                .refreshExpiresIn(refreshExpiresIn)
                .user(dto)
                .build();
    }

    @Override
    public void logout(String refreshToken, UUID deviceId) {
        try {
            if (refreshToken != null) {
                jwtUtil.validate(refreshToken);
                UUID userId = jwtUtil.extractUserId(refreshToken);

                deviceService.revokeDevice(userId, deviceId);

                refreshTokenService.revokeToken(refreshToken);
                log.info("[AUTH] Logout thành công user={}, device deactivated={}, refreshtoken={}",
                        userId, deviceId, refreshToken);
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

    @Override
    public AuthResponse refresh(String refreshToken) {
        jwtUtil.validate(refreshToken);
        UUID userId = jwtUtil.extractUserId(refreshToken);

        RefreshToken newRefresh = refreshTokenService.validateAndRotate(userId, refreshToken, jwtUtil.getRefreshExpiration());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(AuthError.USER_NOT_FOUND));
        Set<String> roles = user.getUserRoles().stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toSet());

        AuthResponse.User dto = AuthResponse.User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .roles(roles)
                .build();

        long accessExpiresIn = jwtUtil.getAccessExpiration();
        String newAccessToken = jwtUtil.createToken(user.getUsername(), user.getId(), roles);

        return AuthResponse.builder()
                .token(newAccessToken)
                .refreshToken(newRefresh.getToken())
                .expiresIn(accessExpiresIn)
                .refreshExpiresIn(jwtUtil.getRefreshExpiration())
                .user(dto)
                .build();
    }


}
