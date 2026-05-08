package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.repository.ThoiKhoaBieuRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tkb")
public class ThoiKhoaBieuController {

    private final ThoiKhoaBieuRepository repo;

    public ThoiKhoaBieuController(ThoiKhoaBieuRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/{msv}")
    public ResponseEntity<?> getByMsv(@PathVariable String msv) {
        return ResponseEntity.ok(repo.findByMaSinhVienOrderByThuAscTietBatDauAsc(msv));
    }

    @GetMapping("/{msv}/{maKy}")
    public ResponseEntity<?> getByMsvAndKy(@PathVariable String msv, @PathVariable String maKy) {
        return ResponseEntity.ok(repo.findByMaSinhVienAndMaKyOrderByThuAscTietBatDauAsc(msv, maKy));
    }
}
