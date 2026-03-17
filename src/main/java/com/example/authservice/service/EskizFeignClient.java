package com.example.authservice.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.authservice.dto.request.SendSmsRequestDTO;
import com.example.authservice.dto.response.TokenRefreshResponseDTO;

@Component
@FeignClient(name = "eskizFeignClient", url = "https://notify.eskiz.uz/api")
public interface EskizFeignClient {

    @PatchMapping("auth/refresh")
    TokenRefreshResponseDTO refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer);

    @PostMapping("message/sms/send")
    String sendSms(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearer, SendSmsRequestDTO sendSmsRequestDTO);
}
