package com.example.demo.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.request.SignInDto;
import com.example.demo.dto.request.SignUpDto;
import com.example.demo.dto.request.StudentSignUpDto;
import com.example.demo.dto.response.TokenResponseDto;
import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.Student;
import com.example.demo.entity.Teacher;
import com.example.demo.entity.User;
import com.example.demo.entity.enums.Role;
import com.example.demo.exceptions.AlreadyExistsException;
import com.example.demo.exceptions.InvalidCredentialsException;
import com.example.demo.exceptions.PhoneNumberNotVerifiedException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.otp.Otp;
import com.example.demo.otp.OtpRepository;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.TeacherRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final OtpRepository otpRepository;

    /* private final StudentMapper studentMapper;
    private final TeacherMapper teacherMapper; */
    private final UserMapper mapper;
    
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UserResponseDto registerStudent(StudentSignUpDto signUpDto) {
        String phone = signUpDto.getPhoneNumber();

        if (teacherRepository.findByPhoneNumber(phone).isPresent()) {
            throw new AlreadyExistsException("User", "phone number", phone);
        }

        String password = passwordEncoder.encode(signUpDto.getPassword());

        User user = new User(
                signUpDto.getFirstName(),
                signUpDto.getLastName(),
                password,
                signUpDto.getBirthDate(),
                phone,
                Role.STUDENT
        );

        Student student = new Student();
        student.setGroupId(signUpDto.getGroupId());
        student.setUser(user);

        studentRepository.save(student);

        return mapper.toResponseDto(user);
    }

    public UserResponseDto registerTeacher(SignUpDto signUpDto) {
        String phone = signUpDto.getPhoneNumber();

        isPhoneNumberVerified(phone);

        if (teacherRepository.findByPhoneNumber(phone).isPresent()) {
            throw new AlreadyExistsException("User", "phone number", phone);
        }

        String password = passwordEncoder.encode(signUpDto.getPassword());

        User user = new User(
                signUpDto.getFirstName(),
                signUpDto.getLastName(),
                password,
                signUpDto.getBirthDate(),
                phone,
                Role.TEACHER
        );

        Teacher teacher = new Teacher();
        teacher.setUser(user);

        teacherRepository.save(teacher);

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
                        signInDto.getPassword()
                ));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Username or password is not correct");
        }

        return new TokenResponseDto(jwtService.generateToken(user));
    }
}
