package com.example.authservice.mapper;

import org.mapstruct.Mapper;

import com.example.authservice.dto.response.UserResponseDto;
import com.example.authservice.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponseDto toResponseDto(User user);   
}
