package com.boilerplate.feature.auth;
import com.boilerplate.feature.user.User;
import com.boilerplate.feature.user.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    void logout(String token);
    UserDto register(RegisterRequest request);
    AuthResponse.User checkStatus();
}
