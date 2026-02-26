package com.example.backend.controller.user;

import com.example.backend.dto.user.TokenResponse;
import com.example.backend.dto.user.UserLoginRequest;
import com.example.backend.dto.user.UserSignupRequest;
import com.example.backend.service.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관리 API (회원가입, 로그인, 인증 등)")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록하고 인증 이메일을 전송합니다.")
    public ResponseEntity<String> signup(@Valid @RequestBody UserSignupRequest request) {
        userService.signup(request);
        return ResponseEntity.ok("회원가입이 완료되었습니다. 이메일을 확인해주세요.");
    }

    @GetMapping("/verify")
    @Operation(summary = "이메일 인증", description = "이메일로 전송된 토큰을 확인하여 계정을 활성화합니다.")
    public ResponseEntity<String> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok("이메일 인증이 완료되었습니다.");
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 정보를 확인하고 JWT 토큰을 발급합니다.")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody UserLoginRequest request) {
        TokenResponse tokenResponse = userService.login(request);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "Refresh 토큰을 사용하여 새로운 Access 토큰을 발급합니다.")
    public ResponseEntity<TokenResponse> refresh(@RequestBody String refreshToken) {
        TokenResponse tokenResponse = userService.refresh(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/me")
    @Operation(summary = "현재 사용자 정보 조회", description = "현재 로그인된 사용자의 정보를 조회합니다.")
    public ResponseEntity<String> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("로그인되지 않은 사용자입니다.");
        }
        return ResponseEntity.ok("현재 로그인된 사용자: " + userDetails.getUsername());
    }
}
