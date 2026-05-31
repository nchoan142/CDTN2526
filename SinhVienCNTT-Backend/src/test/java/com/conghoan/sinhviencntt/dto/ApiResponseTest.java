package com.conghoan.sinhviencntt.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("ApiResponse - factory methods")
class ApiResponseTest {

    @Test
    @DisplayName("success() trả về code=0, message='Thành công' và data đúng")
    void success_shouldReturnCodeZeroAndData() {
        ApiResponse<String> response = ApiResponse.success("hello");

        assertThat(response.getCode()).isEqualTo(0);
        assertThat(response.getMessage()).isEqualTo("Thành công");
        assertThat(response.getData()).isEqualTo("hello");
    }

    @Test
    @DisplayName("success() với data null vẫn hợp lệ")
    void success_withNullData() {
        ApiResponse<Object> response = ApiResponse.success(null);

        assertThat(response.getCode()).isZero();
        assertThat(response.getData()).isNull();
    }

    @Test
    @DisplayName("error() trả về code=1, message tuỳ ý và data null")
    void error_shouldReturnCodeOneAndMessage() {
        ApiResponse<Object> response = ApiResponse.error("Lỗi truy vấn");

        assertThat(response.getCode()).isEqualTo(1);
        assertThat(response.getMessage()).isEqualTo("Lỗi truy vấn");
        assertThat(response.getData()).isNull();
    }
}
