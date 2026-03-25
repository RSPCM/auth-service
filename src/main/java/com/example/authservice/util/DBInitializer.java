package com.example.authservice.util;

import com.example.authservice.entity.Role;
import com.example.authservice.enums.RoleName;
import com.example.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DBInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Arrays.stream(RoleName.values()).forEach(roleName -> {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        });
    }
}
