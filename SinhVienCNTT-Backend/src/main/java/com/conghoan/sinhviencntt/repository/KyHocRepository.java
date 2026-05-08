package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.KyHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface KyHocRepository extends JpaRepository<KyHoc, Long> {
    Optional<KyHoc> findByMaKy(String maKy);
    Optional<KyHoc> findByMacDinh(Integer macDinh);
    boolean existsByMaKy(String maKy);
}
