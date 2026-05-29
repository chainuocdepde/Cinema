package com.polycinema.backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "NguoiDung")
@Data
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Email")
    private String email;

    @Column(name = "SoDienThoai")
    private String soDienThoai;

    @Column(name = "MatKhauHash")
    private String matKhauHash;

    @Column(name = "HoTen")
    private String hoTen;

    @Column(name = "VaiTro")
    private String vaiTro;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @Column(name = "IsEmailVerified")
    private Boolean isEmailVerified;

    @Column(name = "GoogleId")
    private String googleId;

    @Column(name = "FacebookId")
    private String facebookId;
}