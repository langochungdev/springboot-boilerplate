package com.boilerplate.feature.auth.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password phải >= 6 ký tự")
    private String password;

    @NotBlank
    private String fullName;
}
