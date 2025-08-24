package com.boilerplate.common.util;
import com.boilerplate.config.security.CustomUserDetails;
import com.boilerplate.feature.user.User;
import com.boilerplate.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {
    @Autowired
    private UserRepository userRepository;

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) return null;
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return (CustomUserDetails) principal;
        }
        return null;
    }

    public static UUID getCurrentUserId() {
        CustomUserDetails user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    public static boolean isAdmin() {
        CustomUserDetails user = getCurrentUser();
        if (user == null) return false;
        return user.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
    }

    public User getUser() {
        UUID userId = getCurrentUserId();
        return userId != null ? userRepository.findById(userId).orElse(null) : null;
    }
}
