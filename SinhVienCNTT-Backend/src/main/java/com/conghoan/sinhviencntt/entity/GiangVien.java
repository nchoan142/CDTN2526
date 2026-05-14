package com.conghoan.sinhviencntt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "giang_vien")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class GiangVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_giang_vien", unique = true, nullable = false)
    private String maGiangVien;

    @JsonIgnore
    private String password;
    private String hoTenDem;
    private String ten;
    private String donVi;
    private String dienThoai;
    private String email1;
    private String email2;
    private String ghiChu;
    private String hocHam;
    private String hocVi;
    @Column(name = "giang_vien")
    private Integer roleGiangVien;
    private Integer coHuu1;
    private Integer coHuu2;
    @Column(name = "thinh_giang")
    private Integer roleThinhGiang;
    @Column(name = "thu_ky")
    private Integer roleThuKy;
    @Column(name = "quan_tri")
    private Integer roleQuanTri;
    private Integer trangThai;

    @ManyToOne
    @JoinColumn(name = "nguoi_quan_ly", referencedColumnName = "username")
    private TaiKhoanAdmin adminQuanLy;
}
