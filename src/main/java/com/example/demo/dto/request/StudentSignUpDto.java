package com.example.demo.dto.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentSignUpDto extends SignUpDto{
    private UUID groupId;
}
