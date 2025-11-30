package com.example.demo.notification.sms;

public interface SmsNotificationService {

    void sendNotification(String phoneNumber, String message);
}
