package com.conghoan.sinhviencntt.controller.admin;

import com.conghoan.sinhviencntt.dto.DashboardStats;
import com.conghoan.sinhviencntt.repository.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final SinhVienRepository sinhVienRepo;
    private final GiangVienRepository giangVienRepo;
    private final KyHocRepository kyHocRepo;
    private final HoiDapRepository hoiDapRepo;
    private final ThongBaoRepository thongBaoRepo;
    private final DanhMucRepository danhMucRepo;
    private final TinTucRepository tinTucRepo;

    public AdminDashboardController(SinhVienRepository sinhVienRepo, GiangVienRepository giangVienRepo,
                                     KyHocRepository kyHocRepo, HoiDapRepository hoiDapRepo,
                                     ThongBaoRepository thongBaoRepo, DanhMucRepository danhMucRepo,
                                     TinTucRepository tinTucRepo) {
        this.sinhVienRepo = sinhVienRepo;
        this.giangVienRepo = giangVienRepo;
        this.kyHocRepo = kyHocRepo;
        this.hoiDapRepo = hoiDapRepo;
        this.thongBaoRepo = thongBaoRepo;
        this.danhMucRepo = danhMucRepo;
        this.tinTucRepo = tinTucRepo;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardStats stats = DashboardStats.builder()
                .tongSinhVien(sinhVienRepo.count())
                .tongGiangVien(giangVienRepo.count())
                .tongKyHoc(kyHocRepo.count())
                .tongHoiDapChuaDuyet(hoiDapRepo.findByDaDuyetFalseOrderByNgayHoiDesc().size())
                .tongThongBao(thongBaoRepo.count())
                .tongDanhMuc(danhMucRepo.count())
                .kyHocHienTai(kyHocRepo.findByMacDinh(1).map(k -> k.getMaKy() + " - " + k.getTenKy()).orElse("Chưa thiết lập"))
                .build();
        model.addAttribute("stats", stats);
        return "admin/dashboard";
    }
}
