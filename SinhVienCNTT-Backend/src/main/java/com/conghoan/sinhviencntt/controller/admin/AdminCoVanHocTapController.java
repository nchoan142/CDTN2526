package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.CoVanHocTap;
import com.conghoan.sinhviencntt.repository.CoVanHocTapRepository;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.KyHocRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/cvht")
public class AdminCoVanHocTapController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final CoVanHocTapRepository repo;
    private final KyHocRepository kyHocRepo;
    private final GiangVienRepository giangVienRepo;

    public AdminCoVanHocTapController(CoVanHocTapRepository repo, KyHocRepository kyHocRepo, GiangVienRepository giangVienRepo) {
        this.repo = repo;
        this.kyHocRepo = kyHocRepo;
        this.giangVienRepo = giangVienRepo;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String maKy,
                       @RequestParam(required = false) String maLop,
                       @RequestParam(required = false) String maGv,
                       Model model) {
        List<CoVanHocTap> list;
        if (maGv != null && !maGv.isEmpty()) {
            list = repo.findByMaGiangVien(maGv);
            model.addAttribute("maGv", maGv);
        } else if (maLop != null && !maLop.isEmpty()) {
            list = repo.findByMaLop(maLop);
            model.addAttribute("maLop", maLop);
        } else if (maKy != null && !maKy.isEmpty()) {
            list = repo.findByMaKy(maKy);
            model.addAttribute("maKy", maKy);
        } else {
            list = repo.findAll();
        }
        model.addAttribute("list", list);
        model.addAttribute("kyHocList", kyHocRepo.findAll());
        return "admin/cvht-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("cvht", new CoVanHocTap());
        model.addAttribute("kyHocList", kyHocRepo.findAll());
        return "admin/cvht-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("cvht", repo.findById(id).orElseThrow());
        model.addAttribute("kyHocList", kyHocRepo.findAll());
        return "admin/cvht-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("cvht") CoVanHocTap cvht, RedirectAttributes ra, Model model) {
        // Kiểm tra xem mã giảng viên có tồn tại không
        if (cvht.getMaGiangVien() != null && !cvht.getMaGiangVien().isEmpty()) {
            if (!giangVienRepo.existsByMaGiangVien(cvht.getMaGiangVien())) {
                model.addAttribute("error", "Mã giảng viên '" + cvht.getMaGiangVien() + "' không tồn tại trong hệ thống!");
                model.addAttribute("kyHocList", kyHocRepo.findAll());
                return "admin/cvht-form";
            }
        }
        
        repo.save(cvht);
        ra.addFlashAttribute("success", "Lưu phân công CVHT thành công!");
        return "redirect:/admin/cvht";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá phân công thành công!");
        return "redirect:/admin/cvht";
    }
}
