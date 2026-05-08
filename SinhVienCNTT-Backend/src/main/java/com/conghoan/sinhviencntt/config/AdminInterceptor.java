package com.conghoan.sinhviencntt.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "com.conghoan.sinhviencntt.controller.admin")
public class AdminInterceptor {

    @ModelAttribute("currentPage")
    public String currentPage(HttpServletRequest request) {
        String uri = request.getRequestURI();
        if (uri.contains("/dashboard")) return "dashboard";
        if (uri.contains("/sinhvien")) return "sinhvien";
        if (uri.contains("/giangvien")) return "giangvien";
        if (uri.contains("/kyhoc")) return "kyhoc";
        if (uri.contains("/bangdiem")) return "bangdiem";
        if (uri.contains("/tkb")) return "tkb";
        if (uri.contains("/cvht")) return "cvht";
        if (uri.contains("/thongbao")) return "thongbao";
        if (uri.contains("/hoidap")) return "hoidap";
        if (uri.contains("/danhmuc")) return "danhmuc";
        return "";
    }
}
