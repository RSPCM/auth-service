package com.example.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.common.response.ApiMessageResponse;
import com.example.demo.dto.request.SignInDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.dto.request.StudentSignUpDto;
import com.example.demo.dto.request.ValidatePhoneNumberDto;
import com.example.demo.dto.response.TokenResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.otp.OtpService;
import com.example.demo.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
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
