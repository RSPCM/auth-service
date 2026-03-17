package com.example.authservice.service;

public interface SmsNotificationService {

    void sendNotification(String phoneNumber, String message);
}
