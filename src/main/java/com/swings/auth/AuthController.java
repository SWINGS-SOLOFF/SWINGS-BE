package com.swings.auth;

import com.swings.security.JwtTokenProvider;
import com.swings.security.RefreshTokenEntity;
import com.swings.security.RefreshTokenRepository;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
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
    private final RefreshTokenRepository refreshTokenRepository;

    //로그인 API
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestDTO request, HttpServletResponse response) {
        String accessToken = authService.login(request.getUsername(), request.getPassword(), response);
        return ResponseEntity.ok(new TokenResponse(accessToken));  // Access Token만 반환
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = jwtTokenProvider.extractUsername(refreshToken);
        UserEntity user = userRepository.findByUsername(username).orElseThrow();

        // DB에 저장된 Refresh Token과 비교
        RefreshTokenEntity tokenEntity = refreshTokenRepository.findByUser(user).orElseThrow();
        if (!tokenEntity.getRefreshToken().equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 새로운 Access Token 발급
        String newAccessToken = jwtTokenProvider.generateToken(username, user.getRole());
        return ResponseEntity.ok(new TokenResponse(newAccessToken));
    }



    //구글 로그인 API
    @PostMapping("/oauth/google")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        String accessToken = request.get("accessToken");

        // accessToken으로 유저 정보 조회
        Map<String, Object> userInfo = googleOAuthService.getUserInfo(accessToken);
        if (userInfo == null || !userInfo.containsKey("email")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Access Token");
        }

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        Optional<UserEntity> userOpt = userRepository.findByEmail(email);

        if (userOpt.isPresent()) {
            // 기존 회원 → JWT 토큰 발급
            UserEntity user = userOpt.get();
            String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole());
            return ResponseEntity.ok(new TokenResponse(token));
        } else {
            // 신규 회원 → 회원가입 유도
            Map<String, Object> signupData = new HashMap<>();
            signupData.put("email", email);
            signupData.put("name", name);
            signupData.put("isNew", true);
            return ResponseEntity.ok(signupData);
        }
    }
}
