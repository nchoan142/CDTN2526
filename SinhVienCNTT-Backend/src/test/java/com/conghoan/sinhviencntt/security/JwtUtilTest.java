package com.conghoan.sinhviencntt.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import com.conghoan.sinhviencntt.entity.SinhVien;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("JwtUtil - xác thực token JWT")
class JwtUtilTest {

    private JwtUtil jwtUtil;
    
    @Autowired
    private SinhVienRepository sinhVienRepo;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "jwtSecret",
                "UnitTestSecretKeyForJWTTokenGenerationMustBeLongEnough256BitsForTest");
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", 3_600_000L);
    }

    @Test
    @DisplayName("Tạo token cho sinh viên")
    void createToken() {
        List<SinhVien> list = sinhVienRepo.findAll();
        String msv = list.get(0).getMaSinhVien();
        String token = jwtUtil.generateToken(msv);
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    @DisplayName("Lấy mã sinh viên từ token")
    void getMaSinhVienFromToken() {
        List<SinhVien> list = sinhVienRepo.findAll();
        String msv = list.get(1000).getMaSinhVien();
        String token = jwtUtil.generateToken(msv);
        String maSinhVien = jwtUtil.getMaSinhVienFromToken(token);
        assertThat(maSinhVien).isEqualTo(msv);
    }

    @Test
    @DisplayName("validateToken() trả true với token tạo từ SV thật")
    void validateToken_shouldReturnTrueForValidTokenFromDB() {
        List<SinhVien> list = sinhVienRepo.findAll();
        assertThat(list).isNotEmpty();
        String realMsv = list.get(0).getMaSinhVien();

        String token = jwtUtil.generateToken(realMsv);

        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("validateToken() trả false với token không hợp lệ")
    void validateToken_shouldReturnFalseForInvalidToken() {
        assertThat(jwtUtil.validateToken("token-khong-hop-le")).isFalse();
        assertThat(jwtUtil.validateToken("aaa.bbb.ccc")).isFalse();
    }

    @Test
    @DisplayName("validateToken() trả false khi token bị hết hạn")
    void validateToken_shouldReturnFalseWhenExpiredFromDB() {
        List<SinhVien> list = sinhVienRepo.findAll();
        String msv = list.get(0).getMaSinhVien();
        ReflectionTestUtils.setField(jwtUtil, "jwtExpirationMs", -1000L);
        String expiredToken = jwtUtil.generateToken(msv);
        assertThat(jwtUtil.validateToken(expiredToken)).isFalse();
    }
}
