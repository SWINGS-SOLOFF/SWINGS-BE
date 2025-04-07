package com.swings.chat.service;

import com.swings.chat.dto.UserSelectDTO;

public interface UserRecommendService {
    UserSelectDTO getRandomUser(String username);
    UserSelectDTO getNextRandomUser(String username, String excludedUsername);
}
