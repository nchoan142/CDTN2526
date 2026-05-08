-- Migration 2026-05-08: dashboard động + bỏ bieu_mau + thêm FK
-- Chạy: mysql -u root -proot sinhviencntt < migration_2026_05_08.sql

START TRANSACTION;

-- 1. Thêm cột loai, ma_man_hinh vào danh_muc (idempotent cho MySQL 8.0)
SET @col_loai := (SELECT COUNT(*) FROM information_schema.COLUMNS
                  WHERE TABLE_SCHEMA = DATABASE()
                    AND TABLE_NAME = 'danh_muc' AND COLUMN_NAME = 'loai');
SET @sql := IF(@col_loai = 0,
  'ALTER TABLE danh_muc ADD COLUMN loai VARCHAR(20) NOT NULL DEFAULT ''BUILTIN''',
  'SELECT ''column loai already exists'' AS msg');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

SET @col_mmh := (SELECT COUNT(*) FROM information_schema.COLUMNS
                 WHERE TABLE_SCHEMA = DATABASE()
                   AND TABLE_NAME = 'danh_muc' AND COLUMN_NAME = 'ma_man_hinh');
SET @sql := IF(@col_mmh = 0,
  'ALTER TABLE danh_muc ADD COLUMN ma_man_hinh VARCHAR(50) NULL',
  'SELECT ''column ma_man_hinh already exists'' AS msg');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- 2. Đổi kiểu danh_muc_cha sang BIGINT cho khớp id + dọn giá trị 0/orphan
ALTER TABLE danh_muc MODIFY COLUMN danh_muc_cha BIGINT NULL;
UPDATE danh_muc SET danh_muc_cha = NULL WHERE danh_muc_cha = 0;
UPDATE danh_muc SET danh_muc_cha = NULL
  WHERE danh_muc_cha IS NOT NULL
    AND danh_muc_cha NOT IN (SELECT id FROM (SELECT id FROM danh_muc) AS x);

-- 3. Seed loai/ma_man_hinh cho 7 record cũ (theo ten_viet_tat)
UPDATE danh_muc SET loai = 'BUILTIN',
  ma_man_hinh = CASE
    WHEN ten_viet_tat IN ('HS','HSCN','PROFILE','HoSo') THEN 'PROFILE'
    WHEN ten_viet_tat IN ('TKB','LICH') THEN 'TKB'
    WHEN ten_viet_tat IN ('BD','BANGDIEM','DIEM') THEN 'BANGDIEM'
    WHEN ten_viet_tat IN ('KHOA','CNTT') THEN 'KHOA'
    WHEN ten_viet_tat IN ('PDT','DAOTAO') THEN 'PDT'
    WHEN ten_viet_tat IN ('TRUONG','DHTL') THEN 'TRUONG'
    WHEN ten_viet_tat IN ('TT','THONGTIN') THEN 'THONGTIN'
    WHEN ten_viet_tat IN ('HD','HOIDAP') THEN 'HOIDAP'
    ELSE NULL
  END
WHERE ma_man_hinh IS NULL;

-- 4. Self-FK cho danh_muc_cha
SET @fk_exists := (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
                   WHERE CONSTRAINT_SCHEMA = DATABASE()
                     AND TABLE_NAME = 'danh_muc'
                     AND CONSTRAINT_NAME = 'fk_danhmuc_cha');
SET @sql := IF(@fk_exists = 0,
  'ALTER TABLE danh_muc ADD CONSTRAINT fk_danhmuc_cha FOREIGN KEY (danh_muc_cha) REFERENCES danh_muc(id) ON DELETE SET NULL ON UPDATE CASCADE',
  'SELECT ''fk_danhmuc_cha already exists'' AS msg');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- 5. Dọn orphan ma_giang_vien trước khi thêm FK
UPDATE bang_diem SET ma_giang_vien = NULL
  WHERE ma_giang_vien IS NOT NULL
    AND ma_giang_vien NOT IN (SELECT ma_giang_vien FROM giang_vien);
UPDATE co_van_hoc_tap SET ma_giang_vien = NULL
  WHERE ma_giang_vien IS NOT NULL
    AND ma_giang_vien NOT IN (SELECT ma_giang_vien FROM giang_vien);
UPDATE thoi_khoa_bieu SET ma_giang_vien = NULL
  WHERE ma_giang_vien IS NOT NULL
    AND ma_giang_vien NOT IN (SELECT ma_giang_vien FROM giang_vien);

-- 6. FK đến giang_vien (chỉ tạo nếu chưa có)
SET @fk1 := (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
             WHERE CONSTRAINT_SCHEMA = DATABASE() AND CONSTRAINT_NAME = 'fk_bd_gv');
SET @sql := IF(@fk1 = 0,
  'ALTER TABLE bang_diem ADD CONSTRAINT fk_bd_gv FOREIGN KEY (ma_giang_vien) REFERENCES giang_vien(ma_giang_vien) ON DELETE SET NULL ON UPDATE CASCADE',
  'SELECT ''fk_bd_gv already exists'' AS msg');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

SET @fk2 := (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
             WHERE CONSTRAINT_SCHEMA = DATABASE() AND CONSTRAINT_NAME = 'fk_cvht_gv');
SET @sql := IF(@fk2 = 0,
  'ALTER TABLE co_van_hoc_tap ADD CONSTRAINT fk_cvht_gv FOREIGN KEY (ma_giang_vien) REFERENCES giang_vien(ma_giang_vien) ON DELETE SET NULL ON UPDATE CASCADE',
  'SELECT ''fk_cvht_gv already exists'' AS msg');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

SET @fk3 := (SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
             WHERE CONSTRAINT_SCHEMA = DATABASE() AND CONSTRAINT_NAME = 'fk_tkb_gv');
SET @sql := IF(@fk3 = 0,
  'ALTER TABLE thoi_khoa_bieu ADD CONSTRAINT fk_tkb_gv FOREIGN KEY (ma_giang_vien) REFERENCES giang_vien(ma_giang_vien) ON DELETE SET NULL ON UPDATE CASCADE',
  'SELECT ''fk_tkb_gv already exists'' AS msg');
PREPARE s FROM @sql; EXECUTE s; DEALLOCATE PREPARE s;

-- 7. Drop bảng bieu_mau
DROP TABLE IF EXISTS bieu_mau;

COMMIT;

SELECT 'Migration 2026-05-08 hoàn tất' AS status;
