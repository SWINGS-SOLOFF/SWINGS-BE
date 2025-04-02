package com.swings.user.service;

import com.swings.user.dto.UserPointDTO;

import java.util.List;

public interface UserPointService {
    List<UserPointDTO> findPointLogByUsername(String username);
    void chargePoint(String username, int amount, String description);
    void usePoint(String username, int amount, String description);
}
