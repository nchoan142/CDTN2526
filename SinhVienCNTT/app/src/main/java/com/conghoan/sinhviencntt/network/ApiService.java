package com.conghoan.sinhviencntt.network;

import com.conghoan.sinhviencntt.model.*;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // Auth
    @POST("api/auth/login")
    Call<ApiResponse<LoginResponse>> login(@Body Map<String, String> body);

    @POST("api/auth/register")
    Call<ApiResponse<String>> register(@Body Map<String, String> body);

    @POST("api/auth/tao-tai-khoan")
    Call<ApiResponse<String>> taoTaiKhoan(@Body Map<String, String> body);

    @POST("api/auth/change-password")
    Call<ApiResponse<String>> changePassword(@Body Map<String, String> body);

    // Sinh vien
    @GET("api/v1/sinhvien/{msv}")
    Call<Map<String, Object>> getSinhVien(@Path("msv") String msv);

    @GET("api/v1/sinhvien/count")
    Call<Map<String, Object>> getSinhVienCount();

    // Bang diem
    @GET("api/v1/bangdiem/{msv}")
    Call<List<BangDiemModel>> getBangDiem(@Path("msv") String msv);

    // Thoi khoa bieu
    @GET("api/v1/tkb/{msv}")
    Call<List<ThoiKhoaBieuModel>> getTKB(@Path("msv") String msv);

    // Giang vien
    @GET("api/v1/giangvien")
    Call<List<GiangVienModel>> getGiangVien();

    // Thong bao
    @GET("api/v1/thongbao")
    Call<List<ThongBaoModel>> getThongBao();

    // Hoi dap - lấy Q&A của chính sinh viên đó
    @GET("api/v1/hoidap/sinhvien/{msv}")
    Call<List<HoiDapModel>> getHoiDapByMsv(@Path("msv") String msv);

    @POST("api/v1/hoidap")
    Call<ApiResponse<String>> guiCauHoi(@Body Map<String, String> body);

    // Tin tuc khoa CNTT
    @GET("api/v1/tintuc")
    Call<List<ThongBaoModel>> getTinTuc();

    // CVHT cua sinh vien
    @GET("api/v1/covanhoctap/sinhvien/{msv}")
    Call<Map<String, String>> getCvhtOfSinhVien(@Path("msv") String msv);

    // Danh muc - dashboard động
    @GET("api/v1/danhmuc/active")
    Call<List<DanhMucModel>> getDanhMucActive();
}
