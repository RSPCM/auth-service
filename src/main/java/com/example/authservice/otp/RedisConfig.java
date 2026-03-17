package com.example.authservice.otp;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@Profile("!test")
@EnableRedisRepositories(basePackages = "com.example.authservice.otp")
public class RedisConfig {
}
