package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.ThoiKhoaBieu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ThoiKhoaBieuRepository extends JpaRepository<ThoiKhoaBieu, Long> {
    List<ThoiKhoaBieu> findByMaSinhVienAndMaKyOrderByThuAscTietBatDauAsc(String maSinhVien, String maKy);
    List<ThoiKhoaBieu> findByMaSinhVienOrderByThuAscTietBatDauAsc(String maSinhVien);
    List<ThoiKhoaBieu> findByMaKy(String maKy);
}
