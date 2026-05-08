package com.conghoan.sinhviencntt.util;

import android.content.Context;
import android.content.Intent;

import com.conghoan.sinhviencntt.activity.BangDiemActivity;
import com.conghoan.sinhviencntt.activity.HoiDapActivity;
import com.conghoan.sinhviencntt.activity.KhoaCNTTActivity;
import com.conghoan.sinhviencntt.activity.PhongDaoTaoActivity;
import com.conghoan.sinhviencntt.activity.TKBActivity;
import com.conghoan.sinhviencntt.activity.ThongTinCaNhanActivity;
import com.conghoan.sinhviencntt.activity.ThongTinKhacActivity;
import com.conghoan.sinhviencntt.activity.TruongDHTLActivity;

public class ScreenRouter {

    private ScreenRouter() {}

    public static Intent intentFor(Context context, String maManHinh) {
        if (maManHinh == null) return null;
        Class<?> target;
        switch (maManHinh.toUpperCase()) {
            case "PROFILE":  target = ThongTinCaNhanActivity.class; break;
            case "TKB":      target = TKBActivity.class; break;
            case "BANGDIEM": target = BangDiemActivity.class; break;
            case "KHOA":     target = KhoaCNTTActivity.class; break;
            case "PDT":      target = PhongDaoTaoActivity.class; break;
            case "TRUONG":   target = TruongDHTLActivity.class; break;
            case "THONGTIN": target = ThongTinKhacActivity.class; break;
            case "HOIDAP":   target = HoiDapActivity.class; break;
            default: return null;
        }
        return new Intent(context, target);
    }
}
