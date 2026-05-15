package com.conghoan.sinhviencntt.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "thong_bao")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ThongBao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tieuDe;

    @Column(columnDefinition = "TEXT")
    private String noiDung;

    @Column(name = "nguoi_gui")
    private String nguoiGui;
    private String doiTuong; // ALL, KHOA_33, LOP_TT33h1, etc.
    private LocalDateTime ngayGui;
    private Boolean daDoc;
    private Boolean ghim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nguoi_gui", referencedColumnName = "ma_giang_vien", insertable = false, updatable = false)
    private GiangVien giangVien;
}
