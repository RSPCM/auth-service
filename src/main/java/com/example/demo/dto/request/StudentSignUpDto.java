package com.example.demo.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSignUpDto extends SignUpDto {
    @NotNull
    private UUID groupId;
}
