package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/giangvien")
public class AdminGiangVienController {

    private final GiangVienRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AdminGiangVienController(GiangVienRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", repo.findAll());
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
                       RedirectAttributes ra) {
        // Nếu nhập mật khẩu mới → mã hóa BCrypt
        if (rawPassword != null && !rawPassword.trim().isEmpty()) {
            giangVien.setPassword(passwordEncoder.encode(rawPassword.trim()));
        } else if (giangVien.getId() != null) {
            // Đang sửa, giữ nguyên password cũ
            GiangVien existing = repo.findById(giangVien.getId()).orElse(null);
            if (existing != null) {
                giangVien.setPassword(existing.getPassword());
            }
        } else {
            // Thêm mới, không nhập password → mặc định = mã GV
            giangVien.setPassword(passwordEncoder.encode(giangVien.getMaGiangVien()));
        }
        repo.save(giangVien);
        ra.addFlashAttribute("success", "Lưu giảng viên thành công!");
        return "redirect:/admin/giangvien";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá giảng viên thành công!");
        return "redirect:/admin/giangvien";
    }
}
