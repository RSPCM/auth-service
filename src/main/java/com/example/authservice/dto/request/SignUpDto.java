package com.example.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignUpDto {
    @NotBlank(message = "Username cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9]{3,20}$", message = "Username can only contain letters and digits, and must be between 3 and 20 characters long")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+\\d{2} \\d{3}-\\d{2}-\\d{2}$", message = "Phone number must match pattern: +XX XXX-XX-XX")
    private String phoneNumber;
}
