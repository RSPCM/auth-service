package com.example.authservice.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authservice.dto.request.SignInDto;
import com.example.authservice.dto.request.SignUpDto;
import com.example.authservice.dto.request.StudentSignUpDto;
import com.example.authservice.dto.response.TokenResponseDto;
import com.example.authservice.dto.response.UserResponseDto;
import com.example.authservice.entity.User;
import com.example.authservice.entity.enums.Role;
import com.example.authservice.exceptions.AlreadyExistsException;
import com.example.authservice.exceptions.InvalidCredentialsException;
import com.example.authservice.exceptions.PhoneNumberNotVerifiedException;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.otp.Otp;
import com.example.authservice.otp.OtpRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final OtpRepository otpRepository;
    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UserResponseDto registerStudent(StudentSignUpDto signUpDto) {
        String phone = signUpDto.getPhoneNumber();

        // TODO: check group exists or not
        // TODO: even driven architecture: send requst to student service to
        // save student
        if (repository.findByPhoneNumber(phone).isPresent()) {
            throw new AlreadyExistsException("User", "phone number", phone);
        }

        String password = passwordEncoder.encode(signUpDto.getPassword());

        User user = new User(
                password,
                phone,
                Role.STUDENT);

        repository.save(user);

        return mapper.toResponseDto(user);
    }

    public UserResponseDto registerTeacher(SignUpDto signUpDto) {
        String phone = signUpDto.getPhoneNumber();

        isPhoneNumberVerified(phone);

        // TODO: even driven architecture: send requst to student service to
        // save teacher (rabbitmq)
        if (repository.findByPhoneNumber(phone).isPresent()) {
            throw new AlreadyExistsException("User", "phone number", phone);
        }

        String password = passwordEncoder.encode(signUpDto.getPassword());

        User user = new User(
                password,
                phone,
                Role.TEACHER);

        return mapper.toResponseDto(user);
    }

    private void isPhoneNumberVerified(String phoneNumber) {
        Otp otp = otpRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new PhoneNumberNotVerifiedException(phoneNumber));

        if (!otp.isVerified()) {
            throw new PhoneNumberNotVerifiedException(phoneNumber);
        }
    }

    public TokenResponseDto login(SignInDto signInDto) {
        User user = repository.findByPhoneNumber(signInDto.getPhoneNumber())
                .orElseThrow(() -> new InvalidCredentialsException(
                        signInDto.getPhoneNumber(),
                        signInDto.getPassword()));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Username or password is not correct");
        }

        return new TokenResponseDto(jwtService.generateToken(user));
    }
}
