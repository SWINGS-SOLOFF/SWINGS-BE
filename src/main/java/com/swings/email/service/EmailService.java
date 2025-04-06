package com.swings.email.service;

import com.swings.user.entity.UserEntity;

public interface EmailService {

    void sendEmailVerification(UserEntity user);
    void sendTemporaryPassword(UserEntity user, String tempPassword);

}
