package com.conghoan.sinhviencntt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sinh_vien")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SinhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sinh_vien", unique = true, nullable = false)
    private String maSinhVien;

    @JsonIgnore
    private String password;
    private String ten;
    private String hoTenDem;
    private String ngaySinh;
    private String nhapHoc;
    private String lop;
    private String khoa;
    private String lopChuyenNganh;
    private String nganh;
    private String email1;
    private String email2;
    private String dienThoai1;
    private String dienThoai2;
    private String ghiChu;
    private String khoaNhapHoc;
    private Integer trangThai;
}
