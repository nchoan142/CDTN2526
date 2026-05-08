package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.ThongBao;
import com.conghoan.sinhviencntt.repository.ThongBaoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/thongbao")
public class AdminThongBaoController {

    private final ThongBaoRepository repo;

    public AdminThongBaoController(ThongBaoRepository repo) {
        this.repo = repo;
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
    public String save(@ModelAttribute ThongBao thongBao, RedirectAttributes ra) {
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
