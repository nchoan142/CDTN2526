package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.SinhVien;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/sinhvien")
public class AdminSinhVienController {

    private final SinhVienRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AdminSinhVienController(SinhVienRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
            model.addAttribute("list", repo.findByTenContainingIgnoreCaseOrMaSinhVienContainingIgnoreCase(search, search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("list", repo.findAll());
        }
        return "admin/sinhvien-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("sinhVien", new SinhVien());
        return "admin/sinhvien-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("sinhVien", repo.findById(id).orElseThrow());
        return "admin/sinhvien-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute SinhVien sinhVien, RedirectAttributes ra) {
        if (sinhVien.getId() != null) {
            // Trường hợp: Cập nhật (Sửa sinh viên)
            SinhVien existingSinhVien = repo.findById(sinhVien.getId()).orElse(null);
            if (existingSinhVien != null) {
                if (sinhVien.getPassword() == null || sinhVien.getPassword().trim().isEmpty()) {
                    // Bỏ trống mật khẩu -> Lấy lại mật khẩu cũ lưu vào
                    sinhVien.setPassword(existingSinhVien.getPassword());
                } else {
                    // Có nhập mật khẩu mới -> Mã hoá trước khi lưu
                    sinhVien.setPassword(passwordEncoder.encode(sinhVien.getPassword()));
                }
            }
        } else {
            // Trường hợp: Thêm mới sinh viên
            if (sinhVien.getPassword() != null && !sinhVien.getPassword().trim().isEmpty()) {
                sinhVien.setPassword(passwordEncoder.encode(sinhVien.getPassword()));
            }
        }

        repo.save(sinhVien);
        ra.addFlashAttribute("success", "Lưu sinh viên thành công!");
        return "redirect:/admin/sinhvien";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá sinh viên thành công!");
        return "redirect:/admin/sinhvien";
    }
}
