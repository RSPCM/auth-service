package com.example.authservice.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private String username;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String phoneNumber;
}
