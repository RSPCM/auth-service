package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class SignInDto {
    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^\\+\\d{2} \\d{3}-\\d{2}-\\d{2}$", message = "Phone number must match pattern: +XX XXX-XX-XX")
    private String phoneNumber;

    @NotBlank(message = "Password cannot be blank")
    private String password;
}
