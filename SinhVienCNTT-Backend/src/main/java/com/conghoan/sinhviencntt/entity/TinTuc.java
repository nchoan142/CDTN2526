package com.conghoan.sinhviencntt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tin_tuc")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TinTuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tieuDe;

    @Column(columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "nguoi_dang")
    private String nguoiDang;
    private LocalDateTime ngayDang;
    private Boolean hienThi;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_dang", referencedColumnName = "ma_giang_vien", insertable = false, updatable = false)
    private GiangVien giangVien;
}
