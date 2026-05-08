package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.DanhMuc;
import com.conghoan.sinhviencntt.repository.DanhMucRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/danhmuc")
public class AdminDanhMucController {

    private final DanhMucRepository repo;

    public AdminDanhMucController(DanhMucRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", repo.findAll());
        return "admin/danhmuc-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("danhMuc", new DanhMuc());
        return "admin/danhmuc-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("danhMuc", repo.findById(id).orElseThrow());
        return "admin/danhmuc-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DanhMuc danhMuc, RedirectAttributes ra) {
        repo.save(danhMuc);
        ra.addFlashAttribute("success", "Lưu danh mục thành công!");
        return "redirect:/admin/danhmuc";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá danh mục thành công!");
        return "redirect:/admin/danhmuc";
    }
}
