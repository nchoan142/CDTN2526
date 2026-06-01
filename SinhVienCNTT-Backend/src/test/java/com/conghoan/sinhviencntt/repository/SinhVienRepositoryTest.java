package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.SinhVien;
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
@DisplayName("SinhVienRepository - JPA queries")
class SinhVienRepositoryTest {

    @Autowired
    SinhVienRepository repo;

    @Test
    @DisplayName("Lấy dữ liệu của sinh viên nếu sinh viên có trong hệ thống")
    void findByMaSinhVien_shouldReturnPresent_whenStudentExists() {
        Optional<SinhVien> found = repo.findByMaSinhVien("A35025");
        assertThat(found).isPresent();
    }

    @Test
    @DisplayName("Kiểm tra sinh viên có tồn tại trong hệ thống không?")
    void existsByMaSinhVien_shouldReturnCorrectBoolean_basedOnExistence() {
        assertThat(repo.existsByMaSinhVien("A35025")).isTrue();
        assertThat(repo.existsByMaSinhVien("A35099")).isFalse();
    }

    @Test
    @DisplayName("Lấy danh sách sinh viên theo lớp chuyên ngành")
    void findByLopChuyenNganh_shouldReturnNotEmptyList_whenClassHasStudents() {
        List<SinhVien> svs = repo.findByLopChuyenNganh("TA33c1");

        assertThat(svs).isNotEmpty();
    }

    @Test
    @DisplayName("Lấy danh sách sinh viên theo khóa")
    void findByKhoa_shouldReturnExpectedSize_whenFacultyHasStudents() {
        assertThat(repo.findByKhoa("33")).hasSize(311);
    }

    @Test
    @DisplayName("Lấy danh sách sinh viên theo mã sinh viên hoặc tên sinh viên (không phân biệt chữ hoa/ chữ thường")
    void findByTenContainingOrMaSinhVienContaining_shouldBeCaseInsensitive() {
        assertThat(repo.findByTenContainingIgnoreCaseOrMaSinhVienContainingIgnoreCase("nguyễn công hoàn", "a35025")).isNotEmpty();
        assertThat(repo.findByTenContainingIgnoreCaseOrMaSinhVienContainingIgnoreCase("Nguyễn Công Hoàn", "A35025")).isNotEmpty();
        assertThat(repo.findByTenContainingIgnoreCaseOrMaSinhVienContainingIgnoreCase("Nguyễn Công Hoàn", "a35025")).isNotEmpty();
    }
}
