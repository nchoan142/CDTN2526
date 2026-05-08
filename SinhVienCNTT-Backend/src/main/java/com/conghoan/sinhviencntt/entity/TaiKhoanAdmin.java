package com.conghoan.sinhviencntt.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tai_khoan_admin")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class TaiKhoanAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String password;
    private String hoTen;
    private String email;
    private String role; // ADMIN, MANAGER
}
