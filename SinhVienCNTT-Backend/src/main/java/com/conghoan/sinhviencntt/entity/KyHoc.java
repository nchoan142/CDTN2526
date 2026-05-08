package com.conghoan.sinhviencntt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ky_hoc")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class KyHoc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_ky", unique = true, nullable = false)
    private String maKy;

    private String tenKy;
    private String maNam;
    private String batDauKyHoc;
    private String ketThucKyHoc;
    private String batDauChonLich;
    private String ketThucChonLich;
    private String batDauLapLich;
    private String ketThucLapLich;
    private String lastModify;
    private Integer macDinh;
}
