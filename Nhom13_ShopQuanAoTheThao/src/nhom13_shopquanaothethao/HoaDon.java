/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nhom13_shopquanaothethao;

/**
 *
 * @author HP
 */
public class HoaDon {
    public String IDHoaDon;
    public String NgayLap;
    public String IDNhanVien;
    public String TenKhachHang;
    public String DiaChi;
    public String SDT;
    public String HinhThuc;
    public int GiamGia;
    public int ThanhTien;

    public HoaDon(String IDHoaDon, String NgayLap, String IDNhanVien, String TenKhachHang, String DiaChi, String SDT, String HinhThuc, int GiamGia, int ThanhTien) {
        this.IDHoaDon = IDHoaDon;
        this.NgayLap = NgayLap;
        this.IDNhanVien = IDNhanVien;
        this.TenKhachHang = TenKhachHang;
        this.DiaChi = DiaChi;
        this.SDT = SDT;
        this.HinhThuc = HinhThuc;
        this.GiamGia = GiamGia;
        this.ThanhTien = ThanhTien;
    }

    public String getIDHoaDon() {
        return IDHoaDon;
    }

    public void setIDHoaDon(String IDHoaDon) {
        this.IDHoaDon = IDHoaDon;
    }

    public String getNgayLap() {
        return NgayLap;
    }

    public void setNgayLap(String NgayLap) {
        this.NgayLap = NgayLap;
    }

    public String getIDNhanVien() {
        return IDNhanVien;
    }

    public void setIDNhanVien(String IDNhanVien) {
        this.IDNhanVien = IDNhanVien;
    }

    public String getTenKhachHang() {
        return TenKhachHang;
    }

    public void setTenKhachHang(String TenKhachHang) {
        this.TenKhachHang = TenKhachHang;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String DiaChi) {
        this.DiaChi = DiaChi;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getHinhThuc() {
        return HinhThuc;
    }

    public void setHinhThuc(String HinhThuc) {
        this.HinhThuc = HinhThuc;
    }

    public int getGiamGia() {
        return GiamGia;
    }

    public void setGiamGia(int GiamGia) {
        this.GiamGia = GiamGia;
    }

    public int getThanhTien() {
        return ThanhTien;
    }

    public void setThanhTien(int ThanhTien) {
        this.ThanhTien = ThanhTien;
    }
    
    
    
}
