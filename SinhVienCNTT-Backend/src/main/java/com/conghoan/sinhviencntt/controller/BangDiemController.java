package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.repository.BangDiemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bangdiem")
public class BangDiemController {

    private final BangDiemRepository repo;

    public BangDiemController(BangDiemRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{msv}")
    public ResponseEntity<?> getByMsv(@PathVariable String msv) {
        return ResponseEntity.ok(repo.findByMaSinhVien(msv));
    }

    @GetMapping("/{msv}/{chuyenNganh}")
    public ResponseEntity<?> getByMsvAndNganh(@PathVariable String msv, @PathVariable String chuyenNganh) {
        return ResponseEntity.ok(repo.findByMaSinhVienAndChuyenNganhId(msv, chuyenNganh));
    }
}
