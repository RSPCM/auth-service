package com.example.authservice.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.example.authservice.entity.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private LocalDateTime createdAt;

    public User(String password, String phoneNumber,
            Role role) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
        createdAt = LocalDateTime.now();
    }

}
