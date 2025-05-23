package com.swings.auth.service;

import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {
    String login(String username, String password, HttpServletResponse response);
}
