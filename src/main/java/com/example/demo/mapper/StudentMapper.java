package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    UserResponseDto toResponseDto(Student student);
}
