package com.swings.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.swings.security.JwtTokenProvider;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final GoogleOAuthService googleOAuthService;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * ✅ 일반 로그인 엔드포인트
     * username + password를 통해 JWT 토큰 반환
     */
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestDTO request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    /**
     * ✅ Google OAuth 로그인 엔드포인트
     * - 프론트에서 받은 idToken 검증
     * - DB에 사용자 존재 시: JWT 토큰 발급
     * - 사용자 미존재 시: 회원가입 안내 및 기본정보 반환
     */
    @PostMapping("/oauth/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        String idToken = request.get("idToken");

        GoogleIdToken.Payload payload = googleOAuthService.verify(idToken);
        if (payload == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID Token");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            // 기존 회원 → 토큰 발급
            UserEntity user = userOpt.get();
            String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
            return ResponseEntity.ok(new TokenResponse(token));
        } else {
            // 신규 회원 → 회원가입 유도
            Map<String, Object> signupData = new HashMap<>();
            signupData.put("email", email);
            signupData.put("name", name);
            signupData.put("isNew", true);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(signupData);
        }
    }
}
