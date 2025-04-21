package com.swings.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class GoogleOAuthService {

    private static final String USERINFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=";
    private final ObjectMapper objectMapper = new ObjectMapper();


    public Map<String, Object> getUserInfo(String accessToken) {
        try {
            URL url = new URL(USERINFO_URL + accessToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
            conn.setRequestProperty("Accept", "application/json");

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                log.error("[GoogleOAuthService] 유저 정보 요청 실패 - 응답 코드: {}", responseCode);
                return null;
            }

            InputStream responseStream = conn.getInputStream();
            JsonNode response = objectMapper.readTree(responseStream);

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", response.get("email").asText());
            userInfo.put("name", response.get("name").asText());

            log.info("[GoogleOAuthService] 사용자 정보 조회 성공: {}", userInfo);
            return userInfo;

        } catch (Exception e) {
            log.error("[GoogleOAuthService] access token으로 사용자 정보 조회 실패", e);
            return null;
        }
    }
}
