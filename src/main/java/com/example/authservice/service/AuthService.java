package com.example.authservice.service;

import com.example.authservice.exceptions.ErrorCodes;
import com.example.authservice.exceptions.entity.ErrorMessageException;
import com.example.authservice.repository.RoleRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authservice.dto.request.SignInDto;
import com.example.authservice.dto.request.SignUpDto;
import com.example.authservice.dto.request.StudentSignUpDto;
import com.example.authservice.dto.response.TokenResponseDto;
import com.example.authservice.dto.response.UserResponseDto;
import com.example.authservice.entity.User;
import com.example.authservice.entity.Role;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.otp.Otp;
import com.example.authservice.otp.OtpRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final OtpRepository otpRepository;
    private final UserMapper mapper;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final ApplicationEventPublisher eventPublisher;

    public UserResponseDto registerStudent(StudentSignUpDto signUpDto) {
        String phone = signUpDto.getPhoneNumber();

        // TODO: check group exists or not
        // TODO: even driven architecture: send request to student service to save student
        if (repository.findByPhoneNumber(phone).isPresent()) {
            throw new ErrorMessageException("User with phone number: " + phone, ErrorCodes.AlreadyExists);
        }

        String password = passwordEncoder.encode(signUpDto.getPassword());
        Role studentRole = roleRepository.findByName("ROLE_STUDENT");

        User user = User.builder()
                .username(signUpDto.getUsername())
                .password(password)
                .phoneNumber(phone)
                .roles(Set.of(studentRole))
                .build();

        repository.save(user);

        return mapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto registerTeacher(SignUpDto signUpDto) {
        String phone = signUpDto.getPhoneNumber();
        String username = signUpDto.getUsername();

        isPhoneNumberVerified(phone);

        // TODO: even driven architecture: send requst to student service to
        // TODO: save teacher (rabbitmq)
        if (repository.findByPhoneNumber(phone).isPresent()) {
            throw new ErrorMessageException("User with phone number: %s already exists".formatted(phone), ErrorCodes.AlreadyExists);
        }

        String password = passwordEncoder.encode(signUpDto.getPassword());

        User user = new User(
                username,
                password,
                phone,
                Role.TEACHER);

        return mapper.toResponseDto(user);
    }

    private void isPhoneNumberVerified(String phoneNumber) {
        Otp otp = otpRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ErrorMessageException("Phone number is not verified", ErrorCodes.PhoneNotVerified));

        if (!otp.isVerified()) {
            throw new ErrorMessageException("Phone number is not verified", ErrorCodes.PhoneNotVerified);
        }
    }

    public TokenResponseDto login(SignInDto signInDto) {
        User user = repository.findByPhoneNumber(signInDto.getUsername())
                .orElseThrow(() -> new ErrorMessageException("Invalid username or password is incorrect", ErrorCodes.InvalidCredentials));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new ErrorMessageException("Invalid username or password is incorrect", ErrorCodes.InvalidCredentials);
        }

        return new TokenResponseDto(jwtService.generateToken(user));
    }
}
