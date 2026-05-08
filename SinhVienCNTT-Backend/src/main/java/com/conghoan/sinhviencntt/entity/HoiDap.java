package com.conghoan.sinhviencntt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hoi_dap", indexes = {
    @Index(name = "idx_hd_msv", columnList = "ma_sinh_vien")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class HoiDap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_sinh_vien")
    private String maSinhVien;

    private String tenSinhVien;

    @Column(columnDefinition = "TEXT")
    private String cauHoi;

    @Column(columnDefinition = "TEXT")
    private String cauTraLoi;

    private String nguoiTraLoi;
    private LocalDateTime ngayHoi;
    private LocalDateTime ngayTraLoi;
    private Boolean daDuyet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sinh_vien", referencedColumnName = "ma_sinh_vien",
            insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "fk_hoidap_sinhvien"))
    @JsonIgnore
    private SinhVien sinhVien;
}
