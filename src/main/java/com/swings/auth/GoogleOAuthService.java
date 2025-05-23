package com.swings.auth;

import java.util.Map;

public interface GoogleOAuthService {
    Map<String, Object> getUserInfo(String accessToken);
}
