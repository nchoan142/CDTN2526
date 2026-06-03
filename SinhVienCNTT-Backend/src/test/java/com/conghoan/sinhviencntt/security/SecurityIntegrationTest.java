package com.conghoan.sinhviencntt.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Kiểm thử bảo mật & Phân quyền")
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private GiangVienRepository giangVienRepo;

    @Test
    @DisplayName("Sinh viên dùng JWT truy cập trang Admin sẽ bị chặn và chuyển hướng về trang Login")
    void studentShouldBeForbiddenFromAccessingAdminEndpoints() throws Exception {
        // Tạo token của sinh viên
        String studentToken = jwtUtil.generateToken("A35025");
        
        // Sử dụng token của sinh viên để truy cập tới API admin/sinhvien
        mockMvc.perform(get("/admin/sinhvien")
                .header("Authorization", "Bearer " + studentToken))
                // Kỳ vọng trả về code 3xx
                .andExpect(status().is3xxRedirection())
                // Kỳ vọng chuyển hướng về trang login nếu là token của sinh viên
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "GV001")
    @DisplayName("Giảng viên không có role Admin/ Thư ký truy cập trang Sinh viên sẽ bị ẩn nút Thêm/Sửa/Xóa")
    void normalTeacherShouldNotHaveEditPermission() throws Exception {
        mockMvc.perform(get("/admin/sinhvien"))
                // Kỳ vọng trả về code 200 (Thành công)
                .andExpect(status().isOk())
                // Kỳ vọng trả về đúng giao diện trang sinh viên
                .andExpect(view().name("admin/sinhvien-list"))
                // Kỳ vọng canEdit trả về FALSE
                .andExpect(model().attribute("canEdit", false));
    }

    @Test
    @WithMockUser(username = "admin")
    @DisplayName("Admin truy cập trang Sinh viên sẽ được cấp quyền Thêm/Sửa/Xóa")
    void adminShouldHaveEditPermission() throws Exception {
        mockMvc.perform(get("/admin/sinhvien"))
                // Kỳ vọng trả về code 200 (Thành công)
                .andExpect(status().isOk())
                // Kỳ vọng trả về đúng giao diện trang sinh viên
                .andExpect(view().name("admin/sinhvien-list"))
                // Kỳ vọng canEdit trả về TRUE
                .andExpect(model().attribute("canEdit", true));
    }

    @Test
    @Transactional
    @WithMockUser(username = "GV_ADMIN")
    @DisplayName("Giảng viên có role Admin truy cập trang Sinh viên sẽ được cấp quyền Thêm/Sửa/Xóa")
    void authorizedTeacherRoleAdminShouldHaveEditPermission() throws Exception {
        // Tạo giảng viên có role là Admin và lưu vào database test
        GiangVien gvAdmin = GiangVien.builder()
                .maGiangVien("GV_ADMIN")
                .roleQuanTri(true)
                .build();
        giangVienRepo.save(gvAdmin);

        mockMvc.perform(get("/admin/sinhvien"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/sinhvien-list"))
                .andExpect(model().attribute("canEdit", true));
    }

    @Test
    @Transactional
    @WithMockUser(username = "GV_THUKY")
    @DisplayName("Giảng viên có role Thư ký truy cập trang Sinh viên sẽ được cấp quyền Thêm/Sửa/Xóa")
    void authorizedTeacherRoleThuKyShouldHaveEditPermission() throws Exception {
        GiangVien gvThuKy = GiangVien.builder()
                .maGiangVien("GV_THUKY")
                .roleThuKy(true)
                .build();
        giangVienRepo.save(gvThuKy);

        mockMvc.perform(get("/admin/sinhvien"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/sinhvien-list"))
                .andExpect(model().attribute("canEdit", true));
    }
}