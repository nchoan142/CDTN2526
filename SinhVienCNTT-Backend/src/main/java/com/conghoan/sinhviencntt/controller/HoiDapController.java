package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.dto.ApiResponse;
import com.conghoan.sinhviencntt.dto.HoiDapRequest;
import com.conghoan.sinhviencntt.entity.HoiDap;
import com.conghoan.sinhviencntt.repository.HoiDapRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/hoidap")
public class HoiDapController {

    private final HoiDapRepository repo;

    public HoiDapController(HoiDapRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repo.findAllByOrderByNgayHoiDesc());
    }

    @GetMapping("/daduyet")
    public ResponseEntity<?> getDaDuyet() {
        return ResponseEntity.ok(repo.findByDaDuyetTrueOrderByNgayHoiDesc());
    }

    @GetMapping("/sinhvien/{msv}")
    public ResponseEntity<?> getByMsv(@PathVariable String msv) {
        return ResponseEntity.ok(repo.findByMaSinhVien(msv));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody HoiDapRequest request) {
        HoiDap hd = HoiDap.builder()
                .maSinhVien(request.getMaSinhVien())
                .tenSinhVien(request.getTenSinhVien())
                .cauHoi(request.getCauHoi())
                .ngayHoi(LocalDateTime.now())
                .daDuyet(false)
                .build();
        repo.save(hd);
        return ResponseEntity.ok(ApiResponse.success("Câu hỏi đã được gửi, chờ duyệt"));
    }
}
