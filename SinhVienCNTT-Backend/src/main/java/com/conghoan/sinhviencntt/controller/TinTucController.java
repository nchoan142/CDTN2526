package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.repository.TinTucRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tintuc")
public class TinTucController {

    private final TinTucRepository repo;

    public TinTucController(TinTucRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repo.findByHienThiTrueOrderByNgayDangDesc());
    }
}
