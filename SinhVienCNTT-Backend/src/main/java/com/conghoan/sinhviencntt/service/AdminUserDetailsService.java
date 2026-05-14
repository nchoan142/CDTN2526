package com.conghoan.sinhviencntt.service;

import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.entity.TaiKhoanAdmin;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.TaiKhoanAdminRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class AdminUserDetailsService implements UserDetailsService {

    private final TaiKhoanAdminRepository adminRepo;
    private final GiangVienRepository giangVienRepo;

    public AdminUserDetailsService(TaiKhoanAdminRepository adminRepo, GiangVienRepository giangVienRepo) {
        this.adminRepo = adminRepo;
        this.giangVienRepo = giangVienRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Thử tìm trong bảng tai_khoan_admin trước
        Optional<TaiKhoanAdmin> adminOpt = adminRepo.findByUsername(username);
        if (adminOpt.isPresent()) {
            TaiKhoanAdmin admin = adminOpt.get();
            return new User(admin.getUsername(), admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + admin.getRole())));
        }

        // Nếu không tìm thấy, thử đăng nhập bằng mã giảng viên (CVHT)
        Optional<GiangVien> gvOpt = giangVienRepo.findByMaGiangVien(username);
        if (gvOpt.isPresent()) {
            GiangVien gv = gvOpt.get();
            String password = gv.getPassword();
            if (password == null || password.isEmpty()) {
                throw new UsernameNotFoundException("Giảng viên chưa có mật khẩu: " + username);
            }
            return new User(gv.getMaGiangVien(), password,
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_CVHT")));
        }

        throw new UsernameNotFoundException("Không tìm thấy tài khoản: " + username);
    }
}
