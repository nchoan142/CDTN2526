package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.GiangVien;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("GiangVienRepository")
class GiangVienRepositoryTest {

    @Autowired
    GiangVienRepository repo;

    @Test
    @DisplayName("Lấy dữ liệu của giảng viên nếu giảng viên có trong hệ thống")
    void findByMaGiangVien_shouldReturnPresent_whenTeacherExists() {
        Optional<GiangVien> found = repo.findByMaGiangVien("CTI064");
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("Trả về true nếu giảng viên tồn tại trong hệ thống")
    void existsByMaGiangVien_shouldReturnCorrectBoolean_basedOnExistence() {
        assertThat(repo.existsByMaGiangVien("CTI060")).isTrue();
        assertThat(repo.existsByMaGiangVien("ZZZ999")).isFalse();
    }

    @Test
    @DisplayName("Lấy danh sách giảng viên theo mã giảng viên hoặc tên giảng viên (không phân biệt chữ hoa/ chữ thường")
    void findByTenContainingOrMaGiangVienContaining_shouldBeCaseInsensitive() {
        assertThat(repo.findByTenContainingIgnoreCaseOrMaGiangVienContainingIgnoreCase("Nguyễn Xuân Thanh", "CTI061")).isNotEmpty();
        assertThat(repo.findByTenContainingIgnoreCaseOrMaGiangVienContainingIgnoreCase("NGUYỄN XUÂN THANH", "CTI061")).isNotEmpty();
    }
}