package com.polycinema.backend.service;

import com.polycinema.backend.entity.NguoiDung;
import com.polycinema.backend.repository.NguoiDungRepository;
import com.polycinema.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final NguoiDungRepository repo;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ================= OTP STORE =================
    private final Map<String, OtpData> otpStore = new HashMap<>();
    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    static class OtpData {
        String code;
        long expireAt;

        OtpData(String code, long expireAt) {
            this.code = code;
            this.expireAt = expireAt;
        }
    }

    // ================= VALIDATION =================
    private String validateEmail(String email) {
        if (email == null || email.isBlank()) return "Email không được để trống";

        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(regex, email)) return "Email không đúng định dạng";

        return null;
    }

    private String validatePhone(String phone) {
        if (phone == null || phone.isBlank()) return "Số điện thoại không được để trống";

        if (!phone.matches("^(03|05|07|08|09)\\d{8}$"))
            return "Số điện thoại không hợp lệ";

        return null;
    }

    private String validateName(String name) {
        if (name == null || name.isBlank()) return "Họ tên không được để trống";

        if (!name.matches("^[\\p{L} ]+$"))
            return "Họ tên không hợp lệ";

        return null;
    }

    // ================= REGISTER =================
    public String register(String email, String password, String hoTen, String soDienThoai) {

        email = email.trim().toLowerCase();

        String emailError = validateEmail(email);
        if (emailError != null) return emailError;

        String phoneError = validatePhone(soDienThoai);
        if (phoneError != null) return phoneError;

        String nameError = validateName(hoTen);
        if (nameError != null) return nameError;

        if (repo.findByEmail(email).isPresent())
            return "Email đã tồn tại";

        if (repo.findBySoDienThoai(soDienThoai).isPresent())
            return "Số điện thoại đã tồn tại";

        NguoiDung u = new NguoiDung();
        u.setEmail(email);
        u.setMatKhauHash(encoder.encode(password));
        u.setHoTen(hoTen);
        u.setSoDienThoai(soDienThoai);
        u.setVaiTro("customer");
        u.setTrangThai(true);
        u.setIsEmailVerified(false);

        repo.save(u);

        // gửi OTP verify
        sendVerifyOtp(email);

        return "Đăng ký thành công. Vui lòng xác thực email";
    }

    // ================= LOGIN =================
    public String login(String email, String password) {

        email = email.trim().toLowerCase();

        NguoiDung u = repo.findByEmail(email).orElse(null);

        if (u == null || !encoder.matches(password, u.getMatKhauHash()))
            return "Sai email hoặc mật khẩu";

        if (!Boolean.TRUE.equals(u.getIsEmailVerified()))
            return "Email chưa xác thực";

        return jwtUtil.generateToken(email, u.getVaiTro());
    }

    // ================= SEND VERIFY OTP =================
    public String sendVerifyOtp(String email) {

        email = email.trim().toLowerCase();

        if (repo.findByEmail(email).isEmpty())
            return "Email không tồn tại";

        otpStore.remove(email);

        String otp = String.valueOf(100000 + new Random().nextInt(900000));

        otpStore.put(email, new OtpData(
                otp,
                System.currentTimeMillis() + EXPIRE_TIME
        ));

        try {
            emailService.sendOtp(email, otp);
        } catch (Exception e) {
            e.printStackTrace();
            return "Không gửi được OTP";
        }

        return "OTP đã gửi";
    }

    // ================= VERIFY EMAIL =================
    public String verifyEmail(String email, String otp) {

        email = email.trim().toLowerCase();

        OtpData data = otpStore.get(email);

        if (data == null) return "Không tìm thấy OTP";

        if (System.currentTimeMillis() > data.expireAt) {
            otpStore.remove(email);
            return "OTP hết hạn";
        }

        if (!data.code.equals(otp))
            return "Sai OTP";

        NguoiDung u = repo.findByEmail(email).orElse(null);
        if (u == null) return "Không tồn tại user";

        u.setIsEmailVerified(true);
        repo.save(u);

        otpStore.remove(email);

        return "Xác thực email thành công";
    }

    // ================= RESEND OTP =================
    public String resendVerifyOtp(String email) {
        return sendVerifyOtp(email);
    }

    // ================= FORGOT PASSWORD =================
    public String sendForgotOtp(String email) {
        return sendVerifyOtp(email);
    }

    // ================= RESET PASSWORD =================
    public String resetPassword(String email, String otp, String newPass) {

        email = email.trim().toLowerCase();

        OtpData data = otpStore.get(email);

        if (data == null) return "Không tìm thấy OTP";

        if (System.currentTimeMillis() > data.expireAt) {
            otpStore.remove(email);
            return "OTP hết hạn";
        }

        if (!data.code.equals(otp))
            return "Sai OTP";

        NguoiDung u = repo.findByEmail(email).orElse(null);

        if (u == null) return "Không tồn tại user";

        u.setMatKhauHash(encoder.encode(newPass));
        repo.save(u);

        otpStore.remove(email);

        return "Đổi mật khẩu thành công";
    }
}