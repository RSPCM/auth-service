package com.example.authservice.mapper;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

import com.example.authservice.dto.response.UserResponseDto;
import com.example.authservice.entity.Role;
import com.example.authservice.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", expression = "java(mapRoleNames(user.getRoles()))")
    UserResponseDto toResponseDto(User user);

    default Set<String> mapRoleNames(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptySet();
        }

        return roles.stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }
}
