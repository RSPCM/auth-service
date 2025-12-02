package com.example.authservice.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignUpDto {
    @NotBlank(message = "First name cannot be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name can only contain letters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name can only contain letters")
    private String lastName;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotNull(message = "Birth date cannot be blank")
    private LocalDate birthDate;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+\\d{2} \\d{3}-\\d{2}-\\d{2}$", message = "Phone number must match pattern: +XX XXX-XX-XX")
    private String phoneNumber;
}
