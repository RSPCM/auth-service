package com.example.authservice.dto.message;

import java.time.LocalDate;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherProfileCreateMessage {
    private UUID authUserId;
    private String username;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
}

