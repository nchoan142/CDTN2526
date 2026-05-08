package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ThongBaoRepository extends JpaRepository<ThongBao, Long> {
    List<ThongBao> findByDoiTuongOrderByNgayGuiDesc(String doiTuong);
    List<ThongBao> findAllByOrderByNgayGuiDesc();
    List<ThongBao> findByGhimTrueOrderByNgayGuiDesc();
}
