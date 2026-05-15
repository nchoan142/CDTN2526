package com.conghoan.sinhviencntt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "co_van_hoc_tap", indexes = { // Tạo index giúp lấy dữ liệu nhanh hơn, thay vì lấy tất cả dữ liệu, ta chỉ lấy dữ liệu từ 2 cột: ma_ky, ma_giang_vien
    @Index(name = "idx_cvht_ky", columnList = "ma_ky"),
    @Index(name = "idx_cvht_gv", columnList = "ma_giang_vien")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CoVanHocTap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_giang_vien")
    private String maGiangVien;

    private String maLop;

    @Column(name = "ma_ky")
    private String maKy;

    private String ghiChu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_giang_vien", referencedColumnName = "ma_giang_vien",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_cvht_gv"))
    @JsonIgnore
    private GiangVien giangVien;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_ky", referencedColumnName = "ma_ky",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_cvht_kyhoc"))
    @JsonIgnore
    private KyHoc kyHoc;
}
