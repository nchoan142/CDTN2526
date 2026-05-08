package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.repository.ThongBaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/thongbao")
public class ThongBaoController {

    private final ThongBaoRepository repo;

    public ThongBaoController(ThongBaoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repo.findAllByOrderByNgayGuiDesc());
    }

    @GetMapping("/ghim")
    public ResponseEntity<?> getGhim() {
        return ResponseEntity.ok(repo.findByGhimTrueOrderByNgayGuiDesc());
    }
}
