package com.example.authservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.authservice.common.response.ApiMessageResponse;
import com.example.authservice.dto.request.SignInDto;
import com.example.authservice.dto.request.SignUpDto;
import com.example.authservice.dto.request.StudentSignUpDto;
import com.example.authservice.dto.request.ValidatePhoneNumberDto;
import com.example.authservice.dto.response.TokenResponseDto;
import com.example.authservice.dto.response.UserResponseDto;
import com.example.authservice.otp.OtpService;
import com.example.authservice.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;

    @PostMapping("/validate")
    public ResponseEntity<ApiMessageResponse> validatePhoneNumber(
            @Valid @RequestBody ValidatePhoneNumberDto dto) {
        return ResponseEntity.ok(otpService.sendSms(dto));
    }

    @PostMapping("/teacher/register")
    public ResponseEntity<UserResponseDto> registerTeacher(
            @Valid @RequestBody SignUpDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerTeacher(dto));
    }

    @PostMapping("/student/register")
    public ResponseEntity<UserResponseDto> registerStudent(
            @Valid @RequestBody StudentSignUpDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerStudent(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(
            @Valid @RequestBody SignInDto dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

}
