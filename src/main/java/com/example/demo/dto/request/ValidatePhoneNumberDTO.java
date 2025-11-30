package com.example.demo.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ValidatePhoneNumberDTO {

    @Pattern(regexp = "^\\+\\d{2} \\d{3}-\\d{2}-\\d{2}$")
    private String phoneNumber;

    private Integer otp;
}
