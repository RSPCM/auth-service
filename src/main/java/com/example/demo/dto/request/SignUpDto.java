package com.example.demo.dto.request;

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
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String password;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    @Pattern(regexp = "^\\+\\d{2} \\d{3}-\\d{2}-\\d{2}$")
    private String phoneNumber;
}
