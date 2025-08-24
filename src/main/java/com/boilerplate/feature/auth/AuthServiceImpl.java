package com.boilerplate.feature.auth;
import com.boilerplate.common.service.TokenBlacklistService;
import com.boilerplate.common.util.CurrentUserUtil;
import com.boilerplate.common.util.JwtUtil;
import com.boilerplate.feature.device.Device;
import com.boilerplate.feature.device.DeviceRepository;
import com.boilerplate.feature.user.User;
import com.boilerplate.feature.user.UserDto;
import com.boilerplate.feature.user.UserMapper;
import com.boilerplate.feature.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<?> login(AuthRequest request, HttpServletResponse response) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);
        if (user == null) {
            log.warn("[AUTH] Login thất bại: username={} không tồn tại", request.getUsername());
//            ErrorResponder.sendError(response, "sai tai khoan");
            return ResponseEntity.badRequest().build();
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            log.warn("[AUTH] Login thất bại: username={} nhập sai mật khẩu", request.getUsername());
//            ErrorResponder.sendError(response, "sai mat khau");
            return ResponseEntity.badRequest().build();
        }

        // Lưu hoặc cập nhật thông tin thiết bị
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

        String token = jwtUtil.createToken(user.getUsername(), user.getId(), user.getRole());
        long expiresIn = jwtUtil.getExpiration();

        AuthResponse.User dto = AuthResponse.User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .expiresIn(expiresIn)
                .user(dto)
                .build();

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(expiresIn / 1000)
                .build();
        log.info("[AUTH] Login thành công: user={} id={} role={} device={}",
                user.getUsername(), user.getId(), user.getRole(), request.getDeviceName());
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @Override
    public UserDto register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        log.info("[AUTH] User mới đăng ký: username={} email={} id={}", user.getUsername(), user.getEmail(), user.getId());
        return userMapper.toDto(user);
    }

    @Override
    public ResponseEntity<?> logout(String token, HttpServletResponse response) {
        try {
            if (token != null && jwtUtil.validateToken(token)) {
                UUID userId = jwtUtil.extractUserId(token);
                log.info("[AUTH] User={} yêu cầu logout", userId);

                deviceRepository.findAllByUserId(userId).forEach(d -> {
                    d.setIsActive(false);
                    deviceRepository.save(d);
                });

                long ttlMs = jwtUtil.getExpirationFromToken(token) - System.currentTimeMillis();
                if (ttlMs > 0) tokenBlacklistService.blacklistToken(token, ttlMs);

                log.info("[AUTH] Logout thành công user={}, devices deactivated={}, blacklisted={}",
                        userId, true, ttlMs > 0);
            } else {
                log.warn("[AUTH] Logout với token không hợp lệ hoặc null");
            }
        } catch (Exception e) {
            log.error("[AUTH] Lỗi khi logout: {}", e.getMessage(), e);
        }

        ResponseCookie cookie = ResponseCookie.from("token", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/me")
    public ResponseEntity<?> checkStatus() {
        User user = currentUserUtil.getUser();
        AuthResponse.User dto = AuthResponse.User.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
        return ResponseEntity.ok(dto);
    }
}
