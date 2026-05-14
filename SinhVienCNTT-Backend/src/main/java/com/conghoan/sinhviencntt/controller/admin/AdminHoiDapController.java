package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.entity.CoVanHocTap;
import com.conghoan.sinhviencntt.entity.HoiDap;
import com.conghoan.sinhviencntt.entity.SinhVien;
import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.repository.CoVanHocTapRepository;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.HoiDapRepository;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/hoidap")
public class AdminHoiDapController {

    private final HoiDapRepository repo;
    private final CoVanHocTapRepository cvhtRepo;
    private final SinhVienRepository svRepo;
    private final GiangVienRepository giangVienRepo;

    public AdminHoiDapController(HoiDapRepository repo, CoVanHocTapRepository cvhtRepo,
                                  SinhVienRepository svRepo, GiangVienRepository giangVienRepo) {
        this.repo = repo;
        this.cvhtRepo = cvhtRepo;
        this.svRepo = svRepo;
        this.giangVienRepo = giangVienRepo;
    }

    @GetMapping
    public String list(Model model, Authentication auth) {
        String username = auth.getName();
        boolean isCvht = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_CVHT"));

        if (isCvht) {
            // tab "SV của tôi" + tab "Tất cả"
            Set<String> svOfMe = getMsvOfCvht(username);
            List<HoiDap> listOfMe = svOfMe.isEmpty()
                    ? List.of()
                    : repo.findByMaSinhVienInOrderByNgayHoiDesc(svOfMe);
            List<HoiDap> listAll = repo.findAllByOrderByNgayHoiDesc();

            model.addAttribute("listOfMe", listOfMe);
            model.addAttribute("list", listAll);
            model.addAttribute("isCvht", true);
            model.addAttribute("tenCvht", username);
        } else {
            // Admin: xem tất cả
            model.addAttribute("list", repo.findAllByOrderByNgayHoiDesc());
            model.addAttribute("isCvht", false);
        }
        return "admin/hoidap-list";
    }

    @GetMapping("/chuaduyet")
    public String chuaDuyet(Model model, Authentication auth) {
        model.addAttribute("list", repo.findByDaDuyetFalseOrderByNgayHoiDesc());
        model.addAttribute("isCvht", false);
        return "admin/hoidap-list";
    }

    @GetMapping("/traloi/{id}")
    public String traLoiForm(@PathVariable Long id, Model model, Authentication auth) {
        HoiDap hd = repo.findById(id).orElseThrow();
        
        // Lấy tên giảng viên từ Database
        String currentUserName = "Admin";
        Optional<GiangVien> optGv = giangVienRepo.findByMaGiangVien(auth.getName());
        if (optGv.isPresent()) {
            GiangVien gv = optGv.get();
            currentUserName = gv.getTen();
        }
        model.addAttribute("currentUserName", currentUserName);
        
        model.addAttribute("hoiDap", hd);
        return "admin/hoidap-form";
    }

    @PostMapping("/traloi")
    public String traLoi(@ModelAttribute HoiDap hoiDap, RedirectAttributes ra) {
        HoiDap existing = repo.findById(hoiDap.getId()).orElseThrow();
        existing.setCauTraLoi(hoiDap.getCauTraLoi());
        existing.setNguoiTraLoi(hoiDap.getNguoiTraLoi());
        existing.setNgayTraLoi(LocalDateTime.now());
        existing.setDaDuyet(true);
        repo.save(existing);
        ra.addFlashAttribute("success", "Đã trả lời và duyệt câu hỏi!");
        return "redirect:/admin/hoidap";
    }

    @GetMapping("/duyet/{id}")
    public String duyet(@PathVariable Long id, RedirectAttributes ra) {
        HoiDap hd = repo.findById(id).orElseThrow();
        hd.setDaDuyet(true);
        repo.save(hd);
        ra.addFlashAttribute("success", "Đã duyệt câu hỏi!");
        return "redirect:/admin/hoidap";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        repo.deleteById(id);
        ra.addFlashAttribute("success", "Đã xoá câu hỏi!");
        return "redirect:/admin/hoidap";
    }

    // Lấy danh sách MSV của sinh viên mà CVHT phụ trách
    // Flow: maGiangVien → co_van_hoc_tap.maLop → sinh_vien.lopChuyenNganh
    private Set<String> getMsvOfCvht(String maGiangVien) {
        // Tìm các lớp mà CVHT phụ trách
        List<CoVanHocTap> assignments = cvhtRepo.findByMaGiangVien(maGiangVien);
        Set<String> maLops = assignments.stream()
                .map(CoVanHocTap::getMaLop)
                .collect(Collectors.toSet());

        if (maLops.isEmpty()) return Set.of();

        // Tìm sinh viên thuộc các lớp đó
        List<SinhVien> students = svRepo.findByLopChuyenNganhIn(maLops);
        return students.stream()
                .map(SinhVien::getMaSinhVien)
                .collect(Collectors.toSet());
    }
}
