package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.dto.*;
import com.conghoan.sinhviencntt.entity.SinhVien;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import com.conghoan.sinhviencntt.security.JwtUtil;
import com.conghoan.sinhviencntt.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SinhVienRepository sinhVienRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthController(SinhVienRepository sinhVienRepo, JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder, EmailService emailService) {
        this.sinhVienRepo = sinhVienRepo;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        SinhVien sv = sinhVienRepo.findByMaSinhVien(request.getMaSinhVien()).orElse(null);
        if (sv == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mã sinh viên không tồn tại"));
        }
        if (sv.getPassword() == null || sv.getPassword().isEmpty()) {
            // Lần đầu đăng nhập, password = maSinhVien
            if (!request.getPassword().equals(sv.getMaSinhVien())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Sai mật khẩu"));
            }
        } else {
            if (!passwordEncoder.matches(request.getPassword(), sv.getPassword())) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Sai mật khẩu"));
            }
        }
        String token = jwtUtil.generateToken(sv.getMaSinhVien());
        return ResponseEntity.ok(ApiResponse.success(LoginResponse.fromSinhVien(sv, token)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@RequestBody LoginRequest request) {
        SinhVien sv = sinhVienRepo.findByMaSinhVien(request.getMaSinhVien()).orElse(null);
        if (sv == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mã sinh viên không tồn tại trong hệ thống"));
        }
        if (sv.getPassword() != null && !sv.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Tài khoản đã được đăng ký"));
        }
        sv.setPassword(passwordEncoder.encode(request.getPassword()));
        sinhVienRepo.save(sv);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công"));
    }

    @PostMapping("/tao-tai-khoan")
    public ResponseEntity<ApiResponse<String>> taoTaiKhoan(@RequestBody java.util.Map<String, String> body) {
        String msv = body.get("maSinhVien");
        if (msv == null || msv.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Vui lòng nhập mã sinh viên"));
        }
        SinhVien sv = sinhVienRepo.findByMaSinhVien(msv).orElse(null);
        if (sv == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Mã sinh viên không tồn tại trong hệ thống"));
        }
        if (sv.getPassword() != null && !sv.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Tài khoản đã được tạo trước đó. Liên hệ admin nếu quên mật khẩu."));
        }
        String email = sv.getEmail1();
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Sinh viên chưa có email trong hệ thống"));
        }

        // Tạo mật khẩu ngẫu nhiên 8 ký tự
        String password = generatePassword();
        sv.setPassword(passwordEncoder.encode(password));
        sinhVienRepo.save(sv);

        // Gửi email
        boolean sent = emailService.sendPassword(email, msv, password);
        if (sent) {
            return ResponseEntity.ok(ApiResponse.success("Mật khẩu đã gửi về email " + maskEmail(email)));
        } else {
            return ResponseEntity.ok(ApiResponse.success("Tài khoản đã tạo. Mật khẩu gửi email thất bại, liên hệ admin."));
        }
    }

    private String generatePassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 2) return email;
        return email.substring(0, 2) + "***" + email.substring(at);
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponse<String>> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody java.util.Map<String, String> body) {
        String token = authHeader.replace("Bearer ", "");
        String msv = jwtUtil.getMaSinhVienFromToken(token);
        SinhVien sv = sinhVienRepo.findByMaSinhVien(msv).orElse(null);
        if (sv == null) {
            return ResponseEntity.badRequest().body(ApiResponse.error("Không tìm thấy sinh viên"));
        }
        sv.setPassword(passwordEncoder.encode(body.get("newPassword")));
        sinhVienRepo.save(sv);
        return ResponseEntity.ok(ApiResponse.success("Đổi mật khẩu thành công"));
    }
}
