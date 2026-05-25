package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.GiangVien;
import com.conghoan.sinhviencntt.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GiangVienRepository extends JpaRepository<GiangVien, Long> {
    Optional<GiangVien> findByMaGiangVien(String maGiangVien);
    List<GiangVien> findByTenContainingIgnoreCaseOrMaGiangVienContainingIgnoreCase(String ten, String maGiangVien);
    boolean existsByMaGiangVien(String maGiangVien);
}
