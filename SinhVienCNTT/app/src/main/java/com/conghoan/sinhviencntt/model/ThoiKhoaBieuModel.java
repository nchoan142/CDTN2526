package com.conghoan.sinhviencntt.model;

public class ThoiKhoaBieuModel {
    private Long id;
    private String maSinhVien;
    private String maHocPhan;
    private String tenHocPhan;
    private String maGiangVien;
    private String tenGiangVien;
    private String phongHoc;
    private Integer thu;
    private String tietBatDau;
    private String tietKetThuc;
    private String gioBatDau;
    private String gioKetThuc;
    private String maKy;
    private String nhom;

    public Long getId() { return id; }
    public String getTenHocPhan() { return tenHocPhan; }
    public String getTenGiangVien() { return tenGiangVien; }
    public String getPhongHoc() { return phongHoc; }
    public Integer getThu() { return thu; }
    public String getTietBatDau() { return tietBatDau; }
    public String getTietKetThuc() { return tietKetThuc; }
    public String getGioBatDau() { return gioBatDau; }
    public String getMaHocPhan() { return maHocPhan; }
    public String getNhom() { return nhom; }
}
