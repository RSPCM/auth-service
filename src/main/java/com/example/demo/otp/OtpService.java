package com.example.demo.otp;

import com.example.demo.common.response.ApiMessageResponse;
import com.example.demo.dto.request.ValidatePhoneNumberDto;
import com.example.demo.exceptions.EntityNotFoundException;
import com.example.demo.exceptions.OtpEarlyResentException;
import com.example.demo.exceptions.OtpLimitExitedException;
import com.example.demo.notification.sms.SmsNotificationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpRepository otpRepository;
    private final Random random = new Random();
    private final String VERIFICATION_MASSAGE = "Your verification code is: %d%n";

    private final OtpProperties otpProperties;

    private final SmsNotificationService smsNotificationService;

    public ApiMessageResponse sendSms(ValidatePhoneNumberDto validatePhoneNumberDto) {
        String phoneNumber = validatePhoneNumberDto.getPhoneNumber();
        Optional<Otp> existingOtp = otpRepository.findByPhoneNumber(phoneNumber);

        if (validatePhoneNumberDto.getOtp() == null) {
            if (existingOtp.isPresent()) {
                return reTry(existingOtp.get());
            }
            Otp otp = sendSmsInternal(phoneNumber);
            otpRepository.save(otp);
            return new ApiMessageResponse("Sms was sent successfully");
        }

        if (existingOtp.isEmpty()) {
            throw new EntityNotFoundException("Otp", phoneNumber);
        }

        Otp otp = existingOtp.orElseThrow(() -> new RuntimeException("We didnt send any verification"));

        if (otp.getCode() == validatePhoneNumberDto.getOtp()) {
            otp.setVerified(true);
            otpRepository.save(otp);
            return new ApiMessageResponse("Otp was successfully verified");
        }

        return new ApiMessageResponse("Otp was incorrect");
    }

    private ApiMessageResponse reTry(Otp otp) {
        if (otp.getLastSendTime().plusSeconds(otpProperties.getRetryWaitTime()).isAfter(LocalDateTime.now())) {
            long resentTime = Duration.between(otp.getLastSendTime(), LocalDateTime.now()).getSeconds();
            throw new OtpEarlyResentException(otpProperties.getRetryWaitTime() - resentTime);
        }

        if (otp.getSendCount() >= otpProperties.getRetryCount()) {
            throw new OtpLimitExitedException(otp.getSendCount(),
                    otp.getCreatedAt().plusSeconds(otpProperties.getTimeToLive()));
        }

        otp = sendSmsInternal(otp);
        otpRepository.save(otp);

        return new ApiMessageResponse("Sms was re-send successfully");
    }

    private Otp sendSmsInternal(String phoneNumber) {
        int code = random.nextInt(10000, 99999);
        smsNotificationService.sendNotification(phoneNumber, VERIFICATION_MASSAGE.formatted(code));
        return new Otp(phoneNumber, code, 1, LocalDateTime.now(), LocalDateTime.now(), false);
    }

    private Otp sendSmsInternal(Otp otp) {
        int code = random.nextInt(10000, 99999);
        smsNotificationService.sendNotification(otp.getPhoneNumber(), VERIFICATION_MASSAGE.formatted(code));

        otp.setLastSendTime(LocalDateTime.now());
        otp.setSendCount(otp.getSendCount() + 1);
        otp.setCode(code);

        return otp;
    }
}
