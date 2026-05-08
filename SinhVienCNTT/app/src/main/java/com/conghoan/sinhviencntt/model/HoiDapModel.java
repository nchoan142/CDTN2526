package com.conghoan.sinhviencntt.model;

public class HoiDapModel {
    private Long id;
    private String maSinhVien;
    private String tenSinhVien;
    private String cauHoi;
    private String cauTraLoi;
    private String nguoiTraLoi;
    private String ngayHoi;
    private String ngayTraLoi;
    private Boolean daDuyet;

    public Long getId() { return id; }
    public String getMaSinhVien() { return maSinhVien; }
    public String getTenSinhVien() { return tenSinhVien; }
    public String getCauHoi() { return cauHoi; }
    public String getCauTraLoi() { return cauTraLoi; }
    public String getNguoiTraLoi() { return nguoiTraLoi; }
    public String getNgayHoi() { return ngayHoi; }
    public String getNgayTraLoi() { return ngayTraLoi; }
    public Boolean getDaDuyet() { return daDuyet; }
}
