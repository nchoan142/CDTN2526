package com.conghoan.sinhviencntt.dto;

import com.conghoan.sinhviencntt.entity.SinhVien;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginResponse - mapping từ SinhVien")
class LoginResponseTest {

    @Test
    @DisplayName("fromSinhVien() copy đầy đủ field và token")
    void fromSinhVien_shouldCopyAllFields() {
        SinhVien sv = SinhVien.builder()
                .maSinhVien("A35025")
                .ten("Nguyễn Công Hoàn")
                .lopChuyenNganh("CNTT-K32")
                .nganh("Công nghệ thông tin")
                .khoa("Khoa CNTT")
                .build();

        LoginResponse response = LoginResponse.fromSinhVien(sv, "jwt-token-abc");

        assertThat(response.getToken()).isEqualTo("jwt-token-abc");
        assertThat(response.getMaSinhVien()).isEqualTo("A35025");
        assertThat(response.getTen()).isEqualTo("Nguyễn Công Hoàn");
        assertThat(response.getLopChuyenNganh()).isEqualTo("CNTT-K32");
        assertThat(response.getNganh()).isEqualTo("Công nghệ thông tin");
        assertThat(response.getKhoa()).isEqualTo("Khoa CNTT");
    }
}
