package com.boilerplate.feature.auth;
import com.boilerplate.common.util.JwtUtil;
import com.boilerplate.feature.user.User;
import com.boilerplate.feature.user.dto.UserDto;
import com.boilerplate.feature.user.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        System.out.println(request.getUsername());
        AuthResponse authResponse = authService.login(request);
        String token = jwtUtil.createToken(authResponse.getUser().getUsername(), authResponse.getUser().getId(), authResponse.getUser().getRole());

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(authResponse.getExpiresIn() / 1000)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(authResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "token", required = false) String token) {
        authService.logout(token);

        // clear cookie
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true) // bật true khi chạy HTTPS
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logout successful");
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {
        UserDto dto = authService.register(request);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse.User> checkStatus() {
        return ResponseEntity.ok(authService.checkStatus());
    }

}
