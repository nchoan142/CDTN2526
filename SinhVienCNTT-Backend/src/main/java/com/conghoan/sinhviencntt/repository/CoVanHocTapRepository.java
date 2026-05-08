package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.CoVanHocTap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CoVanHocTapRepository extends JpaRepository<CoVanHocTap, Long> {
    List<CoVanHocTap> findByMaKy(String maKy);
    List<CoVanHocTap> findByMaGiangVien(String maGiangVien);
    List<CoVanHocTap> findByMaLop(String maLop);
}
