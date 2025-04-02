package com.swings.payment.config;

import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@Getter
@Component
public class TossConfig {

    private String secretKey;

    @PostConstruct
    public void loadSecretKey() {
        try {
            secretKey = Files.readString(Paths.get("toss-keys.txt")).trim();
            System.out.println("✅ Toss Secret Key Loaded Successfully");
        } catch (Exception e) {
            throw new RuntimeException("❌ toss-keys.txt 파일을 읽을 수 없습니다.", e);
        }
    }

    @Bean
    public WebClient tossWebClient() {
        String encodedKey = Base64.getEncoder()
                .encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        return WebClient.builder()
                .baseUrl("https://api.tosspayments.com/v1")
                .defaultHeader("Authorization", "Basic " + encodedKey)
                .build();
    }
}
