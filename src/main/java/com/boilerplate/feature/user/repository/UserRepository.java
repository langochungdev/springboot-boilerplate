package com.boilerplate.feature.user.repository;

import com.boilerplate.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}

//save(e), findAll(), findById(id), delete(e),

//  @Query("select u from UserError.java u where u.email = :email")
//  Optional<UserError.java> findByEmail(@Param("email") String email);