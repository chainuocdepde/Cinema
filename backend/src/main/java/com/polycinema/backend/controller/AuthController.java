package com.polycinema.backend.controller;

import com.polycinema.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService service;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> req) {

        String result = service.register(
                req.get("email"),
                req.get("password"),
                req.get("hoTen"),
                req.get("soDienThoai")
        );

        if (!result.startsWith("Đăng ký thành công")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {

        String result = service.login(
                req.get("email"),
                req.get("password")
        );

        switch (result) {

            case "Sai email hoặc mật khẩu":
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(result);

            case "Email chưa xác thực":
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(result);

            case "Tài khoản đã bị khóa":
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(result);

            default:
                return ResponseEntity.ok(result);
        }
    }

    // ================= VERIFY EMAIL =================
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> req) {

        String result = service.verifyEmail(
                req.get("email"),
                req.get("otp")
        );

        if (!result.equals("Xác thực email thành công")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ================= RESEND VERIFY OTP =================
    @PostMapping("/resend-verify")
    public ResponseEntity<?> resend(@RequestBody Map<String, String> req) {

        String result = service.resendVerifyOtp(
                req.get("email")
        );

        if (!result.equals("OTP đã gửi")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody Map<String, String> req) {

        String result = service.sendForgotOtp(
                req.get("email")
        );

        if (!result.equals("OTP đã gửi")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody Map<String, String> req) {

        String result = service.resetPassword(
                req.get("email"),
                req.get("otp"),
                req.get("newPassword")
        );

        if (!result.equals("Đổi mật khẩu thành công")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
}