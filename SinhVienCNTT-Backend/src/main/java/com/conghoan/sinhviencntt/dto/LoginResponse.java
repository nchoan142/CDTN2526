package com.conghoan.sinhviencntt.dto;

import com.conghoan.sinhviencntt.entity.SinhVien;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LoginResponse {
    private String token;
    private String maSinhVien;
    private String ten;
    private String hoTenDem;
    private String lopChuyenNganh;
    private String nganh;
    private String khoa;

    public static LoginResponse fromSinhVien(SinhVien sv, String token) {
        return LoginResponse.builder()
                .token(token)
                .maSinhVien(sv.getMaSinhVien())
                .ten(sv.getTen())
                .hoTenDem(sv.getHoTenDem())
                .lopChuyenNganh(sv.getLopChuyenNganh())
                .nganh(sv.getNganh())
                .khoa(sv.getKhoa())
                .build();
    }
}
