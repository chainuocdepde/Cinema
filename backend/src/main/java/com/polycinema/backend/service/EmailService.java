package com.polycinema.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // ================= OTP =================
    public void sendOtp(String email, String otp) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("PolyCinema - OTP Reset Password");

            msg.setText(
                    "Xin chào,\n\n" +
                    "Mã OTP của bạn là: " + otp + "\n" +
                    "Hiệu lực: 15 phút\n\n" +
                    "Nếu bạn không yêu cầu, hãy bỏ qua email này."
            );

            mailSender.send(msg);

        } catch (Exception e) {
            System.out.println("❌ Send OTP email failed: " + e.getMessage());
        }
    }

    // ================= WELCOME =================
    public void sendWelcome(String email, String name) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("Chào mừng bạn đến PolyCinema");

            msg.setText(
                    "Xin chào " + name + ",\n\n" +
                    "Tài khoản của bạn đã được tạo thành công.\n" +
                    "Chúc bạn trải nghiệm vui vẻ!"
            );

            mailSender.send(msg);

        } catch (Exception e) {
            System.out.println("❌ Send welcome email failed: " + e.getMessage());
        }
    }
}