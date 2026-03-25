package com.example.authservice.service;

import com.example.authservice.dto.message.TeacherProfileCreateMessage;
import com.example.authservice.exception.ErrorCodes;
import com.example.authservice.exception.entity.ErrorMessageException;
import com.example.authservice.enums.RoleName;
import com.example.authservice.repository.RoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.authservice.dto.request.SignInDto;
import com.example.authservice.dto.request.StudentSignUpDto;
import com.example.authservice.dto.request.TeacherSignUpDto;
import com.example.authservice.dto.response.TokenResponseDto;
import com.example.authservice.dto.response.UserResponseDto;
import com.example.authservice.entity.User;
import com.example.authservice.entity.Role;
import com.example.authservice.mapper.UserMapper;
import com.example.authservice.entity.Otp;
import com.example.authservice.repository.OtpRepository;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

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
    private final OutboxService outboxService;


    public UserResponseDto registerStudent(StudentSignUpDto signUpDto) {
        String phone = signUpDto.getPhoneNumber();
        String username = signUpDto.getUsername();

        // TODO: check group exists or not
        // TODO: even driven architecture: send request to student service to save student
        if (repository.findByPhoneNumber(phone).isPresent()) {
            throw new ErrorMessageException("This phone number: %s is already registered".formatted(phone), ErrorCodes.AlreadyExists);
        }

        if (repository.findByUsername(username).isPresent()) {
            throw new ErrorMessageException("This username: %s is already taken".formatted(username), ErrorCodes.AlreadyExists);
        }

        String password = passwordEncoder.encode(signUpDto.getPassword());
        Role studentRole = roleRepository.findByName(RoleName.ROLE_STUDENT)
                .orElseThrow(() -> new ErrorMessageException("Role STUDENT not found", ErrorCodes.InternalServerError));

        User user = User.builder()
                .username(username)
                .phoneNumber(signUpDto.getPhoneNumber())
                .password(password)
                .roles(Set.of(studentRole))
                .build();

        repository.save(user);

        return mapper.toResponseDto(user);
    }

    @Transactional
    public UserResponseDto registerTeacher(TeacherSignUpDto signUpDto) {
        String phone = signUpDto.getPhoneNumber();
        String username = signUpDto.getUsername();

        isPhoneNumberVerified(phone);

        // TODO: even driven architecture: send request to student service to save teacher
        if (repository.findByPhoneNumber(phone).isPresent()) {
            throw new ErrorMessageException("This phone number: %s is already registered".formatted(phone), ErrorCodes.AlreadyExists);
        }

        if (repository.findByUsername(username).isPresent()) {
            throw new ErrorMessageException("This username: %s is already taken".formatted(username), ErrorCodes.AlreadyExists);
        }

        String password = passwordEncoder.encode(signUpDto.getPassword());

        Role teacherRole = roleRepository.findByName(RoleName.ROLE_TEACHER)
                .orElseThrow(() -> new ErrorMessageException("Role TEACHER not found", ErrorCodes.InternalServerError));

        User user = User.builder()
                .username(username)
                .phoneNumber(phone)
                .password(password)
                .roles(Set.of(teacherRole))
                .build();
        User savedUser = repository.save(user);

        TeacherProfileCreateMessage teacherProfileCreateMessage = TeacherProfileCreateMessage.builder()
                .authUserId(savedUser.getId())
                .username(savedUser.getUsername())
                .phoneNumber(savedUser.getPhoneNumber())
                .firstName(signUpDto.getFirstName())
                .lastName(signUpDto.getLastName())
                .birthDate(signUpDto.getBirthDate())
                .build();

        outboxService.enqueueTeacherProfileCreate(teacherProfileCreateMessage);

        return mapper.toResponseDto(savedUser);
    }

    private void isPhoneNumberVerified(String phoneNumber) {
        Otp otp = otpRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new ErrorMessageException("Phone number is not verified", ErrorCodes.PhoneNotVerified));

        if (!otp.isVerified()) {
            throw new ErrorMessageException("Phone number is not verified", ErrorCodes.PhoneNotVerified);
        }
    }

    public TokenResponseDto login(SignInDto signInDto) {
        User user = repository.findByUsername(signInDto.getUsername())
                .orElseThrow(() -> new ErrorMessageException("Invalid username or password is incorrect", ErrorCodes.InvalidCredentials));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new ErrorMessageException("Invalid username or password is incorrect", ErrorCodes.InvalidCredentials);
        }

        return new TokenResponseDto(jwtService.generateToken(user));
    }
}
