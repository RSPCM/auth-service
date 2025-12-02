package com.example.authservice.notification.sms;

public interface SmsNotificationService {

    void sendNotification(String phoneNumber, String message);
}
