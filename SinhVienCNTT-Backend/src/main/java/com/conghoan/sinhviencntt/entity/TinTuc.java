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

    private String nguoiDang;
    private LocalDateTime ngayDang;
    private Boolean hienThi;
}
