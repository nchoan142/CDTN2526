package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.TinTuc;
import com.conghoan.sinhviencntt.repository.TinTucRepository;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/tintuc")
public class AdminTinTucController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final TinTucRepository repo;
    private final GiangVienRepository giangVienRepo;

    public AdminTinTucController(TinTucRepository repo, GiangVienRepository giangVienRepo) {
        this.repo = repo;
        this.giangVienRepo = giangVienRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", repo.findAllByOrderByNgayDangDesc());
        return "admin/tintuc-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("tinTuc", new TinTuc());
        return "admin/tintuc-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("tinTuc", repo.findById(id).orElseThrow());
        return "admin/tintuc-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("tinTuc") TinTuc tinTuc, RedirectAttributes ra, Model model) {
        // Kiểm tra xem người gửi (giảng viên) có tồn tại không
        if (tinTuc.getNguoiDang() != null) {
            boolean giangVienExists = giangVienRepo.existsByMaGiangVien(tinTuc.getNguoiDang());

            if (!giangVienExists) {
                model.addAttribute("error", "Mã người gửi '" + tinTuc.getNguoiDang() + "' không tồn tại trong hệ thống!");
                return "admin/tintuc-form";
            }
        }

        if (tinTuc.getNgayDang() == null) {
            tinTuc.setNgayDang(LocalDateTime.now());
        }
        if (tinTuc.getHienThi() == null) tinTuc.setHienThi(true);
        repo.save(tinTuc);
        ra.addFlashAttribute("success", "Lưu tin tức thành công!");
        return "redirect:/admin/tintuc";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá tin tức thành công!");
        return "redirect:/admin/tintuc";
    }
}
