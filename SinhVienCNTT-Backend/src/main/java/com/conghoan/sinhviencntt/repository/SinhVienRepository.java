package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.SinhVien;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SinhVienRepository extends JpaRepository<SinhVien, Long> {
    Optional<SinhVien> findByMaSinhVien(String maSinhVien);
    List<SinhVien> findByLopChuyenNganh(String lopChuyenNganh);
    List<SinhVien> findByLopChuyenNganhIn(Collection<String> lopChuyenNganhs);
    List<SinhVien> findByKhoa(String khoa);
    List<SinhVien> findByNganh(String nganh);
    List<SinhVien> findByTenContainingIgnoreCase(String ten);
    List<SinhVien> findByTenContainingIgnoreCaseOrMaSinhVienContainingIgnoreCase(String ten, String msv);
    boolean existsByMaSinhVien(String maSinhVien);
}