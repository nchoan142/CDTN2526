package com.conghoan.sinhviencntt.config;

import com.conghoan.sinhviencntt.entity.*;
import com.conghoan.sinhviencntt.repository.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    @Value("${app.crawled-data-path}")
    private String crawledDataPath;

    private final SinhVienRepository sinhVienRepo;
    private final GiangVienRepository giangVienRepo;
    private final KyHocRepository kyHocRepo;
    private final BangDiemRepository bangDiemRepo;
    private final CoVanHocTapRepository cvhtRepo;
    private final DanhMucRepository danhMucRepo;
    private final TaiKhoanAdminRepository adminRepo;
    private final ThoiKhoaBieuRepository tkbRepo;
    private final ThongBaoRepository thongBaoRepo;
    private final PasswordEncoder passwordEncoder;
    private final Gson gson = new Gson();

    public DataSeeder(SinhVienRepository sinhVienRepo, GiangVienRepository giangVienRepo,
                      KyHocRepository kyHocRepo, BangDiemRepository bangDiemRepo,
                      CoVanHocTapRepository cvhtRepo, DanhMucRepository danhMucRepo,
                      TaiKhoanAdminRepository adminRepo, ThoiKhoaBieuRepository tkbRepo,
                      ThongBaoRepository thongBaoRepo,
                      PasswordEncoder passwordEncoder) {
        this.sinhVienRepo = sinhVienRepo;
        this.giangVienRepo = giangVienRepo;
        this.kyHocRepo = kyHocRepo;
        this.bangDiemRepo = bangDiemRepo;
        this.cvhtRepo = cvhtRepo;
        this.danhMucRepo = danhMucRepo;
        this.adminRepo = adminRepo;
        this.tkbRepo = tkbRepo;
        this.thongBaoRepo = thongBaoRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        createDefaultAdmin();
//        seedTKB();
//        seedThongBao();
        if (sinhVienRepo.count() == 0) {
            System.out.println(">>> Bat dau import du lieu tu: " + crawledDataPath);
            importSinhVien();
            importGiangVien();
            importKyHoc();
            importCoVanHocTap();
            importDanhMuc();
        } else {
            System.out.println(">>> Sinh vien da ton tai, bo qua seed sinh vien/ky hoc/cvht/danh muc/giang vien.");
        }
        // Bang diem check rieng - import neu chua co data
        if (bangDiemRepo.count() == 0) {
            System.out.println(">>> Bang diem rong, bat dau import bang diem...");
            importBangDiem();
        } else {
            System.out.println(">>> Bang diem da ton tai (" + bangDiemRepo.count() + " dong), bo qua seed bang diem.");
        }
        fixGiangVienPasswords();
        System.out.println(">>> Import du lieu hoan tat!");
    }

    private void fixGiangVienPasswords() {
        // Cập nhật password cho GV chưa có hoặc password chưa mã hóa BCrypt
        List<com.conghoan.sinhviencntt.entity.GiangVien> allGv = giangVienRepo.findAll();
        int fixed = 0;
        for (com.conghoan.sinhviencntt.entity.GiangVien gv : allGv) {
            if (gv.getPassword() == null || gv.getPassword().isEmpty()
                    || !gv.getPassword().startsWith("$2")) {
                gv.setPassword(passwordEncoder.encode(gv.getMaGiangVien()));
                giangVienRepo.save(gv);
                fixed++;
            }
        }
        if (fixed > 0) {
            System.out.println(">>> Cap nhat password cho " + fixed + " giang vien (mat khau = ma GV)");
        }
    }

    private void createDefaultAdmin() {
        if (!adminRepo.existsByUsername("admin")) {
            TaiKhoanAdmin admin = TaiKhoanAdmin.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .hoTen("Quản trị viên")
                    .email("admin@sinhviencntt.vn")
                    .role("ADMIN")
                    .build();
            adminRepo.save(admin);
            System.out.println(">>> Tao tai khoan admin mac dinh: admin / admin123");
        }
    }

    private void importSinhVien() {
        try {
            Path file = Path.of(crawledDataPath, "sinhvien.json");
            if (!Files.exists(file)) return;
            Reader reader = new FileReader(file.toFile());
            Type type = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> list = gson.fromJson(reader, type);
            reader.close();
            for (JsonObject obj : list) {
                String msv = getStr(obj, "maSinhVien");
                if (msv == null || sinhVienRepo.existsByMaSinhVien(msv)) continue;
                SinhVien sv = SinhVien.builder()
                        .maSinhVien(msv)
                        .ten(getStr(obj, "ten"))
                        .hoTenDem(getStr(obj, "hoTenDem"))
                        .ngaySinh(getStr(obj, "ngaySinh"))
                        .nhapHoc(getStr(obj, "nhapHoc"))
                        .lop(getStr(obj, "lop"))
                        .khoa(getStr(obj, "khoa"))
                        .lopChuyenNganh(getStr(obj, "lopChuyenNganh"))
                        .nganh(getStr(obj, "nganh"))
                        .email1(getStr(obj, "email1"))
                        .email2(getStr(obj, "email2"))
                        .dienThoai1(getStr(obj, "dienThoai1"))
                        .dienThoai2(getStr(obj, "dienThoai2"))
                        .ghiChu(getStr(obj, "ghiChu"))
                        .khoaNhapHoc(getStr(obj, "khoaNhapHoc"))
                        .trangThai(getInt(obj, "trangThai"))
                        .build();
                sinhVienRepo.save(sv);
            }
            System.out.println(">>> Import sinh vien: " + sinhVienRepo.count());
        } catch (Exception e) {
            System.err.println("Loi import sinh vien: " + e.getMessage());
        }
    }

    private void importKyHoc() {
        try {
            Path file = Path.of(crawledDataPath, "ky_hoc.json");
            if (!Files.exists(file)) return;
            Reader reader = new FileReader(file.toFile());
            Type type = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> list = gson.fromJson(reader, type);
            reader.close();
            for (JsonObject obj : list) {
                String maKy = getStr(obj, "maKy");
                if (maKy == null || kyHocRepo.existsByMaKy(maKy)) continue;
                KyHoc ky = KyHoc.builder()
                        .maKy(maKy)
                        .tenKy(getStr(obj, "tenKy"))
                        .maNam(getStr(obj, "maNam"))
                        .batDauKyHoc(getStr(obj, "batDauKyHoc"))
                        .ketThucKyHoc(getStr(obj, "ketThucKyHoc"))
                        .batDauChonLich(getStr(obj, "batDauChonLich"))
                        .ketThucChonLich(getStr(obj, "ketThucChonLich"))
                        .batDauLapLich(getStr(obj, "batDauLapLich"))
                        .ketThucLapLich(getStr(obj, "ketThucLapLich"))
                        .lastModify(getStr(obj, "lastModify"))
                        .macDinh(getInt(obj, "macDinh"))
                        .build();
                kyHocRepo.save(ky);
            }
            System.out.println(">>> Import ky hoc: " + kyHocRepo.count());
        } catch (Exception e) {
            System.err.println("Loi import ky hoc: " + e.getMessage());
        }
    }

    private void importCoVanHocTap() {
        try {
            String[] files = {"covanhoctap_2526K1.json", "covanhoctap_2425K3N1.json",
                    "covanhoctap_2425K3N2.json", "covanhoctap_2425K3N3.json"};
            for (String fileName : files) {
                Path file = Path.of(crawledDataPath, fileName);
                if (!Files.exists(file)) continue;
                Reader reader = new FileReader(file.toFile());
                Type type = new TypeToken<List<JsonObject>>() {}.getType();
                List<JsonObject> list = gson.fromJson(reader, type);
                reader.close();
                for (JsonObject obj : list) {
                    String maGv = getStr(obj, "maGiangVien");
                    
                    // Tạo giảng viên tạm nếu mã này chưa tồn tại trong Database
                    if (maGv != null && !maGv.isEmpty() && !giangVienRepo.existsByMaGiangVien(maGv)) {
                        GiangVien placeholder = GiangVien.builder()
                                .maGiangVien(maGv)
                                .ten("(Chưa cập nhật)")
                                .hoTenDem("")
                                .password(passwordEncoder.encode(maGv))
                                .trangThai(1)
                                .build();
                        giangVienRepo.save(placeholder);
                    }

                    CoVanHocTap cv = CoVanHocTap.builder()
                            .maGiangVien(maGv)
                            .maLop(getStr(obj, "maLop"))
                            .maKy(getStr(obj, "maKy"))
                            .ghiChu(getStr(obj, "ghiChu"))
                            .build();
                    cvhtRepo.save(cv);
                }
            }
            System.out.println(">>> Import CVHT: " + cvhtRepo.count());
        } catch (Exception e) {
            System.err.println("Loi import CVHT: " + e.getMessage());
        }
    }

    private void importDanhMuc() {
        try {
            Path file = Path.of(crawledDataPath, "danh_muc.json");
            if (!Files.exists(file)) return;
            Reader reader = new FileReader(file.toFile());
            Type type = new TypeToken<List<JsonObject>>() {}.getType();
            List<JsonObject> list = gson.fromJson(reader, type);
            reader.close();
            for (JsonObject obj : list) {
                String tenVT = getStr(obj, "tenVietTat");
                DanhMuc dm = DanhMuc.builder()
                        .dangDung(obj.has("dang_dung") && !obj.get("dang_dung").isJsonNull() ? obj.get("dang_dung").getAsBoolean() : null)
                        .danhMucCha(getLong(obj, "danh_muc_cha"))
                        .linkAnh(getStr(obj, "link_anh"))
                        .linkTruyCap(getStr(obj, "link_truy_cap"))
                        .nguoiQuanLy(getStr(obj, "nguoi_quan_ly"))
                        .stt(getInt(obj, "stt"))
                        .tenDayDu(getStr(obj, "ten_day_du"))
                        .tenVietTat(getStr(obj, "ten_viet_tat"))
                        .loai("BUILTIN")
                        .maManHinh(mapTenVietTatToMaManHinh(tenVT))
                        .build();
                danhMucRepo.save(dm);
            }
            System.out.println(">>> Import danh muc: " + danhMucRepo.count());
        } catch (Exception e) {
            System.err.println("Loi import danh muc: " + e.getMessage());
        }
    }

    private void importGiangVien() {
        try {
            Path file = Path.of(crawledDataPath, "giang_vien.json");
            if (!Files.exists(file)) return;

            Reader reader = new FileReader(file.toFile());
            JsonObject wrapper = gson.fromJson(reader, JsonObject.class);
            reader.close();

            if (!wrapper.has("data") || wrapper.get("data").isJsonNull()) return;
            JsonArray dataArray = wrapper.getAsJsonArray("data");

            for (JsonElement element : dataArray) {
                JsonObject obj = element.getAsJsonObject();
                String maGv = getStr(obj, "ma_giang_vien"); // Chú ý: JSON của bạn dùng "ma_giang_vien"

                if (maGv == null) continue;

                GiangVien gv = giangVienRepo.findByMaGiangVien(maGv).orElse(new GiangVien());
                String usernameAdmin = getStr(obj, "nguoi_quan_ly");
                TaiKhoanAdmin admin = adminRepo.findByUsername(usernameAdmin).orElse(null);
                if (admin != null) {
                    gv.setAdminQuanLy(admin);
                }


                gv.setMaGiangVien(maGv);
                if (gv.getPassword() == null) gv.setPassword(passwordEncoder.encode(maGv));
                gv.setTen(getStr(obj, "ten"));
                gv.setHoTenDem(getStr(obj, "ho_ten_dem"));
                gv.setDonVi(getStr(obj, "don_vi"));
                gv.setDienThoai(getStr(obj, "dien_thoai"));
                gv.setEmail1(getStr(obj, "email1"));
                gv.setEmail2(getStr(obj, "email2"));
                gv.setGhiChu(getStr(obj, "ghi_chu"));
                gv.setHocHam(getStr(obj, "hoc_ham"));
                gv.setHocVi(getStr(obj, "hoc_vi"));
                gv.setRoleGiangVien(getInt(obj, "giang_vien"));
                gv.setCoHuu1(getInt(obj, "co_huu1"));
                gv.setCoHuu2(getInt(obj, "co_huu2"));
                gv.setRoleThinhGiang(getInt(obj, "thinh_giang"));
                gv.setRoleThuKy(getInt(obj, "thu_ky"));
                gv.setRoleQuanTri(getInt(obj, "quan_tri"));
                gv.setTrangThai(getInt(obj, "trang_thai"));

                giangVienRepo.save(gv);
            }
            System.out.println(">>> Import giang vien: " + giangVienRepo.count());
        } catch (Exception e) {
            System.err.println("Loi import giang vien: " + e.getMessage());
            e.printStackTrace();
        }
    }

//    private void importGiangVien() {
//        try {
//            Path file = Path.of(crawledDataPath, "giang_vien.json");
//            if (!Files.exists(file)) return;
//            Reader reader = new FileReader(file.toFile());
//            JsonObject wrapper = gson.fromJson(reader, JsonObject.class);
//            reader.close();
//            String extentStr = getStr(wrapper, "extent");
//            if (extentStr == null) return;
//            JsonObject obj = gson.fromJson(extentStr, JsonObject.class);
//            String maGv = getStr(obj, "maGiangVien");
//            if (maGv == null || giangVienRepo.existsByMaGiangVien(maGv)) return;
//            GiangVien gv = GiangVien.builder()
//                    .maGiangVien(maGv)
//                    .password(passwordEncoder.encode(maGv)) // Mật khẩu mặc định = mã giảng viên
//                    .ten(getStr(obj, "ten"))
//                    .hoTenDem(getStr(obj, "hoTenDem"))
//                    .donVi(getStr(obj, "donVi"))
//                    .dienThoai(getStr(obj, "dienThoai"))
//                    .email1(getStr(obj, "email1"))
//                    .email2(getStr(obj, "email2"))
//                    .ghiChu(getStr(obj, "ghiChu"))
//                    .hocHam(getStr(obj, "hocHam"))
//                    .hocVi(getStr(obj, "hocVi"))
//                    .roleGiangVien(getInt(obj, "giangVien"))
//                    .coHuu1(getInt(obj, "coHuu1"))
//                    .coHuu2(getInt(obj, "coHuu2"))
//                    .roleThinhGiang(getInt(obj, "thinhGiang"))
//                    .roleThuKy(getInt(obj, "thuKy"))
//                    .roleQuanTri(getInt(obj, "quanTri"))
//                    .trangThai(getInt(obj, "trangThai"))
//                    .adminQuanLy(getStr(obj, "nguoi_quan_ly"))
//                    .build();
//            giangVienRepo.save(gv);
//            System.out.println(">>> Import giang vien: " + giangVienRepo.count());
//        } catch (Exception e) {
//            System.err.println("Loi import giang vien: " + e.getMessage());
//        }
//    }

    private void importBangDiem() {
        try {
            java.io.File dir = new java.io.File(crawledDataPath);
            java.io.File[] bangDiemFiles = dir.listFiles((d, name) ->
                    name.startsWith("bangdiem_") && name.endsWith(".json"));
            if (bangDiemFiles == null || bangDiemFiles.length == 0) return;

            // Pre-load existing ma_giang_vien và ma_sinh_vien
            java.util.Set<String> existingGv = new java.util.HashSet<>();
            giangVienRepo.findAll().forEach(gv -> existingGv.add(gv.getMaGiangVien()));
            java.util.Set<String> existingSv = new java.util.HashSet<>();
            sinhVienRepo.findAll().forEach(sv -> existingSv.add(sv.getMaSinhVien()));

            // Pass 1: scan all files để collect unique ma_giang_vien chưa có
            java.util.Set<String> newGvCodes = new java.util.HashSet<>();
            for (java.io.File bdFile : bangDiemFiles) {
                String msv = bdFile.getName().replace("bangdiem_", "").replace(".json", "");
                if (!existingSv.contains(msv)) continue;
                try (Reader r = new FileReader(bdFile)) {
                    Type t = new TypeToken<List<JsonObject>>() {}.getType();
                    List<JsonObject> list = gson.fromJson(r, t);
                    if (list == null) continue;
                    for (JsonObject obj : list) {
                        String maGv = getStr(obj, "maGiangVien");
                        // Chuyển chuỗi rỗng thành null
                        if (maGv != null && maGv.trim().isEmpty()) maGv = null;
                        
                        if (maGv != null && !maGv.isEmpty() && !existingGv.contains(maGv)) {
                            newGvCodes.add(maGv);
                        }
                    }
                } catch (Exception ignore) {}
            }
            // Bulk insert placeholder GV trước khi insert bang_diem
            for (String maGv : newGvCodes) {
                GiangVien placeholder = GiangVien.builder()
                        .maGiangVien(maGv)
                        .ten("(Chưa cập nhật)")
                        .hoTenDem("")
                        .password(passwordEncoder.encode(maGv))
                        .trangThai(1)
                        .build();
                giangVienRepo.save(placeholder);
                existingGv.add(maGv);
            }
            giangVienRepo.flush();
            int gvPlaceholders = newGvCodes.size();

            // Pass 2: insert bang_diem
            int count = 0;
            int skippedNoSv = 0;
            for (java.io.File bdFile : bangDiemFiles) {
                String msv = bdFile.getName().replace("bangdiem_", "").replace(".json", "");
                if (!existingSv.contains(msv)) { skippedNoSv++; continue; }
                Reader reader = new FileReader(bdFile);
                Type type = new TypeToken<List<JsonObject>>() {}.getType();
                List<JsonObject> list = gson.fromJson(reader, type);
                reader.close();
                if (list == null || list.isEmpty()) continue;
                for (JsonObject obj : list) {
                    String maGv = getStr(obj, "maGiangVien");
                    // Chuyển chuỗi rỗng thành null
                    if (maGv != null && maGv.trim().isEmpty()) maGv = null;
                    
                    BangDiem bd = BangDiem.builder()
                            .maSinhVien(msv)
                            .maHocPhan(getStr(obj, "maHocPhan"))
                            .tenHocPhan(getStr(obj, "tenHocPhan"))
                            .maGiangVien(maGv)
                            .soTinChi(getInt(obj, "soTinChi"))
                            .tienQuyet(getStr(obj, "tienQuyet"))
                            .loaiHocPhan(getStr(obj, "loaiHocPhan"))
                            .diemTongKet(getDouble(obj, "diemTongKet"))
                            .soLanThi(getInt(obj, "soLanThi"))
                            .khoa(getStr(obj, "khoa"))
                            .chuyenNganhId(getStr(obj, "chuyenNganhId"))
                            .build();
                    bangDiemRepo.save(bd);
                    count++;
                }
            }
            System.out.println(">>> Import bang diem: " + count + " dong tu " + bangDiemFiles.length + " file");
        } catch (Exception e) {
            System.err.println("Loi import bang diem: " + e.getMessage());
        }
    }

    private void seedTKB() {
        if (tkbRepo.count() > 0) return;
        String maKy = "2526K2";
        String msv = "A38200";
        tkbRepo.save(ThoiKhoaBieu.builder().maSinhVien(msv).maHocPhan("CS410").tenHocPhan("Lập trình Java nâng cao").maGiangVien("CTI064").tenGiangVien("ThS. Nguyễn Văn Hùng").phongHoc("A3-302").thu(2).tietBatDau("1").tietKetThuc("3").gioBatDau("7:00").gioKetThuc("9:30").maKy(maKy).nhom("01").build());
        tkbRepo.save(ThoiKhoaBieu.builder().maSinhVien(msv).maHocPhan("IS222").tenHocPhan("Cơ sở dữ liệu").maGiangVien("CTI063").tenGiangVien("TS. Trần Thị Mai").phongHoc("B2-105").thu(2).tietBatDau("4").tietKetThuc("6").gioBatDau("9:30").gioKetThuc("12:00").maKy(maKy).nhom("01").build());
        tkbRepo.save(ThoiKhoaBieu.builder().maSinhVien(msv).maHocPhan("NW212").tenHocPhan("Mạng máy tính").maGiangVien("CTI030").tenGiangVien("ThS. Lê Đức Anh").phongHoc("C1-201").thu(3).tietBatDau("1").tietKetThuc("3").gioBatDau("7:00").gioKetThuc("9:30").maKy(maKy).nhom("01").build());
        tkbRepo.save(ThoiKhoaBieu.builder().maSinhVien(msv).maHocPhan("AI220").tenHocPhan("Trí tuệ nhân tạo").maGiangVien("PAI005").tenGiangVien("TS. Phạm Minh Tuấn").phongHoc("A3-401").thu(4).tietBatDau("1").tietKetThuc("3").gioBatDau("7:00").gioKetThuc("9:30").maKy(maKy).nhom("01").build());
        tkbRepo.save(ThoiKhoaBieu.builder().maSinhVien(msv).maHocPhan("IS345").tenHocPhan("An toàn thông tin").maGiangVien("CTI030").tenGiangVien("ThS. Lê Đức Anh").phongHoc("B2-301").thu(4).tietBatDau("4").tietKetThuc("6").gioBatDau("9:30").gioKetThuc("12:00").maKy(maKy).nhom("01").build());
        tkbRepo.save(ThoiKhoaBieu.builder().maSinhVien(msv).maHocPhan("MA234").tenHocPhan("Thống kê ứng dụng").maGiangVien("MTI058").tenGiangVien("ThS. Hoàng Văn Nam").phongHoc("A2-101").thu(5).tietBatDau("7").tietKetThuc("9").gioBatDau("13:00").gioKetThuc("15:30").maKy(maKy).nhom("01").build());
        tkbRepo.save(ThoiKhoaBieu.builder().maSinhVien(msv).maHocPhan("CF231").tenHocPhan("Kiến trúc máy tính").maGiangVien("CTI057").tenGiangVien("TS. Trần Văn Bình").phongHoc("C1-102").thu(6).tietBatDau("1").tietKetThuc("3").gioBatDau("7:00").gioKetThuc("9:30").maKy(maKy).nhom("01").build());
        System.out.println(">>> Seed TKB: " + tkbRepo.count());
    }

    private void seedThongBao() {
        if (thongBaoRepo.count() > 0) return;
        thongBaoRepo.save(ThongBao.builder().tieuDe("Lịch đăng ký học phần kỳ 2 năm 2025-2026").noiDung("Sinh viên đăng ký học phần từ ngày 26/01/2026 đến 25/02/2026. Vui lòng đăng ký đúng hạn.").nguoiGui("Phòng Đào tạo").doiTuong("ALL").ngayGui(java.time.LocalDateTime.of(2026,1,20,8,0)).ghim(true).daDoc(false).build());
        thongBaoRepo.save(ThongBao.builder().tieuDe("Thông báo nghỉ lễ 30/4 - 1/5").noiDung("Trường thông báo lịch nghỉ lễ 30/4 và 1/5. SV nghỉ học từ 30/04 đến 01/05/2026.").nguoiGui("Ban Giám hiệu").doiTuong("ALL").ngayGui(java.time.LocalDateTime.of(2026,4,10,9,0)).ghim(false).daDoc(false).build());
        thongBaoRepo.save(ThongBao.builder().tieuDe("Nhắc nhở nộp học phí kỳ 2").noiDung("SV chưa hoàn thành học phí kỳ 2 vui lòng nộp trước 28/02/2026. Sau thời hạn sẽ bị khoá đăng ký.").nguoiGui("Phòng Tài chính").doiTuong("ALL").ngayGui(java.time.LocalDateTime.of(2026,2,15,10,0)).ghim(true).daDoc(false).build());
        thongBaoRepo.save(ThongBao.builder().tieuDe("Tuyển dụng thực tập sinh CNTT tại FPT Software").noiDung("FPT Software tuyển thực tập sinh kỳ hè 2026. Ưu tiên SV năm 3-4 ngành CNTT. Nộp CV trước 15/05/2026.").nguoiGui("Khoa CNTT").doiTuong("ALL").ngayGui(java.time.LocalDateTime.of(2026,4,5,14,0)).ghim(false).daDoc(false).build());
        System.out.println(">>> Seed thong bao: " + thongBaoRepo.count());
    }

    private String mapTenVietTatToMaManHinh(String tenVT) {
        if (tenVT == null) return null;
        String t = tenVT.trim().toUpperCase();
        switch (t) {
            case "Thông tin": case "HSCN": case "PROFILE": case "HOSO": return "PROFILE";
            case "TKB": case "LICH": return "TKB";
            case "BD": case "BANGDIEM": case "DIEM": return "BANGDIEM";
            case "KHOA": case "CNTT": return "KHOA";
            case "PDT": case "DAOTAO": return "PDT";
            case "TRUONG": case "DHTL": return "TRUONG";
            case "TT": case "THONGTIN": return "THONGTIN";
            case "HD": case "HOIDAP": return "HOIDAP";
            default: return null;
        }
    }

    private String getStr(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) return null;
        return obj.get(key).getAsString();
    }

    private Integer getInt(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) return null;
        return obj.get(key).getAsInt();
    }

    private Long getLong(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) return null;
        long v = obj.get(key).getAsLong();
        return v == 0L ? null : v;
    }

    private Double getDouble(JsonObject obj, String key) {
        if (!obj.has(key) || obj.get(key).isJsonNull()) return null;
        return obj.get(key).getAsDouble();
    }
}