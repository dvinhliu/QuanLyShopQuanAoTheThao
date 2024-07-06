CREATE DATABASE QL_ShopQuanAoTheThao

GO

USE QL_ShopQuanAoTheThao

GO

CREATE TABLE NHACUNGCAP
(
    IDNhaCungCap varchar(5) PRIMARY KEY NOT NULL,
    TenNhaCungCap NVARCHAR(50),
    DiaChi NVARCHAR(50),
    SDT varchar(10),
    EMAIL varchar(50)
)

GO

CREATE TABLE HANGSANPHAM
(
    IDHangSanPham varchar(5) primary key not null,
    TenHang nvarchar(50),
    TenLoai nvarchar(50)
)

GO

CREATE TABLE SANPHAM
(
    IDSanPham VARCHAR(5) NOT NULL,
    TenSanPham NVARCHAR(50),
    IDHangSanPham VARCHAR(5),
	IDNhaCungCap VARCHAR(5),
    HinhAnh varchar(999),
    Size varchar(5),
	GiaNhap int,
    GiaBan int,
    SoLuongTon int default 0,
    TinhTrang nvarchar(50) default N'Chưa Nhập Sản Phẩm',
	CONSTRAINT PK_SANPHAM PRIMARY KEY (IDSanPham),
	CONSTRAINT FK_SANPHAM_NHACUNGCAP FOREIGN KEY (IDNhaCungCap) references NHACUNGCAP(IDNhaCungCap),
    CONSTRAINT FK_SANPHAM_HANGSANPHAM FOREIGN KEY (IDHangSanPham) references HANGSANPHAM(IDHangSanPham)
)

GO

CREATE TABLE PHIEUNHAP
(
    IDPhieuNhap VARCHAR(5) PRIMARY KEY NOT NULL,
    NGAYNHAP DATETIME,
    IDNhaCungCap VARCHAR(5),
    TIENNHAP INT default 0,
    CONSTRAINT FK_PHIEUNHAP_NHACC FOREIGN KEY (IDNhaCungCap) REFERENCES NHACUNGCAP(IDNhaCungCap)
)

GO

CREATE TABLE ChiTiet_PN
(
    IDPhieuNhap VARCHAR(5) NOT NULL,
    IDSanPham VARCHAR(5) NOT NULL,
    SOLUONG INT ,
    THANHTIEN INT,
    CONSTRAINT PK_CHITIETPN PRIMARY KEY (IDPhieuNhap, IDSanPham),
    CONSTRAINT FK_CHITIETPN_SANPHAM FOREIGN KEY (IDSanPham) REFERENCES SANPHAM(IDSanPham),
    CONSTRAINT FK_CHITIETPN_PHIEUNHAP FOREIGN KEY (IDPhieuNhap) references PHIEUNHAP(IDPhieuNhap)
)

GO

CREATE TABLE TAIKHOAN 
(
    IDTaiKhoan varchar(5) NOT NULL PRIMARY KEY,
    TaiKhoan VARCHAR(50),
    MatKhau VARCHAR(50)
)

GO

CREATE TABLE NHANVIEN
(
    IDNhanVien varchar(5) primary key not null,
    TenNhanVien nvarchar(50) ,
    NgaySinh Date,
    GioiTinh nvarchar(3),
    DiaChi nvarchar(50),
    SDT varchar(10),
    Email varchar(50),
    ViTriCongViec nvarchar(50),
    Luong int ,
    NgayVaoLam date,
    IDTaiKhoan varchar(5),
    CONSTRAINT FK_NHANVIEN_TAIKHOAN FOREIGN KEY (IDTaiKhoan) references TAIKHOAN(IDTaiKhoan)
)

GO

CREATE TABLE KHACHHANG
(
	IDKH varchar(5) PRIMARY KEY NOT NULL,
	TenKH NVARCHAR(50),
	SDT VARCHAR(10)
)

GO

CREATE TABLE HOADON
(
    IDHoaDon varchar(5) PRIMARY KEY NOT NULL,
    NgayLapHD DATETIME,
    IDNhanVien varchar(5),
	IDKH varchar(5),
    DiaChi nVARCHAR(50),
    HinhThuc NVARCHAR(10),
    GiamGia int,
    ThanhTien int,
    CONSTRAINT FK_HOADON_NHANVIEN FOREIGN KEY (IDNhanVien) references NHANVIEN(IDNhanVien),
	CONSTRAINT FK_HOADON_KHACHHANG FOREIGN KEY (IDKH) REFERENCES KHACHHANG(IDKH)
)

GO

CREATE TABLE CHITIETHOADON
(
    IDHoaDon varchar(5) NOT NULL,
    IDSanPham VARCHAR(5) NOT NULL,
    SoLuong int,
    ThanhTien int,
    CONSTRAINT PK_CHITIETHOADON PRIMARY KEY (IDHoaDon, IDSanPham),
    CONSTRAINT FK_CHITIETHOADON_HOADON FOREIGN KEY (IDHoaDon) references HOADON(IDHoaDon),
    CONSTRAINT FK_CHITIETHOADON_SANPHAM FOREIGN KEY (IDSanPham) references SANPHAM(IDSanPham)
)

GO

INSERT INTO NHACUNGCAP
VALUES
('NCC01',N'Hoàng Tây',N'Thôn 2, Thị Trấn ĐakĐoa, ĐakĐoa, Gia Lai','0862322671','hoangtay2@gmail.com'),
('NCC02',N'Minh Tú',N'Xã Đông Hòa, Châu Thành, Tiền Giang','0384396100','mitu32@gmail.com')

GO

INSERT INTO HANGSANPHAM
VALUES
('HSP01','Adidas', N'Áo Bóng Đá'),
('HSP02','Nike', N'Áo Bóng Đá'),
('HSP03','Puma', N'Áo Bóng Đá')

GO

INSERT INTO SANPHAM
VALUES
('SP001', N'Quần áo Liverpool (Sân Nhà)', 'HSP03', 'NCC01', 'hinh1.png', 'M', 1000000, 1500000, 10, 'Còn Hàng'),
('SP002', N'Quần áo Liverpool (Sân Khách)', 'HSP03', 'NCC01', 'hinh2.png', 'L', 1000000, 1500000, 10, 'Còn Hàng'),
('SP003', N'Quần áo Liverpool (Sân Khách)', 'HSP03', 'NCC01', 'hinh2.png', 'XL', 1000000, 1500000, 20, 'Còn Hàng'),
('SP004', N'Quần áo RealMadrid (Sân Nhà)', 'HSP02', 'NCC01', 'hinh3.png', 'L', 1500000, 2000000, 20, 'Còn Hàng'),
('SP005', N'Quần áo RealMadrid (Sân Khách)', 'HSP02', 'NCC01', 'hinh4.png', 'M', 1500000, 2000000, 15, 'Còn Hàng'),
('SP006', N'Quần áo RealMadrid (Sân Khách)', 'HSP02', 'NCC01', 'hinh4.png', 'L', 1500000, 2000000, 15, 'Còn Hàng'),
('SP007', N'Quần áo Đội Tuyển Pháp (Sân Nhà)', 'HSP01', 'NCC02', 'hinh5.png', 'XL', 2000000, 2500000, 40, 'Còn Hàng'),
('SP008', N'Quần áo Đội Tuyển Pháp (Sân Khách)', 'HSP01', 'NCC02', 'hinh6.png', 'XL', 2000000, 2500000, 20, 'Còn Hàng')

GO

INSERT INTO TAIKHOAN
VALUES
('TK001', 'hoald', '123456'),
('TK002', 'tuanmk', '123456'),
('TK003', 'sonla', '123456'),
('TK004', 'thoaidt', '123456'),
('TK005', 'hantt', '123456'),
('TK006', 'conghv', '123456')

GO

SET DATEFORMAT DMY
INSERT INTO PHIEUNHAP
VALUES
('PN001','05/05/2023 6:00:00','NCC01','115000000'),
('PN002','05/05/2023 6:00:00','NCC02','120000000')

GO

INSERT INTO ChiTiet_PN
VALUES 
('PN001','SP001','10', 10000000),
('PN001','SP002','10', 10000000),
('PN001','SP003','20', 20000000),
('PN001','SP004','20', 30000000),
('PN001','SP005','15', 22500000),
('PN001','SP006','15', 22500000),
('PN002','SP007','40', 80000000),
('PN002','SP008','20', 40000000)


GO

SET DATEFORMAT DMY
INSERT INTO NHANVIEN
VALUES
('NV001', N'Lưu Đức Hòa', '06/10/2003', N'Nam', N'Số 12, Quận Tây Thạnh, Tân Phú, TPHCM', '0864627763', 'hoald@gmail.com', N'Nhân Viên Bán Hàng', 10000000, '10/01/2021', 'TK001'),
('NV002', N'Lê Minh Tuấn', '15/05/1995', N'Nam', N'Số 25, Quận Bình Thạnh, TPHCM', '0987654321', 'tuamt@gmail.com', N'Quản Lý Cửa Hàng', 15000000, '15/08/2020', 'TK002'),
('NV003', N'Nguyễn Thành Tài', '20/12/1990', N'Nam', N'Số 5, Quận 1, TPHCM', '0909876543', 'taitt@gmail.com', N'Bảo Vệ', 9000000, '01/12/2022', NULL),
('NV004', N'Nguyễn Hoàng Sơn', '25/03/1987', N'Nam', N'Số 8, Quận 9, TPHCM', '0978888888', 'sonla@gmail.com', N'Nhân Viên Quản Lý Tồn Kho', 12000000, '04/02/2019', 'TK003'),
('NV005', N'Nguyễn Anh Thoại', '18/09/1991', N'Nam', N'Số 19, Quận Gò Vấp, TPHCM', '0934567890', 'thoaimobi@gmail.com', N'Nhân Viên Thu Ngân', 11000000, '20/03/2021', 'TK004'),
('NV006', N'Nguyễn Thị Thu Hà', '28/02/1998', N'Nữ', N'Số 7, Quận Tân Bình, TPHCM', '0912345678', 'hantt@gmail.com', N'Nhân Viên Marketing', 13000000, '05/10/2022', 'TK005'),
('NV007', N'Huỳnh Văn Công', '18/09/1991', N'Nam', N'Số 14, Quận 3, TPHCM', '0925874545', 'conglh@gmail.com', N'Nhân Viên Kho', 9500000, '11/05/2020', 'TK006')

GO

CREATE TRIGGER UpdateSoLuongTon
ON ChiTiet_PN
AFTER INSERT
AS
BEGIN
    UPDATE SANPHAM
    SET SoLuongTon = SoLuongTon + i.SOLUONG,
        TinhTrang='Còn Hàng'
    FROM SANPHAM AS s
    INNER JOIN inserted AS i ON s.IDSanPham = i.IDSanPham;
END;