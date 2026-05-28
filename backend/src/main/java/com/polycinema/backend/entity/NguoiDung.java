package com.polycinema.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "NguoiDung")
@Data
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String soDienThoai;
    private String matKhauHash;
    private String hoTen;

    private String vaiTro;
    private Boolean trangThai;
    private Boolean isEmailVerified;

    private String googleId;
    private String facebookId;
}