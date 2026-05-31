package com.conghoan.sinhviencntt.controller;

import com.conghoan.sinhviencntt.entity.SinhVien;
import com.conghoan.sinhviencntt.repository.SinhVienRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("SinhVienController - tra cứu sinh viên")
class SinhVienControllerTest {

    @Mock SinhVienRepository repo;
    @InjectMocks SinhVienController controller;

    @Test
    @DisplayName("GET /{msv} trả 200 + entity khi tìm thấy")
    void getByMsv_shouldReturn200WhenFound() {
        SinhVien sv = SinhVien.builder().maSinhVien("A38200").build();
        when(repo.findByMaSinhVien("A38200")).thenReturn(Optional.of(sv));

        ResponseEntity<?> resp = controller.getByMsv("A38200");

        assertThat(resp.getBody()).isEqualTo(sv);
        assertThat(resp.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("GET /{msv} trả 404 khi không tìm thấy")
    void getByMsv_shouldReturn404WhenNotFound() {
        when(repo.findByMaSinhVien("XXX")).thenReturn(Optional.empty());

        ResponseEntity<?> resp = controller.getByMsv("XXX");

        assertThat(resp.getStatusCode().value()).isEqualTo(404);
    }

    @Test
    @DisplayName("GET /lop/{lop} trả list theo lopChuyenNganh")
    void getByLop_shouldReturnList() {
        when(repo.findByLopChuyenNganh("TA33c1"))
                .thenReturn(List.of(new SinhVien(), new SinhVien()));

        ResponseEntity<?> resp = controller.getByLop("TA33c1");

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat((List<?>) resp.getBody()).hasSize(2);
    }

    @Test
    @DisplayName("GET /count trả map có key 'total'")
    void count_shouldReturnTotal() {
        when(repo.count()).thenReturn(1922L);

        ResponseEntity<?> resp = controller.count();

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) resp.getBody();
        assertThat(body).containsEntry("total", 1922L);
    }

    @Test
    @DisplayName("GET /search?ten=... gọi đúng method repo")
    void search_shouldDelegateToRepo() {
        when(repo.findByTenContainingIgnoreCaseOrMaSinhVienContainingIgnoreCase("đăng", "123"))
                .thenReturn(List.of(SinhVien.builder().ten("Đăng").build()));

        ResponseEntity<?> resp = controller.search("123", "đăng");

        assertThat(resp.getStatusCode().value()).isEqualTo(200);
        assertThat((List<?>) resp.getBody()).hasSize(1);
    }
}
