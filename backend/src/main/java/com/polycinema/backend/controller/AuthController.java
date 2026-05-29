package com.polycinema.backend.controller;

import com.polycinema.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
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
        return ResponseEntity.ok(
                service.register(
                        req.get("email"),
                        req.get("password"),
                        req.get("hoTen"),
                        req.get("soDienThoai")
                )
        );
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(
                service.login(
                        req.get("email"),
                        req.get("password")
                )
        );
    }

    // ================= VERIFY EMAIL =================
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(
                service.verifyEmail(
                        req.get("email"),
                        req.get("otp")
                )
        );
    }

    // ================= RESEND VERIFY OTP =================
    @PostMapping("/resend-verify")
    public ResponseEntity<?> resend(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(
                service.resendVerifyOtp(req.get("email"))
        );
    }

    // ================= FORGOT PASSWORD (SEND OTP) =================
    @PostMapping("/forgot")
    public ResponseEntity<?> forgot(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(
                service.sendForgotOtp(req.get("email"))
        );
    }

    // ================= RESET PASSWORD =================
    @PostMapping("/reset")
    public ResponseEntity<?> reset(@RequestBody Map<String, String> req) {
        return ResponseEntity.ok(
                service.resetPassword(
                        req.get("email"),
                        req.get("otp"),
                        req.get("newPassword")
                )
        );
    }
}