package com.conghoan.sinhviencntt.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStats {
    private long tongSinhVien;
    private long tongGiangVien;
    private long tongKyHoc;
    private long tongHoiDapChuaDuyet;
    private long tongThongBao;
    private long tongDanhMuc;
    private String kyHocHienTai;
}
