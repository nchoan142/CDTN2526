package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.TinTuc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TinTucRepository extends JpaRepository<TinTuc, Long> {
    List<TinTuc> findByHienThiTrueOrderByNgayDangDesc();
    List<TinTuc> findAllByOrderByNgayDangDesc();
}
