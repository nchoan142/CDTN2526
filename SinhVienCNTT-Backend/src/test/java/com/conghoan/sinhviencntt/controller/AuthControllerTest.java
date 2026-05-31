package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.dto.ApiResponse;
import com.conghoan.sinhviencntt.dto.LoginRequest;
import com.conghoan.sinhviencntt.dto.LoginResponse;
import com.conghoan.sinhviencntt.entity.SinhVien;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import com.conghoan.sinhviencntt.security.JwtUtil;
import com.conghoan.sinhviencntt.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("AuthController - đăng nhập/ đổi mật khẩu")
class AuthControllerTest {

    @Mock SinhVienRepository sinhVienRepo;
    @Mock JwtUtil jwtUtil;
    @Mock PasswordEncoder passwordEncoder;
    @Mock EmailService emailService;
    @InjectMocks AuthController controller;

    private SinhVien sinhVien;

    @BeforeEach
    void setUp() {
        sinhVien = SinhVien.builder()
                .id(1L)
                .maSinhVien("A38200")
                .ten("Đăng")
                .hoTenDem("Trịnh Hoàng")
                .lopChuyenNganh("CNTT-K38")
                .nganh("Công nghệ thông tin")
                .khoa("Khoa CNTT")
                .email1("dang@thanglong.edu.vn")
                .password("$2a$10$encoded-password-hash")
                .build();
    }

    @Nested
    @DisplayName("POST /api/auth/login")
    class Login {

        @Test
        @DisplayName("trả 400 khi mã sinh viên không tồn tại")
        void shouldReturn400WhenMaSinhVienNotFound() {
            when(sinhVienRepo.findByMaSinhVien("XXX")).thenReturn(Optional.empty());

            LoginRequest req = new LoginRequest("XXX", "123");
            ResponseEntity<ApiResponse<LoginResponse>> resp = controller.login(req);

            assertThat(resp.getStatusCode().value()).isEqualTo(400);
            assertThat(resp.getBody().getCode()).isEqualTo(1);
            assertThat(resp.getBody().getMessage()).contains("không tồn tại");
        }

        @Test
        @DisplayName("trả 400 khi mật khẩu sai (đã từng đặt password)")
        void shouldReturn400WhenWrongPassword() {
            when(sinhVienRepo.findByMaSinhVien("A38200")).thenReturn(Optional.of(sinhVien));
            when(passwordEncoder.matches("wrong", sinhVien.getPassword())).thenReturn(false);

            LoginRequest req = new LoginRequest("A38200", "wrong");
            ResponseEntity<ApiResponse<LoginResponse>> resp = controller.login(req);

            assertThat(resp.getStatusCode().value()).isEqualTo(400);
            assertThat(resp.getBody().getMessage()).contains("Sai mật khẩu");
        }

        @Test
        @DisplayName("trả 200 + token khi đăng nhập đúng (đã có password)")
        void shouldReturn200WithTokenOnSuccessfulLogin() {
            when(sinhVienRepo.findByMaSinhVien("A38200")).thenReturn(Optional.of(sinhVien));
            when(passwordEncoder.matches("correct", sinhVien.getPassword())).thenReturn(true);
            when(jwtUtil.generateToken("A38200")).thenReturn("jwt-abc");

            LoginRequest req = new LoginRequest("A38200", "correct");
            ResponseEntity<ApiResponse<LoginResponse>> resp = controller.login(req);

            assertThat(resp.getStatusCode().value()).isEqualTo(200);
            assertThat(resp.getBody().getCode()).isZero();
            assertThat(resp.getBody().getData().getToken()).isEqualTo("jwt-abc");
            assertThat(resp.getBody().getData().getMaSinhVien()).isEqualTo("A38200");
        }

        @Test
        @DisplayName("đăng nhập lần đầu (chưa có password): chấp nhận khi password = maSinhVien")
        void shouldAcceptMaSinhVienAsDefaultPasswordOnFirstLogin() {
            sinhVien.setPassword(null);
            when(sinhVienRepo.findByMaSinhVien("A38200")).thenReturn(Optional.of(sinhVien));
            when(jwtUtil.generateToken("A38200")).thenReturn("jwt-first");

            LoginRequest req = new LoginRequest("A38200", "A38200");
            ResponseEntity<ApiResponse<LoginResponse>> resp = controller.login(req);

            assertThat(resp.getStatusCode().value()).isEqualTo(200);
            assertThat(resp.getBody().getData().getToken()).isEqualTo("jwt-first");
            verify(passwordEncoder, never()).matches(any(), any());
        }

        @Test
        @DisplayName("đăng nhập lần đầu: trả 400 khi nhập sai mã làm mật khẩu")
        void shouldReturn400OnFirstLoginWrongPassword() {
            sinhVien.setPassword("");
            when(sinhVienRepo.findByMaSinhVien("A38200")).thenReturn(Optional.of(sinhVien));

            LoginRequest req = new LoginRequest("A38200", "1111");
            ResponseEntity<ApiResponse<LoginResponse>> resp = controller.login(req);

            assertThat(resp.getStatusCode().value()).isEqualTo(400);
            assertThat(resp.getBody().getMessage()).contains("Sai mật khẩu");
        }
    }

    @Nested
    @DisplayName("POST /api/auth/change-password")
    class ChangePassword {

        @Test
        @DisplayName("đổi mật khẩu thành công khi token hợp lệ")
        void shouldChangePasswordSuccessfully() {
            when(jwtUtil.getMaSinhVienFromToken("token-abc")).thenReturn("A38200");
            when(sinhVienRepo.findByMaSinhVien("A38200")).thenReturn(Optional.of(sinhVien));
            when(passwordEncoder.encode("newpwd")).thenReturn("encoded-new");

            ResponseEntity<ApiResponse<String>> resp = controller.changePassword(
                    "Bearer token-abc",
                    Map.of("newPassword", "newpwd"));

            assertThat(resp.getStatusCode().value()).isEqualTo(200);
            assertThat(sinhVien.getPassword()).isEqualTo("encoded-new");
            verify(sinhVienRepo).save(sinhVien);
        }

        @Test
        @DisplayName("trả 400 khi không tìm thấy sinh viên từ token")
        void shouldReturn400WhenStudentNotFoundFromToken() {
            when(jwtUtil.getMaSinhVienFromToken("token-abc")).thenReturn("XXX");
            when(sinhVienRepo.findByMaSinhVien("XXX")).thenReturn(Optional.empty());

            ResponseEntity<ApiResponse<String>> resp = controller.changePassword(
                    "Bearer token-abc",
                    Map.of("newPassword", "newpwd"));

            assertThat(resp.getStatusCode().value()).isEqualTo(400);
            assertThat(resp.getBody().getMessage()).contains("Không tìm thấy");
        }
    }
}
