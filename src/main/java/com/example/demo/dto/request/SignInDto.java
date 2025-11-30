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
    @NotBlank
    @Pattern(regexp = "^\\+\\d{2} \\d{3}-\\d{2}-\\d{2}$")
    private String phoneNumber;

    @NotBlank
    private String password;
}
