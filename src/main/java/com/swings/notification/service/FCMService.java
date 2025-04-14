package com.swings.notification.service;

public interface FCMService {
    void sendPush(String token, String title, String body);
}