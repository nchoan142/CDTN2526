package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.dto.ApiResponse;
import com.conghoan.sinhviencntt.dto.HoiDapRequest;
import com.conghoan.sinhviencntt.entity.HoiDap;
import com.conghoan.sinhviencntt.repository.HoiDapRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("HoiDapController - hỏi đáp")
class HoiDapControllerTest {

    @Mock HoiDapRepository repo;
    @InjectMocks HoiDapController controller;

    @Test
    @DisplayName("GET / trả list theo ngayHoi giảm dần")
    void getAll_shouldReturnAllOrderedByNgayHoiDesc() {
        when(repo.findAllByOrderByNgayHoiDesc()).thenReturn(List.of(new HoiDap(), new HoiDap()));

        ResponseEntity<?> resp = controller.getAll();

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat((List<?>) resp.getBody()).hasSize(2);
        verify(repo).findAllByOrderByNgayHoiDesc();
    }

    @Test
    @DisplayName("GET /daduyet chỉ trả câu đã duyệt")
    void getDaDuyet_shouldReturnApprovedOnly() {
        when(repo.findByDaDuyetTrueOrderByNgayHoiDesc()).thenReturn(List.of(new HoiDap()));

        ResponseEntity<?> resp = controller.getDaDuyet();

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        verify(repo).findByDaDuyetTrueOrderByNgayHoiDesc();
    }

    @Test
    @DisplayName("GET /sinhvien/{msv} trả câu hỏi của sinh viên")
    void getByMsv_shouldReturnStudentQuestions() {
        when(repo.findByMaSinhVien("A38200"))
                .thenReturn(List.of(HoiDap.builder().maSinhVien("A38200").build()));

        ResponseEntity<?> resp = controller.getByMsv("A38200");

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat((List<?>) resp.getBody()).hasSize(1);
    }

    @Test
    @DisplayName("POST / tạo câu hỏi mới với daDuyet=false và ngayHoi tự gán")
    void create_shouldSaveQuestionWithDefaults() {
        HoiDapRequest req = new HoiDapRequest();
        req.setMaSinhVien("A38200");
        req.setTenSinhVien("Đăng");
        req.setCauHoi("Khi nào đăng ký học phần?");

        ResponseEntity<?> resp = controller.create(req);

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat(((ApiResponse<?>) resp.getBody()).getCode()).isZero();

        ArgumentCaptor<HoiDap> captor = ArgumentCaptor.forClass(HoiDap.class);
        verify(repo).save(captor.capture());
        HoiDap saved = captor.getValue();
        assertThat(saved.getMaSinhVien()).isEqualTo("A38200");
        assertThat(saved.getCauHoi()).isEqualTo("Khi nào đăng ký học phần?");
        assertThat(saved.getDaDuyet()).isFalse();
        assertThat(saved.getNgayHoi()).isNotNull();
    }
}
