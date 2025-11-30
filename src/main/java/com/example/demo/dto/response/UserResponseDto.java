package com.example.demo.dto.response;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private String phoneNumber;
}
