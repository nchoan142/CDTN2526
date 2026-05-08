package com.conghoan.sinhviencntt.model;

public class DanhMucModel {
    private Long id;
    private Integer stt;
    private String tenVietTat;
    private String tenDayDu;
    private String linkAnh;
    private String linkTruyCap;
    private Boolean dangDung;
    private Long danhMucCha;
    private String loai;       // BUILTIN | WEBVIEW
    private String maManHinh;  // PROFILE | TKB | BANGDIEM | KHOA | PDT | TRUONG | THONGTIN | HOIDAP

    public Long getId() { return id; }
    public Integer getStt() { return stt; }
    public String getTenVietTat() { return tenVietTat; }
    public String getTenDayDu() { return tenDayDu; }
    public String getLinkAnh() { return linkAnh; }
    public String getLinkTruyCap() { return linkTruyCap; }
    public Boolean getDangDung() { return dangDung; }
    public Long getDanhMucCha() { return danhMucCha; }
    public String getLoai() { return loai; }
    public String getMaManHinh() { return maManHinh; }
}
