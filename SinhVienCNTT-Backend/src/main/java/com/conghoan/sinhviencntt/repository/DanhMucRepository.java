package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {
    List<DanhMuc> findByDangDungTrue();

    List<DanhMuc> findByDanhMucCha(Long danhMucCha);

    List<DanhMuc> findByDangDungTrueAndDanhMucChaIsNullOrderBySttAsc();

    List<DanhMuc> findAllByOrderBySttAsc();
}
