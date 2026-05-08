package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.GiangVien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GiangVienRepository extends JpaRepository<GiangVien, Long> {
    Optional<GiangVien> findByMaGiangVien(String maGiangVien);
    boolean existsByMaGiangVien(String maGiangVien);
}
