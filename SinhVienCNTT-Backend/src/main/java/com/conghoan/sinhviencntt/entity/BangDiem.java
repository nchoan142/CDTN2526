package com.conghoan.sinhviencntt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bang_diem", indexes = {
    @Index(name = "idx_bd_msv", columnList = "ma_sinh_vien")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BangDiem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sinh_vien")
    private String maSinhVien;

    private String maHocPhan;
    private String tenHocPhan;

    @Column(name = "ma_giang_vien")
    private String maGiangVien;

    private Integer soTinChi;
    private String tienQuyet;
    private String loaiHocPhan;
    private Double diemTongKet;
    private Integer soLanThi;
    private String khoa;
    private String chuyenNganhId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sinh_vien", referencedColumnName = "ma_sinh_vien",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_bangdiem_sinhvien"))
    @JsonIgnore
    private SinhVien sinhVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_giang_vien", referencedColumnName = "ma_giang_vien",
                insertable = false, updatable = false)
    @JsonIgnore
    private GiangVien giangVien;
}
