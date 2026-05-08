package com.conghoan.sinhviencntt.model;

public class DashboardItem {
    private String title;
    private String subtitle;
    private String linkAnh;       // URL ảnh, nullable - sẽ fallback drawable mặc định
    private int fallbackIconRes;  // drawable fallback
    private int bgColorResId;
    private int iconColorResId;
    private String loai;          // BUILTIN | WEBVIEW
    private String maManHinh;     // chỉ khi BUILTIN
    private String linkTruyCap;   // chỉ khi WEBVIEW

    public DashboardItem(String title, String subtitle, String linkAnh, int fallbackIconRes,
                         int bgColorResId, int iconColorResId,
                         String loai, String maManHinh, String linkTruyCap) {
        this.title = title;
        this.subtitle = subtitle;
        this.linkAnh = linkAnh;
        this.fallbackIconRes = fallbackIconRes;
        this.bgColorResId = bgColorResId;
        this.iconColorResId = iconColorResId;
        this.loai = loai;
        this.maManHinh = maManHinh;
        this.linkTruyCap = linkTruyCap;
    }

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getLinkAnh() { return linkAnh; }
    public int getFallbackIconRes() { return fallbackIconRes; }
    public int getBgColorResId() { return bgColorResId; }
    public int getIconColorResId() { return iconColorResId; }
    public String getLoai() { return loai; }
    public String getMaManHinh() { return maManHinh; }
    public String getLinkTruyCap() { return linkTruyCap; }
}
