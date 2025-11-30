package com.example.demo.mapper;

import org.mapstruct.Mapper;

import com.example.demo.dto.response.UserResponseDto;
import com.example.demo.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponseDto(User user);
    
}
