package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.SinhVien;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/sinhvien")
public class AdminSinhVienController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final SinhVienRepository repo;
    private final PasswordEncoder passwordEncoder;

    public AdminSinhVienController(SinhVienRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String search, Model model) {
        if (search != null && !search.isEmpty()) {
//            model.addAttribute("list", repo.findByTenContainingIgnoreCaseOrMaSinhVienContainingIgnoreCase(search, search));
            model.addAttribute("list", repo.findByMaSinhVienContainingIgnoreCase(search));
//            model.addAttribute("search", repo.findByMaSinhVienContainingIgnoreCase(search));
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
    public String save(@ModelAttribute("sinhVien") SinhVien sinhVien, RedirectAttributes ra, Model model) {
        // Kiểm tra xem mã sinh viên đã tồn tại chưa
        boolean isDuplicate = false;
        if (sinhVien.getId() == null) {
            isDuplicate = repo.existsByMaSinhVien(sinhVien.getMaSinhVien());
        } else {
            SinhVien existing = repo.findById(sinhVien.getId()).orElse(null);
            if (existing != null && !existing.getMaSinhVien().equals(sinhVien.getMaSinhVien())) {
                isDuplicate = repo.existsByMaSinhVien(sinhVien.getMaSinhVien());
            }
        }

        if (isDuplicate) {
            model.addAttribute("error", "Mã sinh viên '" + sinhVien.getMaSinhVien() + "' đã tồn tại trong hệ thống!");
            return "admin/sinhvien-form";
        }

        if (sinhVien.getId() != null) {
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
        try {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá sinh viên thành công!");
        } catch (DataIntegrityViolationException e) {
            ra.addFlashAttribute("error", "Không thể xoá sinh viên này do đang có dữ liệu ràng buộc.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Đã xảy ra lỗi không xác định khi xoá sinh viên.");
        }
        return "redirect:/admin/sinhvien";
    }
}
