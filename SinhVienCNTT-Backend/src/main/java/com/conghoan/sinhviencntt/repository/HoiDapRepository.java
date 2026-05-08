package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.HoiDap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface HoiDapRepository extends JpaRepository<HoiDap, Long> {
    List<HoiDap> findByMaSinhVien(String maSinhVien);
    List<HoiDap> findByDaDuyetTrueOrderByNgayHoiDesc();
    List<HoiDap> findByDaDuyetFalseOrderByNgayHoiDesc();
    List<HoiDap> findAllByOrderByNgayHoiDesc();
    List<HoiDap> findByMaSinhVienInOrderByNgayHoiDesc(Collection<String> maSinhViens);
}
