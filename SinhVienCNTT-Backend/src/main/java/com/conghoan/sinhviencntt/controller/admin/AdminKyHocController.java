package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.KyHoc;
import com.conghoan.sinhviencntt.repository.KyHocRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/kyhoc")
public class AdminKyHocController {

    private final KyHocRepository repo;

    public AdminKyHocController(KyHocRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", repo.findAll());
        return "admin/kyhoc-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("kyHoc", new KyHoc());
        return "admin/kyhoc-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("kyHoc", repo.findById(id).orElseThrow());
        return "admin/kyhoc-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute KyHoc kyHoc, RedirectAttributes ra) {
        repo.save(kyHoc);
        ra.addFlashAttribute("success", "Lưu kỳ học thành công!");
        return "redirect:/admin/kyhoc";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá kỳ học thành công!");
        return "redirect:/admin/kyhoc";
    }

    @GetMapping("/macdinh/{id}")
    public String setDefault(@PathVariable Long id, RedirectAttributes ra) {
        repo.findAll().forEach(k -> { k.setMacDinh(0); repo.save(k); });
        KyHoc ky = repo.findById(id).orElseThrow();
        ky.setMacDinh(1);
        repo.save(ky);
        ra.addFlashAttribute("success", "Đã đặt kỳ mặc định!");
        return "redirect:/admin/kyhoc";
    }
}
