package com.example.demo.common.notification.sms;

public interface SmsNotificationService {

    void sendNotification(String phoneNumber, String message);
}
