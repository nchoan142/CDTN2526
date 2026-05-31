package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.entity.KyHoc;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.KyHocRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/admin/kyhoc")
public class AdminKyHocController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final KyHocRepository repo;
    private final GiangVienRepository giangVienRepo;

    public AdminKyHocController(KyHocRepository repo, GiangVienRepository giangVienRepo) {
        this.repo = repo;
        this.giangVienRepo = giangVienRepo;
    }

    @GetMapping
    public String list(Model model, Principal principal) {
        boolean canEdit = false;
        if (principal != null) {
            String username = principal.getName();
            // Kiểm tra nếu là tài khoản admin mặc định
            if (username.equalsIgnoreCase("admin")) {
                canEdit = true;
            } else {
                // Nếu là giảng viên, kiểm tra quyền Quản trị hoặc Thư ký
                GiangVien gv = giangVienRepo.findByMaGiangVien(username).orElse(null);
                if (gv != null) {
                    boolean isAdmin = Boolean.TRUE.equals(gv.getRoleQuanTri());
                    boolean isThuKy = Boolean.TRUE.equals(gv.getRoleThuKy());
                    if (isAdmin || isThuKy) {
                        canEdit = true;
                    }
                }
            }
        }
        
        model.addAttribute("canEdit", canEdit);
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
        repo.findAll().forEach(k -> { k.setMacDinh(false); repo.save(k); });
        KyHoc ky = repo.findById(id).orElseThrow();
        ky.setMacDinh(true);
        repo.save(ky);
        ra.addFlashAttribute("success", "Đã đặt kỳ mặc định!");
        return "redirect:/admin/kyhoc";
    }
}
