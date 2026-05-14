package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.ThongBao;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.ThongBaoRepository;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/thongbao")
public class AdminThongBaoController {
    // Xóa các ký tự khoảng trắng 2 đầu của chuỗi ở các ô input
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    private final ThongBaoRepository repo;
    private final GiangVienRepository giangVienRepo;

    public AdminThongBaoController(ThongBaoRepository repo, GiangVienRepository giangVienRepo) {
        this.repo = repo;
        this.giangVienRepo = giangVienRepo;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("list", repo.findAllByOrderByNgayGuiDesc());
        return "admin/thongbao-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("thongBao", new ThongBao());
        return "admin/thongbao-form";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("thongBao", repo.findById(id).orElseThrow());
        return "admin/thongbao-form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("thongBao") ThongBao thongBao, RedirectAttributes ra, Model model) {
        // Kiểm tra xem người gửi (giảng viên) có tồn tại không
        if (thongBao.getNguoiGui() != null) {
            boolean giangVienExists = giangVienRepo.existsByMaGiangVien(thongBao.getNguoiGui());
            
            if (!giangVienExists) {
                model.addAttribute("error", "Mã người gửi '" + thongBao.getNguoiGui() + "' không tồn tại trong hệ thống!");
                return "admin/thongbao-form";
            }
        }

        if (thongBao.getNgayGui() == null) {
            thongBao.setNgayGui(LocalDateTime.now());
        }
        if (thongBao.getDaDoc() == null) thongBao.setDaDoc(false);
        if (thongBao.getGhim() == null) thongBao.setGhim(false);
        repo.save(thongBao);
        ra.addFlashAttribute("success", "Lưu thông báo thành công!");
        return "redirect:/admin/thongbao";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Xoá thông báo thành công!");
        return "redirect:/admin/thongbao";
    }

    @GetMapping("/ghim/{id}")
    public String toggleGhim(@PathVariable Long id, RedirectAttributes ra) {
        ThongBao tb = repo.findById(id).orElseThrow();
        tb.setGhim(!Boolean.TRUE.equals(tb.getGhim()));
        repo.save(tb);
        ra.addFlashAttribute("success", tb.getGhim() ? "Đã ghim thông báo!" : "Đã bỏ ghim!");
        return "redirect:/admin/thongbao";
    }
}
