package com.boilerplate.feature.user;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
//    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    public void changePassword(UUID id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
//    @PreAuthorize("hasRole('ADMIN') or #userId == principal.id")
    public void verifyAccount(UUID id, String code) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) return;
        user.setIsVerified(true);
        userRepository.save(user);
    }
}
