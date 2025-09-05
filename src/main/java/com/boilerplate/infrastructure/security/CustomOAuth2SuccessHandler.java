package com.boilerplate.infrastructure.security;

import com.boilerplate.common.util.JwtUtil;
import com.boilerplate.feature.auth.service.RefreshTokenService;
import com.boilerplate.feature.user.entity.User;
import com.boilerplate.feature.user.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        String picture = oAuth2User.getAttribute("picture");

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setId(UUID.randomUUID());
            newUser.setEmail(email);
            newUser.setUsername(email); // dùng email làm username
            newUser.setPassword("{noop}" + UUID.randomUUID()); // hoặc BCrypt.encode(UUID.randomUUID().toString())
            newUser.setFullName(name != null ? name : email);
            newUser.setAvatarUrl(picture);
            newUser.setIsActive(true);
            newUser.setIsVerified(true);
            return userRepository.save(newUser);
        });

        Set<String> roles = user.getUserRoles() != null
                ? user.getUserRoles().stream().map(ur -> ur.getRole().getName()).collect(Collectors.toSet())
                : Set.of("USER");

        String accessToken = jwtUtil.createToken(user.getUsername(), user.getId(), roles);
        String refreshToken = jwtUtil.createRefreshToken(user.getId());
        refreshTokenService.handleLogin(user.getId(), refreshToken, jwtUtil.getRefreshExpiration());

        String redirectUrl = "http://localhost:8080/login.html"
                + "?token=" + URLEncoder.encode(accessToken, StandardCharsets.UTF_8)
                + "&refreshToken=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);

        log.info("[OAUTH2] Đăng nhập Google thành công: email={}, id={}", email, user.getId());
        response.sendRedirect(redirectUrl);
    }
}
