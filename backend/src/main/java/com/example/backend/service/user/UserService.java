package com.example.backend.service.user;

import com.example.backend.dto.user.UserSignupRequest;
import com.example.backend.entity.user.User;
import com.example.backend.repository.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender mailSender;

    @Transactional
    public void signup(UserSignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        String token = UUID.randomUUID().toString();
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .email(request.getEmail())
                .isEmailVerified(false)
                .emailVerificationToken(token)
                .build();

        userRepository.save(user);
        sendVerificationEmail(user.getEmail(), token);
    }

    private void sendVerificationEmail(String email, String token) {
        String verificationUrl = "http://localhost:8080/api/users/verify?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("회원가입 이메일 인증");
        message.setText("가입을 완료하려면 다음 링크를 클릭하세요: " + verificationUrl);
        mailSender.send(message);
    }

    @Transactional
    public void verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("유효하지 않은 토큰입니다."));
        user.verifyEmail();
    }
}
