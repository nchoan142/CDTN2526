package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.entity.CoVanHocTap;
import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.entity.SinhVien;
import com.conghoan.sinhviencntt.repository.CoVanHocTapRepository;
import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/covanhoctap")
public class CoVanHocTapController {

    private final CoVanHocTapRepository repo;
    private final SinhVienRepository svRepo;
    private final GiangVienRepository gvRepo;

    public CoVanHocTapController(CoVanHocTapRepository repo, SinhVienRepository svRepo,
                                  GiangVienRepository gvRepo) {
        this.repo = repo;
        this.svRepo = svRepo;
        this.gvRepo = gvRepo;
    }

    @GetMapping("/{maKy}")
    public ResponseEntity<?> getByKy(@PathVariable String maKy) {
        return ResponseEntity.ok(repo.findByMaKy(maKy));
    }

    @GetMapping("/giangvien/{maGv}")
    public ResponseEntity<?> getByGv(@PathVariable String maGv) {
        return ResponseEntity.ok(repo.findByMaGiangVien(maGv));
    }

    @GetMapping("/lop/{maLop}")
    public ResponseEntity<?> getByLop(@PathVariable String maLop) {
        return ResponseEntity.ok(repo.findByMaLop(maLop));
    }


    // Lấy thông tin CVHT của sinh viên theo MSV
    //Flow: MSV → SinhVien.lopChuyenNganh → CoVanHocTap.maLop → GiangVien
    @GetMapping("/sinhvien/{msv}")
    public ResponseEntity<?> getCVHTOfSinhVien(@PathVariable String msv) {
        SinhVien sv = svRepo.findByMaSinhVien(msv).orElse(null);
        if (sv == null || sv.getLopChuyenNganh() == null) {
            return ResponseEntity.ok(Map.of());
        }

        List<CoVanHocTap> cvhtList = repo.findByMaLop(sv.getLopChuyenNganh());
        if (cvhtList.isEmpty()) {
            return ResponseEntity.ok(Map.of());
        }

        // Lấy CVHT mới nhất (theo kỳ)
        CoVanHocTap cvht = cvhtList.get(cvhtList.size() - 1);
        GiangVien gv = gvRepo.findByMaGiangVien(cvht.getMaGiangVien()).orElse(null);

        Map<String, String> result = new HashMap<>();
        result.put("maGiangVien", cvht.getMaGiangVien());
        result.put("maLop", cvht.getMaLop());
        result.put("maKy", cvht.getMaKy());
        if (gv != null) {
            String tenGv = "";
            if (gv.getHocVi() != null && !gv.getHocVi().isEmpty()) tenGv += gv.getHocVi() + ". ";
            if (gv.getHoTenDem() != null && !gv.getHoTenDem().isEmpty()) tenGv += gv.getHoTenDem() + " ";
            tenGv += gv.getTen();
            result.put("tenGiangVien", tenGv.trim());
            result.put("email", gv.getEmail1());
            result.put("dienThoai", gv.getDienThoai());
        }
        return ResponseEntity.ok(result);
    }
}
