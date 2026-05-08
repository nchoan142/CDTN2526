package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.TinTuc;
import com.conghoan.sinhviencntt.repository.TinTucRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/tintuc")
public class AdminTinTucController {

    private final TinTucRepository repo;

    public AdminTinTucController(TinTucRepository repo) {
        this.repo = repo;
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
    public String save(@ModelAttribute TinTuc tinTuc, RedirectAttributes ra) {
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
