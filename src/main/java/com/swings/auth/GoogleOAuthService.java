package com.swings.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;

@Slf4j
@Service
public class GoogleOAuthService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleOAuthService() {
        String clientId = readClientIdFromFile();
        log.info("✅ [GoogleOAuthService] 불러온 clientId: {}", clientId); // ✅ 로그 출력 추가
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    private String readClientIdFromFile() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("/config/oauth-client-id.txt")))) {
            String line = reader.readLine();
            if (line == null || line.isBlank()) {
                throw new RuntimeException("Google Client ID 파일이 비어있습니다.");
            }
            return line.trim();
        } catch (Exception e) {
            log.error("❌ [GoogleOAuthService] Google Client ID 읽기 실패", e);
            throw new RuntimeException("Google Client ID 읽기 실패", e);
        }
    }

    public GoogleIdToken.Payload verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            return (idToken != null) ? idToken.getPayload() : null;
        } catch (Exception e) {
            log.error("❌ [GoogleOAuthService] ID 토큰 검증 실패", e);
            return null;
        }
    }
}
