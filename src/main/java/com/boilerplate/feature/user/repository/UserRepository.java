package com.boilerplate.feature.user.repository;
import com.boilerplate.feature.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    //    @EntityGraph(attributePaths = {"userRoles", "userRoles.role"})
    Optional<User> findByUsername(String username);

    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String username, String email, Pageable pageable);
    Optional<User> findByEmail(String email);
}
