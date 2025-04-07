package com.swings.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;

@Slf4j
@Service
public class GoogleOAuthService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleOAuthService(@Value("${google.oauth.client-id-file}") Resource clientIdFile) {
        String clientId = readClientIdFromFile(clientIdFile);
        log.info("✅ [GoogleOAuthService] 불러온 clientId: {}", clientId);

        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    private String readClientIdFromFile(Resource resource) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource.getInputStream()))) {

            String line = reader.readLine();
            if (line == null || line.isBlank()) {
                throw new RuntimeException("Google Client ID 파일이 비어있습니다.");
            }
            return line.trim();
        } catch (Exception e) {
            log.error("❌ [GoogleOAuthService] Client ID 파일 읽기 실패", e);
            throw new RuntimeException("Google Client ID 파일을 읽을 수 없습니다.", e);
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
