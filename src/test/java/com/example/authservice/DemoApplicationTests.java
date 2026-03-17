package com.example.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.example.authservice.repository.OtpRepository;
import org.mockito.Mockito;

@SpringBootTest
@ActiveProfiles("test")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@TestConfiguration
	static class OtpRepositoryConfig {
		@Bean
		OtpRepository otpRepository() {
			return Mockito.mock(OtpRepository.class);
		}
	}

}

