package com.example.authservice.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "token")
@Getter
@Setter
public class TokenProperties {
    private long expiration;
    private String token;
}
