package com.conghoan.sinhviencntt.model;

public class BangDiemModel {
    private Long id;
    private String maSinhVien;
    private String maHocPhan;
    private String tenHocPhan;
    private String maGiangVien;
    private Integer soTinChi;
    private String tienQuyet;
    private String loaiHocPhan;
    private Double diemTongKet;
    private Integer soLanThi;
    private String khoa;
    private String chuyenNganhId;

    public Long getId() { return id; }
    public String getMaSinhVien() { return maSinhVien; }
    public String getMaHocPhan() { return maHocPhan; }
    public String getTenHocPhan() { return tenHocPhan; }
    public String getMaGiangVien() { return maGiangVien; }
    public Integer getSoTinChi() { return soTinChi; }
    public String getTienQuyet() { return tienQuyet; }
    public String getLoaiHocPhan() { return loaiHocPhan; }
    public Double getDiemTongKet() { return diemTongKet; }
    public Integer getSoLanThi() { return soLanThi; }
    public String getKhoa() { return khoa; }
    public String getChuyenNganhId() { return chuyenNganhId; }
}
