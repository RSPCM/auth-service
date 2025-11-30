package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    UserResponseDto toResponseDto(Teacher teacher);
}
