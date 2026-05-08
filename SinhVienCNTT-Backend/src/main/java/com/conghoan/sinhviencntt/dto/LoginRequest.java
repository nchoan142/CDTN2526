package com.conghoan.sinhviencntt.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LoginRequest {
    private String maSinhVien;
    private String password;
}
