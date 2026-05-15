package com.conghoan.sinhviencntt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "danh_muc")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer stt;
    private String tenVietTat;
    private String tenDayDu;

    @Column(length = 500)
    private String linkAnh;

    @Column(length = 500)
    private String linkTruyCap;

    @Column(name = "nguoi_quan_ly")
    private String nguoiQuanLy;
    private Boolean dangDung;

    @Column(name = "danh_muc_cha")
    private Long danhMucCha;

    @Column(length = 20, nullable = false)
    @Builder.Default
    private String loai = "BUILTIN";

    @Column(length = 50)
    private String maManHinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_quan_ly", referencedColumnName = "ma_giang_vien", insertable = false, updatable = false)
    private GiangVien giangVienQuanLy;
}
