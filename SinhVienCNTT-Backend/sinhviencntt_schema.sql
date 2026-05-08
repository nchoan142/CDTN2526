mysqldump: [Warning] Using a password on the command line interface can be insecure.
-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: sinhviencntt
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bang_diem`
--

DROP TABLE IF EXISTS `bang_diem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bang_diem` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `chuyen_nganh_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `diem_tong_ket` double DEFAULT NULL,
  `khoa` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `loai_hoc_phan` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_giang_vien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_hoc_phan` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_sinh_vien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `so_lan_thi` int DEFAULT NULL,
  `so_tin_chi` int DEFAULT NULL,
  `ten_hoc_phan` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tien_quyet` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_bd_msv` (`ma_sinh_vien`),
  CONSTRAINT `fk_bangdiem_sinhvien` FOREIGN KEY (`ma_sinh_vien`) REFERENCES `sinh_vien` (`ma_sinh_vien`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=738 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bieu_mau`
--

DROP TABLE IF EXISTS `bieu_mau`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bieu_mau` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dang_dung` bit(1) DEFAULT NULL,
  `link_tai_ve` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `loai` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_bieu_mau` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mo_ta` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stt` int DEFAULT NULL,
  `ten_bieu_mau` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `co_van_hoc_tap`
--

DROP TABLE IF EXISTS `co_van_hoc_tap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `co_van_hoc_tap` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `ghi_chu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_giang_vien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_ky` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_lop` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_cvht_ky` (`ma_ky`),
  CONSTRAINT `fk_cvht_kyhoc` FOREIGN KEY (`ma_ky`) REFERENCES `ky_hoc` (`ma_ky`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=328 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `danh_muc`
--

DROP TABLE IF EXISTS `danh_muc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `danh_muc` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dang_dung` bit(1) DEFAULT NULL,
  `danh_muc_cha` int DEFAULT NULL,
  `link_anh` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `link_truy_cap` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nguoi_quan_ly` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `stt` int DEFAULT NULL,
  `ten_day_du` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ten_viet_tat` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `giang_vien`
--

DROP TABLE IF EXISTS `giang_vien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `giang_vien` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `co_huu1` int DEFAULT NULL,
  `co_huu2` int DEFAULT NULL,
  `dien_thoai` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `don_vi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ghi_chu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `giang_vien` int DEFAULT NULL,
  `ho_ten_dem` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hoc_ham` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `hoc_vi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_giang_vien` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `quan_tri` int DEFAULT NULL,
  `ten` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `thinh_giang` int DEFAULT NULL,
  `thu_ky` int DEFAULT NULL,
  `trang_thai` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_gsncj1btf87flg80d61mrgb9f` (`ma_giang_vien`),
  KEY `idx_ma_gv` (`ma_giang_vien`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hoi_dap`
--

DROP TABLE IF EXISTS `hoi_dap`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoi_dap` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cau_hoi` text COLLATE utf8mb4_unicode_ci,
  `cau_tra_loi` text COLLATE utf8mb4_unicode_ci,
  `da_duyet` bit(1) DEFAULT NULL,
  `ma_sinh_vien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ngay_hoi` datetime(6) DEFAULT NULL,
  `ngay_tra_loi` datetime(6) DEFAULT NULL,
  `nguoi_tra_loi` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ten_sinh_vien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_hd_msv` (`ma_sinh_vien`),
  CONSTRAINT `fk_hoidap_sinhvien` FOREIGN KEY (`ma_sinh_vien`) REFERENCES `sinh_vien` (`ma_sinh_vien`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ky_hoc`
--

DROP TABLE IF EXISTS `ky_hoc`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ky_hoc` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bat_dau_chon_lich` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bat_dau_ky_hoc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `bat_dau_lap_lich` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ket_thuc_chon_lich` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ket_thuc_ky_hoc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ket_thuc_lap_lich` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `last_modify` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_ky` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ma_nam` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `mac_dinh` int DEFAULT NULL,
  `ten_ky` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3v1iawq1mnj0ttscaben1p14l` (`ma_ky`),
  KEY `idx_ma_ky` (`ma_ky`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sinh_vien`
--

DROP TABLE IF EXISTS `sinh_vien`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sinh_vien` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dien_thoai1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `dien_thoai2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email1` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `email2` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ghi_chu` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ho_ten_dem` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `khoa` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `khoa_nhap_hoc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lop` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `lop_chuyen_nganh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_sinh_vien` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `nganh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ngay_sinh` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nhap_hoc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ten` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `trang_thai` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_91yr8sexldey1h1mr76ns7g4o` (`ma_sinh_vien`),
  KEY `idx_ma_sv` (`ma_sinh_vien`)
) ENGINE=InnoDB AUTO_INCREMENT=1922 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tai_khoan_admin`
--

DROP TABLE IF EXISTS `tai_khoan_admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tai_khoan_admin` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ho_ten` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `role` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_aao63km08vd02d5ela0gr3jmo` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `thoi_khoa_bieu`
--

DROP TABLE IF EXISTS `thoi_khoa_bieu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thoi_khoa_bieu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gio_bat_dau` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `gio_ket_thuc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_giang_vien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_hoc_phan` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_ky` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ma_sinh_vien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `nhom` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phong_hoc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ten_giang_vien` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ten_hoc_phan` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `thu` int DEFAULT NULL,
  `tiet_bat_dau` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `tiet_ket_thuc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_tkb_msv` (`ma_sinh_vien`),
  KEY `idx_tkb_ky` (`ma_ky`),
  CONSTRAINT `fk_tkb_kyhoc` FOREIGN KEY (`ma_ky`) REFERENCES `ky_hoc` (`ma_ky`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `fk_tkb_sinhvien` FOREIGN KEY (`ma_sinh_vien`) REFERENCES `sinh_vien` (`ma_sinh_vien`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `thong_bao`
--

DROP TABLE IF EXISTS `thong_bao`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thong_bao` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `da_doc` bit(1) DEFAULT NULL,
  `doi_tuong` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ghim` bit(1) DEFAULT NULL,
  `ngay_gui` datetime(6) DEFAULT NULL,
  `nguoi_gui` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `noi_dung` text COLLATE utf8mb4_unicode_ci,
  `tieu_de` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-15 20:28:29
