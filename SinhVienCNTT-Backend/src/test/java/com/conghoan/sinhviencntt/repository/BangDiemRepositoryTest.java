package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.BangDiem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("BangDiemRepository - JPA queries")
class BangDiemRepositoryTest {

    @Autowired
    BangDiemRepository repo;

    @Test
    @DisplayName("Lấy danh sách bảng điểm theo mã sinh viên")
    void getListScoresByMsv() {
        List<BangDiem> list = repo.findByMaSinhVien("A35025");
        assertThat(list).isNotEmpty();
    }

    @Test
    @DisplayName("Lấy danh sách bảng điểm theo mã học phần")
    void getListScoresByMhp() {
        List<BangDiem> list = repo.findByMaHocPhan("CS121");
        assertThat(list).isNotEmpty();
    }

    @Test
    @DisplayName("Lấy danh sách bảng điểm theo mã sinh viên và mã chuyên ngành")
    void getListScoresByMsvAndMcn() {
        List<BangDiem> list = repo.findByMaSinhVienAndChuyenNganhId("A35025", "TT");
        assertThat(list).isNotEmpty();
    }
}