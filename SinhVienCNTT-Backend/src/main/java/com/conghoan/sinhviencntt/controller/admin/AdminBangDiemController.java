package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.BangDiem;
import com.conghoan.sinhviencntt.repository.BangDiemRepository;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/bangdiem")
public class AdminBangDiemController {

    private final BangDiemRepository repo;
    private final SinhVienRepository svRepo;

    public AdminBangDiemController(BangDiemRepository repo, SinhVienRepository svRepo) {
        this.repo = repo;
        this.svRepo = svRepo;
    }

    @GetMapping
    public String search(@RequestParam(required = false) String msv, Model model) {
        if (msv != null && !msv.isEmpty()) {
            model.addAttribute("list", repo.findByMaSinhVien(msv));
            model.addAttribute("sinhVien", svRepo.findByMaSinhVien(msv).orElse(null));
            model.addAttribute("msv", msv);
        }
        return "admin/bangdiem-list";
    }

    @GetMapping("/add")
    public String addForm(@RequestParam(required = false) String msv, Model model) {
        BangDiem bd = new BangDiem();
        if (msv != null) bd.setMaSinhVien(msv);
        model.addAttribute("bangDiem", bd);
        model.addAttribute("isNew", true);
        return "admin/bangdiem-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("bangDiem", repo.findById(id).orElseThrow());
        model.addAttribute("isNew", false);
        return "admin/bangdiem-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        BangDiem bd = repo.findById(id).orElseThrow();
        String msv = bd.getMaSinhVien();
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá bảng điểm thành công!");
        return "redirect:/admin/bangdiem?msv=" + msv;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BangDiem bangDiem, RedirectAttributes ra) {
        repo.save(bangDiem);
        ra.addFlashAttribute("success", "Cập nhật điểm thành công!");
        return "redirect:/admin/bangdiem?msv=" + bangDiem.getMaSinhVien();
    }
}
