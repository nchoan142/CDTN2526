package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.HoiDap;
import com.conghoan.sinhviencntt.entity.SinhVien;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("HoiDapRepository - JPA queries")
class HoiDapRepositoryTest {

    @Autowired
    HoiDapRepository repo;

    @Test
    @DisplayName("Danh sách câu hỏi của sinh viên theo mã sinh viên")
    void getListQuestionsByMsv() {
        List<HoiDap> questions = repo.findByMaSinhVien("A35092");
        assertThat(questions).hasSize(0);
    }

    @Test
    @DisplayName("Danh sách câu hỏi của sinh viên (đã được duyệt) sắp xếp theo ngày hỏi")
    void getListValidateQuestionsOrderByDate() {
        List<HoiDap> questions = repo.findByDaDuyetTrueOrderByNgayHoiDesc();
        assertThat(questions).hasSize(3);
    }

    @Test
    @DisplayName("Danh sách câu hỏi của sinh viên (chưa được duyệt) sắp xếp theo ngày hỏi")
    void getListUnValidateQuestionsOrderByDate() {
        List<HoiDap> questions = repo.findByDaDuyetFalseOrderByNgayHoiDesc();
        assertThat(questions).hasSize(3);
    }

    @Test
    @DisplayName("Danh sách câu hỏi của sinh viên sắp xếp theo ngày hỏi")
    void getListQuestionsOrderByDate() {
        List<HoiDap> questions = repo.findAllByOrderByNgayHoiDesc();
        assertThat(questions).hasSize(6);
    }

    @Test
    @DisplayName("Danh sách câu hỏi của sinh viên theo mã sinh viên sắp xếp theo ngày hỏi")
    void getFilterListQuestionsOrderByDate() {
        List<HoiDap> questions = repo.findByMaSinhVienInOrderByNgayHoiDesc(Collections.singleton("A35025"));
        assertThat(questions).hasSize(4);
    }
}
