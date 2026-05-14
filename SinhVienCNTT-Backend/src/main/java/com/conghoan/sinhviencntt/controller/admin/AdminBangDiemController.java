package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.BangDiem;
import com.conghoan.sinhviencntt.repository.BangDiemRepository;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/bangdiem")
public class AdminBangDiemController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final BangDiemRepository repo;
    private final SinhVienRepository svRepo;
    private final GiangVienRepository giangVienRepo;

    public AdminBangDiemController(BangDiemRepository repo, SinhVienRepository svRepo, GiangVienRepository giangVienRepo) {
        this.repo = repo;
        this.svRepo = svRepo;
        this.giangVienRepo = giangVienRepo;
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
        ra.addFlashAttribute("success", "Xoá bảng học phần thành công!");
        return "redirect:/admin/bangdiem?msv=" + msv;
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("bangDiem") BangDiem bangDiem, RedirectAttributes ra, Model model) {
        // Kiểm tra mã giảng viên có tồn tại không
        if (bangDiem.getMaGiangVien() != null && !bangDiem.getMaGiangVien().isEmpty()) {
            if (!giangVienRepo.existsByMaGiangVien(bangDiem.getMaGiangVien())) {
                model.addAttribute("error", "Mã giảng viên '" + bangDiem.getMaGiangVien() + "' không tồn tại.");
                model.addAttribute("isNew", bangDiem.getId() == null);
                return "admin/bangdiem-form";
            }
        }
        repo.save(bangDiem);
        ra.addFlashAttribute("success", "Cập nhật điểm thành công!");
        return "redirect:/admin/bangdiem?msv=" + bangDiem.getMaSinhVien();
    }
}
