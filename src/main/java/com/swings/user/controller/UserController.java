package com.swings.user.controller;

import com.swings.user.dto.UserDTO;
import com.swings.user.dto.UserPointDTO;
import com.swings.user.entity.UserEntity;
import com.swings.user.repository.UserPointRepository;
import com.swings.user.repository.UserRepository;
import com.swings.user.service.UserPointService;
import com.swings.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserPointService userPointService;
    private final UserRepository userRepository;
    private final UserPointRepository userPointRepository;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO dto) {
        UserEntity newUser = userService.registerUser(dto);
        return ResponseEntity.ok("회원가입 성공! ID: " + newUser.getUserId());
    }

    // 아이디 중복 확인
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", userService.isUsernameExists(username));
        return ResponseEntity.ok(response);
    }

    // 🔄 특정 ID의 사용자 정보 조회 (원래는 UserEntity 반환 → 선택적으로 DTO로 변경 가능)
    @GetMapping("/{username}")
    public ResponseEntity<UserEntity> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    // ✅ 현재 로그인한 사용자 정보 조회 (Lazy 문제 방지용 DTO 반환)
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUserDto());
    }

    // 회원정보 수정
    @PatchMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UserDTO dto) {
        UserEntity updatedUser = userService.updateUser(username, dto);
        return ResponseEntity.ok("회원 정보 수정 완료! ID:" + updatedUser.getUserId());
    }

    // 회원 탈퇴
    @PostMapping("/delete/me")
    public ResponseEntity<String> deleteWithPassword(@RequestBody UserDTO dto) {
        userService.deleteCurrentUserWithPassword(dto.getPassword());
        return ResponseEntity.ok("회원 탈퇴가 완료되었습니다.");
    }

    // 내 포인트 사용내역 조회
    @GetMapping("/{username}/points")
    public ResponseEntity<List<UserPointDTO>> getMyPointHistory(@PathVariable String username) {
        return ResponseEntity.ok(userPointService.findPointLogByUsername(username));
    }

    // 포인트 충전
    @PostMapping("/{username}/points/charge")
    public ResponseEntity<String> chargePoints(
            @PathVariable String username,
            @RequestParam int amount,
            @RequestParam(defaultValue = "포인트 충전") String description) {
        userPointService.chargePoint(username, amount, description);
        return ResponseEntity.ok("포인트 충전 완료");
    }

    // 포인트 사용
    @PostMapping("/{username}/points/use")
    public ResponseEntity<String> usePoints(
            @PathVariable String username,
            @RequestParam int amount,
            @RequestParam(defaultValue = "포인트 사용") String description) {
        userPointService.usePoint(username, amount, description);
        return ResponseEntity.ok("포인트 사용 완료");
    }
}
