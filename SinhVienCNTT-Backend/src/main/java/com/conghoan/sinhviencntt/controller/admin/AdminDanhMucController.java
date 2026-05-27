package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.DanhMuc;
import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.repository.DanhMucRepository;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/danhmuc")
public class AdminDanhMucController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final DanhMucRepository repo;
    private final GiangVienRepository giangVienRepo;

    public AdminDanhMucController(DanhMucRepository repo, GiangVienRepository giangVienRepo) {
        this.repo = repo;
        this.giangVienRepo = giangVienRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", repo.findAllByOrderBySttAsc());
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
    public String save(@ModelAttribute("danhMuc") DanhMuc danhMuc, RedirectAttributes ra, Model model) {
        // Kiểm tra xem mã màn hình đã tồn tại chưa
        if (danhMuc.getMaManHinh() != null) {
            boolean isDuplicate = false;
            if (danhMuc.getId() == null) {
                isDuplicate = repo.existsByMaManHinh(danhMuc.getMaManHinh());
            } else {
                DanhMuc existing = repo.findById(danhMuc.getId()).orElse(null);
                if (existing != null && !danhMuc.getMaManHinh().equals(existing.getMaManHinh())) {
                    isDuplicate = repo.existsByMaManHinh(danhMuc.getMaManHinh());
                }
            }
            
            if (isDuplicate) {
                model.addAttribute("error", "Danh mục đã tồn tại!");
                return "admin/danhmuc-form";
            }
        }

        // Kiểm tra người quản lý có tồn tại và có quyền hay không
        String nguoiQuanLy = danhMuc.getNguoiQuanLy();
        if (nguoiQuanLy != null) {

            GiangVien gv = giangVienRepo.findByMaGiangVien(nguoiQuanLy).orElse(null);

            if (gv == null) {
                model.addAttribute("error", "Giảng viên '" + nguoiQuanLy + "' không tồn tại trong hệ thống!");
                return "admin/danhmuc-form";
            } else {
                // Kiểm tra xem giảng viên có là Admin hoặc Thư ký hay không
                boolean isAdmin = gv.getRoleQuanTri() != null;
                boolean isThuKy = gv.getRoleThuKy() != null;

                if (!isAdmin && !isThuKy) {
                    model.addAttribute("error", "Người quản lý phải là Giảng viên có quyền Admin hoặc Thư ký");
                    return "admin/danhmuc-form";
                }
            }

        }
        
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
