package com.swings.payment.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Base64;

@Getter
@Component
public class TossConfig {

    @Value("${toss.secret-key-file}")
    private Resource secretKeyFile;

    private String secretKey;

    @PostConstruct
    public void loadSecretKey() {
        try {
            // Resource에서 파일 경로를 읽어 문자열로 변환
            this.secretKey = Files.readString(secretKeyFile.getFile().toPath()).trim();

            System.out.println("Toss Secret Key Loaded Successfully");
            System.out.println("▶ Loaded Secret Key: " + secretKey);
            System.out.println("▶ Key Length: " + secretKey.length());
        } catch (Exception e) {
            throw new RuntimeException("toss-keys.txt 파일을 읽을 수 없습니다.", e);
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
