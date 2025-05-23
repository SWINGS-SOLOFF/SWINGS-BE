package com.swings.auth.service;

import java.util.Map;

public interface GoogleOAuthService {
    Map<String, Object> getUserInfo(String accessToken);
}
