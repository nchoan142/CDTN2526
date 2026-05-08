package com.conghoan.sinhviencntt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "thoi_khoa_bieu", indexes = {
    @Index(name = "idx_tkb_msv", columnList = "ma_sinh_vien"),
    @Index(name = "idx_tkb_ky", columnList = "ma_ky")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThoiKhoaBieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sinh_vien")
    private String maSinhVien;

    private String maHocPhan;
    private String tenHocPhan;

    @Column(name = "ma_giang_vien")
    private String maGiangVien;

    private String tenGiangVien;
    private String phongHoc;
    private Integer thu;
    private String tietBatDau;
    private String tietKetThuc;
    private String gioBatDau;
    private String gioKetThuc;

    @Column(name = "ma_ky")
    private String maKy;

    private String nhom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sinh_vien", referencedColumnName = "ma_sinh_vien",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_tkb_sinhvien"))
    @JsonIgnore
    private SinhVien sinhVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_ky", referencedColumnName = "ma_ky",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_tkb_kyhoc"))
    @JsonIgnore
    private KyHoc kyHoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_giang_vien", referencedColumnName = "ma_giang_vien",
                insertable = false, updatable = false)
    @JsonIgnore
    private GiangVien giangVien;
}
