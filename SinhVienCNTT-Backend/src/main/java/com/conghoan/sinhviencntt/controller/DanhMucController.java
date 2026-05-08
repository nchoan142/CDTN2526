package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.repository.DanhMucRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/danhmuc")
public class DanhMucController {

    private final DanhMucRepository repo;

    public DanhMucController(DanhMucRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(repo.findByDangDungTrue());
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllIncludeDisabled() {
        return ResponseEntity.ok(repo.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveTopLevel() {
        return ResponseEntity.ok(repo.findByDangDungTrueAndDanhMucChaIsNullOrderBySttAsc());
    }

    @GetMapping("/{id}/con")
    public ResponseEntity<?> getChildren(@PathVariable Long id) {
        return ResponseEntity.ok(repo.findByDanhMucCha(id));
    }
}
