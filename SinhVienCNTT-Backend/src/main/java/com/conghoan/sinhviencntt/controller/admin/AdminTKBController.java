package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.ThoiKhoaBieu;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.KyHocRepository;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import com.conghoan.sinhviencntt.repository.ThoiKhoaBieuRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/tkb")
public class AdminTKBController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final ThoiKhoaBieuRepository repo;
    private final SinhVienRepository svRepo;
    private final KyHocRepository kyHocRepo;
    private final GiangVienRepository giangVienRepo;

    public AdminTKBController(ThoiKhoaBieuRepository repo, SinhVienRepository svRepo, KyHocRepository kyHocRepo, GiangVienRepository giangVienRepo) {
        this.repo = repo;
        this.svRepo = svRepo;
        this.kyHocRepo = kyHocRepo;
        this.giangVienRepo = giangVienRepo;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String msv, Model model) {
        if (msv != null && !msv.isEmpty()) {
            model.addAttribute("list", repo.findByMaSinhVienOrderByThuAscTietBatDauAsc(msv));
            model.addAttribute("sinhVien", svRepo.findByMaSinhVien(msv).orElse(null));
            model.addAttribute("msv", msv);
        }
        return "admin/tkb-list";
    }

    @GetMapping("/add")
    public String addForm(@RequestParam(required = false) String msv, Model model) {
        ThoiKhoaBieu tkb = new ThoiKhoaBieu();
        if (msv != null) tkb.setMaSinhVien(msv);
        model.addAttribute("tkb", tkb);
        model.addAttribute("isNew", true);
        model.addAttribute("kyHocList", kyHocRepo.findAll());
        return "admin/tkb-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("tkb", repo.findById(id).orElseThrow());
        model.addAttribute("isNew", false);
        model.addAttribute("kyHocList", kyHocRepo.findAll());
        return "admin/tkb-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("tkb") ThoiKhoaBieu tkb, RedirectAttributes ra, Model model) {
        // Kiểm tra xem mã giảng viên có tồn tại không
        if (tkb.getMaGiangVien() != null && !tkb.getMaGiangVien().isEmpty()) {
            boolean giangVienExists = giangVienRepo.existsByMaGiangVien(tkb.getMaGiangVien());
            
            if (!giangVienExists) {
                model.addAttribute("error", "Mã giảng viên '" + tkb.getMaGiangVien() + "' không tồn tại trong hệ thống!");
                model.addAttribute("isNew", tkb.getId() == null);
                model.addAttribute("kyHocList", kyHocRepo.findAll());
                return "admin/tkb-form";
            }
        }

        repo.save(tkb);
        ra.addFlashAttribute("success", "Lưu thời khoá biểu thành công!");
        return "redirect:/admin/tkb?msv=" + tkb.getMaSinhVien();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        ThoiKhoaBieu tkb = repo.findById(id).orElseThrow();
        String msv = tkb.getMaSinhVien();
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá thời khoá biểu thành công!");
        return "redirect:/admin/tkb?msv=" + msv;
    }
}
