package com.polycinema.backend.controller;

import com.polycinema.backend.dto.*;
import com.polycinema.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService service;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest req) {

        String result = service.register(
                req.getEmail(),
                req.getPassword(),
                req.getHoTen(),
                req.getSoDienThoai()
        );

        if (!result.equals("Đăng ký thành công")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest req) {

        String result = service.login(req.getEmail(), req.getPassword());

        if (result.equals("Sai email hoặc mật khẩu")
                || result.equals("Email chưa xác thực")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot")
    public ResponseEntity<String> forgot(@RequestBody ForgotRequest req) {

        String result = service.sendOtp(req.getEmail());

        if (!result.equals("OTP đã gửi")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset")
    public ResponseEntity<String> reset(@RequestBody ResetPasswordRequest req) {

        String result = service.resetPassword(
                req.getEmail(),
                req.getOtp(),
                req.getNewPassword()
        );

        if (!result.equals("Đổi mật khẩu thành công")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
}