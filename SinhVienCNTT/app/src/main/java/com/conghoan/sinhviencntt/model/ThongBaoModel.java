package com.conghoan.sinhviencntt.model;

public class ThongBaoModel {
    private Long id;
    private String tieuDe;
    private String noiDung;
    private String nguoiDang;
    private String nguoiGui;
    private String doiTuong;
    private String ngayDang;
    private String ngayGui;
    private Boolean ghim;

    public Long getId() { return id; }
    public String getTieuDe() { return tieuDe; }
    public String getNoiDung() { return noiDung; }
    public String getNguoiDang() { return nguoiDang; }
    public String getNguoiGui() { return nguoiGui; }
    public String getNgayGui() { return ngayGui; }
    public String getDoiTuong() { return doiTuong; }
    public String getNgayDang() { return ngayDang; }
    public Boolean getGhim() { return ghim; }
}
