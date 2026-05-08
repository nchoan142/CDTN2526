package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.repository.GiangVienRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/giangvien")
public class GiangVienController {

    private final GiangVienRepository repo;

    public GiangVienController(GiangVienRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{maGv}")
    public ResponseEntity<?> getByMaGv(@PathVariable String maGv) {
        return repo.findByMaGiangVien(maGv)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
