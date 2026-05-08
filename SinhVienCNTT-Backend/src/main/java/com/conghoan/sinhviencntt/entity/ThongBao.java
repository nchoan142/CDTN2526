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

    private String nguoiGui;
    private String doiTuong; // ALL, KHOA_33, LOP_TT33h1, etc.
    private LocalDateTime ngayGui;
    private Boolean daDoc;
    private Boolean ghim;
}
