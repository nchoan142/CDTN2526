package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.dto.ApiResponse;
import com.conghoan.sinhviencntt.entity.SinhVien;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sinhvien")
public class SinhVienController {

    private final SinhVienRepository repo;

    public SinhVienController(SinhVienRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{msv}")
    public ResponseEntity<?> getByMsv(@PathVariable String msv) {
        return repo.findByMaSinhVien(msv)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lop/{lop}")
    public ResponseEntity<?> getByLop(@PathVariable String lop) {
        return ResponseEntity.ok(repo.findByLopChuyenNganh(lop));
    }

    @GetMapping("/khoa/{khoa}")
    public ResponseEntity<?> getByKhoa(@PathVariable String khoa) {
        return ResponseEntity.ok(repo.findByKhoa(khoa));
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        return ResponseEntity.ok(java.util.Map.of("total", repo.count()));
    }

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String maSinhVien, @RequestParam String ten) {
        return ResponseEntity.ok(repo.findByMaSinhVienContainingIgnoreCase(maSinhVien));
    }
}
