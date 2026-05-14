package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.entity.TaiKhoanAdmin;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.TaiKhoanAdminRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/admin/giangvien")
public class AdminGiangVienController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final GiangVienRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final TaiKhoanAdminRepository adminRepo;

    public AdminGiangVienController(GiangVienRepository repo, PasswordEncoder passwordEncoder, TaiKhoanAdminRepository adminRepo) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.adminRepo = adminRepo;
    }

    @GetMapping
    public String list(@RequestParam(value = "search", required = false) String search, Model model) {
        List<GiangVien> list = repo.findAll();
        if (search != null && !search.trim().isEmpty()) {
            String keyword = search.trim().toLowerCase();
            list = list.stream()
                    .filter(gv -> gv.getMaGiangVien() != null && gv.getMaGiangVien().toLowerCase().contains(keyword))
                    .toList();
            model.addAttribute("search", search);
        }
        model.addAttribute("list", list);
        return "admin/giangvien-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("giangVien", new GiangVien());
        return "admin/giangvien-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("giangVien", repo.findById(id).orElseThrow());
        return "admin/giangvien-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute GiangVien giangVien,
                       @RequestParam(value = "rawPassword", required = false) String rawPassword,
                       RedirectAttributes ra,
                       Model model) {

        // Kiểm tra mã giảng viên đã tồn tại
        boolean isDuplicate = false;
        if (giangVien.getId() == null) {
            isDuplicate = repo.existsByMaGiangVien(giangVien.getMaGiangVien());
        } else {
            GiangVien existing = repo.findById(giangVien.getId()).orElse(null);
            if (existing != null && !existing.getMaGiangVien().equals(giangVien.getMaGiangVien())) {
                isDuplicate = repo.existsByMaGiangVien(giangVien.getMaGiangVien());
            }
        }

        if (isDuplicate) {
            model.addAttribute("error", "Mã giảng viên '" + giangVien.getMaGiangVien() + "' đã tồn tại trong hệ thống!");
            return "admin/giangvien-form"; // trả về chính trang đó
        }

        // Xử lý chức năng sửa giảng viên
        if (giangVien.getId() != null) {
            // Lấy username của admin
            GiangVien existing = repo.findById(giangVien.getId()).orElse(null);
            if (existing != null) {
                // Gán username cho nguoi_quan_ly
                giangVien.setAdminQuanLy(existing.getAdminQuanLy());
                
                // Xử lý mật khẩu
                if (rawPassword != null && !rawPassword.trim().isEmpty()) {
                    giangVien.setPassword(passwordEncoder.encode(rawPassword.trim())); // Thay bằng password mới, mã hóa trước khi lưu
                } else {
                    giangVien.setPassword(existing.getPassword()); // Giữ nguyên password cũ nếu bỏ trống
                }
            }
        } else {
            // Xử lý chức năng thêm mới
            // Set người quản lý mặc định là "admin"
            TaiKhoanAdmin admin = adminRepo.findByUsername("admin").orElse(null);
            if (admin != null) {
                giangVien.setAdminQuanLy(admin);
            }
            
            // Xử lý mật khẩu
            if (rawPassword != null && !rawPassword.trim().isEmpty()) {
                giangVien.setPassword(passwordEncoder.encode(rawPassword.trim())); // Thay bằng password mới, mã hóa trước khi lưu
            } else {
                giangVien.setPassword(passwordEncoder.encode(giangVien.getMaGiangVien())); // Giữ nguyên password cũ nếu bỏ trống
            }
        }

        repo.save(giangVien);
        ra.addFlashAttribute("success", "Lưu giảng viên thành công!");
        return "redirect:/admin/giangvien"; // "reredirect": điều hướng sang trang khác
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            repo.deleteById(id);
            ra.addFlashAttribute("success", "Xoá giảng viên thành công!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Không thể xoá giảng viên này do đang có dữ liệu ràng buộc.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Đã xảy ra lỗi không xác định khi xoá giảng viên.");
        }
        return "redirect:/admin/giangvien";
    }
}
