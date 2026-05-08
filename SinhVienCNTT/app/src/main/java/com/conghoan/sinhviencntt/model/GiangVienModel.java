package com.conghoan.sinhviencntt.model;

public class GiangVienModel {
    private Long id;
    private String maGiangVien;
    private String hoTenDem;
    private String ten;
    private String donVi;
    private String dienThoai;
    private String email1;
    private String hocHam;
    private String hocVi;
    private Integer quanTri;

    public Long getId() { return id; }
    public String getMaGiangVien() { return maGiangVien; }
    public String getTen() { return ten; }
    public String getHoTenDem() { return hoTenDem; }
    public String getDonVi() { return donVi; }
    public String getDienThoai() { return dienThoai; }
    public String getEmail1() { return email1; }
    public String getHocVi() { return hocVi; }
    public String getHocHam() { return hocHam; }
    public String getFullName() {
        String prefix = hocVi != null ? hocVi + ". " : "";
        return prefix + (ten != null ? ten : "");
    }
}
