package com.swings.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController //Controller+ResponseBody
@RequestMapping("/auth")
@RequiredArgsConstructor //final이 붙은 필드를 자동으로 생성자 주입
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequestDTO request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(new TokenResponse(token));
    }
}
