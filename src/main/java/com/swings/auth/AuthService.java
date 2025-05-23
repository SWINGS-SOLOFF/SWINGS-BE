package com.swings.auth;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    String login(String username, String password, HttpServletResponse response);
}
