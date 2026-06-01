package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.entity.BangDiem;
import com.conghoan.sinhviencntt.repository.BangDiemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("BangDiemController - GET bảng điểm theo MSV / chuyên ngành")
class BangDiemControllerTest {

    @Mock BangDiemRepository repo;
    @InjectMocks BangDiemController controller;

    @Test
    @DisplayName("trả về list bảng điểm của sinh viên")
    void getByMsv_shouldReturnList() {
        BangDiem bd = BangDiem.builder().id(1L).maSinhVien("A38200").maHocPhan("CS101").build();
        when(repo.findByMaSinhVien("A38200")).thenReturn(List.of(bd));

        ResponseEntity<?> resp = controller.getByMsv("A38200");

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat((List<?>) resp.getBody()).hasSize(1);
        verify(repo).findByMaSinhVien("A38200");
    }

    @Test
    @DisplayName("GET /{msv} với MSV không có dữ liệu trả list rỗng")
    void getByMsv_shouldReturnEmptyList() {
        when(repo.findByMaSinhVien("ZZZ")).thenReturn(List.of());

        ResponseEntity<?> resp = controller.getByMsv("ZZZ");

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat((List<?>) resp.getBody()).isEmpty();
    }

    @Test
    @DisplayName("GET /{msv}/{chuyenNganh} lọc theo cả 2 tiêu chí")
    void getByMsvAndNganh_shouldFilterBoth() {
        BangDiem bd = BangDiem.builder().id(2L).maSinhVien("A38200").chuyenNganhId("TA").build();
        when(repo.findByMaSinhVienAndChuyenNganhId("A38200", "TA")).thenReturn(List.of(bd));

        ResponseEntity<?> resp = controller.getByMsvAndNganh("A38200", "TA");

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat((List<?>) resp.getBody()).hasSize(1);
        verify(repo).findByMaSinhVienAndChuyenNganhId("A38200", "TA");
    }
}
