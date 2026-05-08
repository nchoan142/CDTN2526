package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.repository.KyHocRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ky")
public class KyHocController {

    private final KyHocRepository repo;

    public KyHocController(KyHocRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/{maKy}")
    public ResponseEntity<?> getByMaKy(@PathVariable String maKy) {
        return repo.findByMaKy(maKy)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/hientai")
    public ResponseEntity<?> getKyHienTai() {
        return repo.findByMacDinh(1)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
