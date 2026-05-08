package com.conghoan.sinhviencntt.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EmailService {

    @Value("${app.resend.api-key}")
    private String apiKey;

    @Value("${app.resend.from}")
    private String fromEmail;

    @Value("${app.resend.from-name}")
    private String fromName;

    public boolean sendPassword(String toEmail, String maSinhVien, String password) {
        try {
            String htmlBody = "<div style='font-family:Arial,sans-serif;max-width:500px;margin:0 auto;'>"
                    + "<div style='background:#1B2A4A;padding:20px;text-align:center;border-radius:8px 8px 0 0;'>"
                    + "<h2 style='color:#fff;margin:0;'>SV CNTT - Thăng Long University</h2></div>"
                    + "<div style='padding:24px;background:#fff;border:1px solid #e0e0e0;'>"
                    + "<p>Xin chào sinh viên <b>" + maSinhVien + "</b>,</p>"
                    + "<p>Tài khoản của bạn đã được tạo thành công.</p>"
                    + "<div style='background:#f5f5f5;padding:16px;border-radius:8px;margin:16px 0;'>"
                    + "<p style='margin:4px 0;'><b>Mã sinh viên:</b> " + maSinhVien + "</p>"
                    + "<p style='margin:4px 0;'><b>Mật khẩu:</b> <code style='background:#e8e8e8;padding:2px 8px;border-radius:4px;font-size:16px;'>" + password + "</code></p>"
                    + "</div>"
                    + "<p style='color:#666;font-size:13px;'>Vui lòng đăng nhập và đổi mật khẩu ngay sau lần đầu sử dụng.</p>"
                    + "</div>"
                    + "<div style='background:#f9f9f9;padding:12px;text-align:center;border-radius:0 0 8px 8px;border:1px solid #e0e0e0;border-top:none;'>"
                    + "<p style='color:#999;font-size:11px;margin:0;'>Email tự động - Không trả lời email này</p></div></div>";

            String json = String.format(
                    "{\"from\":\"%s <%s>\",\"to\":[\"%s\"],\"subject\":\"Tài khoản SV CNTT - %s\",\"html\":\"%s\"}",
                    fromName, fromEmail, toEmail, maSinhVien, htmlBody.replace("\"", "\\\"")
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(">>> Resend response: " + response.statusCode() + " - " + response.body());
            return response.statusCode() == 200;
        } catch (Exception e) {
            System.err.println("Loi gui email: " + e.getMessage());
            return false;
        }
    }
}
