package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.BangDiem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BangDiemRepository extends JpaRepository<BangDiem, Long> {
    List<BangDiem> findByMaSinhVien(String maSinhVien);
    List<BangDiem> findByMaHocPhan(String maHocPhan);
    List<BangDiem> findByMaSinhVienAndChuyenNganhId(String maSinhVien, String chuyenNganhId);
}
