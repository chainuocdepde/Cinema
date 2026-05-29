package com.polycinema.backend.config;

import com.polycinema.backend.entity.NguoiDung;
import com.polycinema.backend.repository.NguoiDungRepository;
import com.polycinema.backend.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final NguoiDungRepository repo;
    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        String googleId = user.getAttribute("sub");

        NguoiDung nd = repo.findByEmail(email).orElse(null);

        // User chưa tồn tại -> tạo mới
        if (nd == null) {

            nd = new NguoiDung();
            nd.setEmail(email);
            nd.setHoTen(name);
            nd.setGoogleId(googleId);
            nd.setVaiTro("customer");
            nd.setTrangThai(true);
            nd.setIsEmailVerified(true);

            repo.save(nd);

        } else {

            // Có email rồi -> cập nhật GoogleId
            nd.setGoogleId(googleId);
            repo.save(nd);
        }

        // tạo JWT (email + role)
        String token = jwtUtil.generateToken(
                email,
                nd.getVaiTro()
        );

        // redirect về Vue
        response.sendRedirect(
                "http://localhost:5173/?token=" + token
        );
    }
}