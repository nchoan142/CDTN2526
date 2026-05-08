package com.conghoan.sinhviencntt.repository;

import com.conghoan.sinhviencntt.entity.TaiKhoanAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaiKhoanAdminRepository extends JpaRepository<TaiKhoanAdmin, Long> {
    Optional<TaiKhoanAdmin> findByUsername(String username);
    boolean existsByUsername(String username);
}
