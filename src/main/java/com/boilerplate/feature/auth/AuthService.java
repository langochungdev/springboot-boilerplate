package com.boilerplate.feature.auth;
import com.boilerplate.feature.user.User;
import com.boilerplate.feature.user.dto.UserDto;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<?> login(AuthRequest request, HttpServletResponse response);
    UserDto register(User user);
    ResponseEntity<?> logout(String token, HttpServletResponse response);
    ResponseEntity<?> checkStatus();
}
