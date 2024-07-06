/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.lang.reflect.Array;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import org.jdatepicker.impl.*;
import java.text.SimpleDateFormat;
import java.util.Properties;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFFont;
import java.util.List;
import org.apache.logging.log4j.core.layout.HtmlLayout;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
/**
 *
 * @author HP
 */
public class ThongKeForm extends javax.swing.JFrame {

    /**
     * Creates new form ThongKeForm
     */
    Connection con;
    DefaultTableModel tblModel;
    JDatePickerImpl datePicker;
    ArrayList<HoaDon> arr = new ArrayList<>();
    private String sql = "SELECT * FROM HOADON";
    private boolean isFirstLoad = true; // Biến cờ để kiểm tra lần đầu load
    
    public ThongKeForm() {
        initComponents();                

        tblModel = (DefaultTableModel) tbl_ThongKe.getModel();
        
        
        loadThongKeTbl(sql);
        
        setupDatePicker();
        loadCbbChon();        
        loadCbbThang();
        load_lb_AllBill();
        load_lb_AllTotal();
        load_lb_AllTotalBuy();
        
//        lb_AllTotalBuy.setVisible(false);
//        Lable3.setVisible(false);
       
    }
    
    private void loadThongKeTbl(String s)
    {
        // Xóa hết dữ liệu cũ trước khi load mới
        tblModel.setRowCount(0);
            
        try {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(s);
            while (rs.next()) {
                String idKhachHang = rs.getString("IDKH");
                String tenKhachHang;
                String sdtKhachHang;

                if (idKhachHang == null || idKhachHang.trim().isEmpty()) {
                    tenKhachHang = "Khách vãng lai";
                    sdtKhachHang = ""; // Không có số điện thoại cho khách vãng lai
                } else {
                    // Truy vấn để lấy tên khách hàng và số điện thoại từ bảng khách hàng
                    String sqlKH = "SELECT TenKH, SDT FROM KHACHHANG WHERE IDKH = '" + idKhachHang + "'";
                    Statement stKH = con.createStatement();
                    ResultSet rsKH = stKH.executeQuery(sqlKH);

                    if (rsKH.next()) {
                        tenKhachHang = rsKH.getString("TenKH");
                        sdtKhachHang = rsKH.getString("SDT");
                    } else {
                        tenKhachHang = "Không tìm thấy";
                        sdtKhachHang = "Không tìm thấy";
                    }
                    rsKH.close();
                    stKH.close();
                }

                Object[] row = {
                    rs.getString("IDHoaDon"),
                    rs.getString("NgayLapHD"),
                    rs.getString("IDNhanVien"),
                    tenKhachHang,
                    sdtKhachHang,
                    rs.getString("DiaChi"),
                    rs.getString("HinhThuc"),
                    rs.getString("GiamGia"),
                    rs.getString("ThanhTien")
                };
                tblModel.addRow(row);
            }
            tbl_ThongKe.setModel(tblModel);
            st.close();
            rs.close();
            con.close();

        // Sau khi cập nhật bảng, gọi các phương thức tính lại giá trị
        load_lb_AllBill();
        load_lb_AllTotal();
        load_lb_AllTotalBuy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCbbChon()
    {
        cbb_Chon.removeAllItems();
        cbb_Chon.addItem("Mời Bạn Chọn");
        cbb_Chon.addItem("Thống kê theo ngày");
        cbb_Chon.addItem("Thống kê theo tháng");
        
        setPanelEnabled(jPanel2,false);
        setPanelEnabled(jPanel3,false);
        datePicker.setEnabled(false);
        

        cbb_Chon.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED)
                {
                    String selected = (String) cbb_Chon.getSelectedItem();
                    if(selected.equals("Thống kê theo ngày"))
                    {
                        setPanelEnabled(jPanel2,true);
                        setPanelEnabled(jPanel3,false);
                        datePicker.setEnabled(true);
                        
                        datePicker.addActionListener(datePActionListener);
                    }
                    else if(selected.equals("Thống kê theo tháng"))
                    {
                        setPanelEnabled(jPanel2,false);
                        setPanelEnabled(jPanel3,true);
                        datePicker.setEnabled(false);
                        
                        loadThongKeTheoThang();
                        
                        if (!isFirstLoad) {
                            loadThongKeTbl(sql);
                        }
                        isFirstLoad = false; // Đã load lần đầu, sau này không cần load lại nữa

                    }
                    else{
                        
                        setPanelEnabled(jPanel2,false);
                        setPanelEnabled(jPanel3,false);
                        datePicker.setEnabled(false);
                        
                         // Chỉ gọi lại loadThongKeTbl(sql) nếu không phải là lần đầu load
                        tblModel.setRowCount(0);
                        loadThongKeTbl(sql);
                    }
                    
                }
            }
        });
    }
    
    private void setPanelEnabled(JPanel panel, boolean isEnabled) {
        panel.setEnabled(isEnabled);
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                setPanelEnabled((JPanel) component, isEnabled);
            } else {
                component.setEnabled(isEnabled);
            }
        }
    }
    
    private void loadCbbThang()
    {
        cbb_ChonThang.removeAllItems();
        String[] thang = {"Tháng 1","Tháng 2","Tháng 3","Tháng 4","Tháng 5","Tháng 6","Tháng 7","Tháng 8","Tháng 9","Tháng 10","Tháng 11","Tháng 12"};
        ArrayList<String> thangs = new ArrayList<>();
        for (String t: thang) {
            thangs.add(t);
        }
        
        for (String t: thangs){
            cbb_ChonThang.addItem(t);
        }
    }
    
    private void setupDatePicker() {
        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        // Thêm DatePicker vào panel jPanel2
        jPanel2.add(datePicker);
        datePicker.setBounds(150, 48, 200, 40); // Đặt vị trí và kích thước của datePicker

        // Thiết lập hành động khi chọn ngày
        datePicker.addActionListener(e -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        });
    }
    
    private final ActionListener datePActionListener = e -> loadTxt(); 
    
    private void loadTxt() {
        // Lấy giá trị ngày từ datePicker và kiểm tra xem nó có null không
        Date selectedDate = (Date) datePicker.getModel().getValue();
        if (selectedDate == null) {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn ngày trước khi xem thống kê.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Chuyển đổi giá trị ngày sang chuỗi định dạng yyyy-MM-dd
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String totalHD_Day = formatter.format(selectedDate);

        try {
            // Kết nối đến cơ sở dữ liệu
            con = nhom13_shopquanaothethao.Connection.GetConnection();

            // Tạo câu lệnh SQL để tính số lượng hóa đơn và tổng thành tiền trong ngày đã chọn
            String sql = "SELECT " +
                        "CAST(NgayLapHD AS DATE) AS Ngay, " +
                        "COUNT(IDHoaDon) AS SoLuongHoaDon, " +
                        "SUM(ThanhTien) AS TotalThanhTien " +
                        "FROM HOADON " +
                        "WHERE CAST(NgayLapHD AS DATE) = '" + totalHD_Day + "' " +
                        "GROUP BY CAST(NgayLapHD AS DATE) " +
                        "ORDER BY Ngay;";

            // Tạo statement và thực thi truy vấn
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Xử lý kết quả truy vấn
            if (rs.next()) {
                // Lấy số lượng hóa đơn và hiển thị lên lb_TotalBill
                int soLuongHoaDon = rs.getInt("SoLuongHoaDon");
                lb_TotalBill.setText(String.valueOf(soLuongHoaDon));

                // Lấy tổng thành tiền và hiển thị lên lb_ToTal
                double totalThanhTien = rs.getDouble("TotalThanhTien");

                // Định dạng tổng thành tiền
                DecimalFormat df = new DecimalFormat("#,##0");
                String formattedTotalThanhTien = df.format(totalThanhTien);
                lb_ToTal.setText(formattedTotalThanhTien);
            } else {
                // Nếu không có kết quả, đặt lb_TotalBill và lb_ToTal thành 0
                lb_TotalBill.setText("0");
                lb_ToTal.setText("0");
            }

            // Đóng các tài nguyên
            rs.close();

            // Tạo câu lệnh SQL để lấy danh sách hóa đơn trong ngày đã chọn
            String sqlDetail = "SELECT * FROM HOADON WHERE CAST(NgayLapHD AS DATE) = '" + totalHD_Day + "';";

            // Thực thi truy vấn và cập nhật bảng
            rs = st.executeQuery(sqlDetail);
            tblModel.setRowCount(0); // Xóa các hàng hiện có trong bảng
            while (rs.next()) {
                String idKhachHang = rs.getString("IDKH");
                String tenKhachHang;
                String sdtKhachHang;

                if (idKhachHang == null || idKhachHang.trim().isEmpty()) {
                    tenKhachHang = "Khách vãng lai";
                    sdtKhachHang = ""; // Không có số điện thoại cho khách vãng lai
                } else {
                    // Truy vấn để lấy tên khách hàng và số điện thoại từ bảng khách hàng
                    String sqlKH = "SELECT TenKH, SDT FROM KHACHHANG WHERE IDKH = '" + idKhachHang + "'";
                    Statement stKH = con.createStatement();
                    ResultSet rsKH = stKH.executeQuery(sqlKH);

                    if (rsKH.next()) {
                        tenKhachHang = rsKH.getString("TenKH");
                        sdtKhachHang = rsKH.getString("SDT");
                    } else {
                        tenKhachHang = "Không tìm thấy";
                        sdtKhachHang = "Không tìm thấy";
                    }
                    rsKH.close();
                    stKH.close();
                }

                Object[] row = {
                    rs.getString("IDHoaDon"),
                    rs.getString("NgayLapHD"),
                    rs.getString("IDNhanVien"),
                    tenKhachHang,
                    sdtKhachHang,
                    rs.getString("DiaChi"),
                    rs.getString("HinhThuc"),
                    rs.getString("GiamGia"),
                    rs.getString("ThanhTien")
                };
                tblModel.addRow(row);
            }
            tbl_ThongKe.setModel(tblModel);

            // Đóng các tài nguyên
            rs.close();
            st.close();
            con.close();

            load_lb_AllBill();
            load_lb_AllTotal();
            load_lb_AllTotalBuy();

            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    
    private void loadThongKeTheoThang() {
        cbb_ChonThang.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedMonth = (String) cbb_ChonThang.getSelectedItem();
                    int month = getMonthNumber(selectedMonth);
                    loadMonthlyData(month);
                }
            }
        });
    }   

    // Hàm chuyển đổi tên tháng sang số tháng
        private int getMonthNumber(String monthName) {
            switch (monthName) {
                case "Tháng 1": return 1;
                case "Tháng 2": return 2;
                case "Tháng 3": return 3;
                case "Tháng 4": return 4;
                case "Tháng 5": return 5;
                case "Tháng 6": return 6;
                case "Tháng 7": return 7;
                case "Tháng 8": return 8;
                case "Tháng 9": return 9;
                case "Tháng 10": return 10;
                case "Tháng 11": return 11;
                case "Tháng 12": return 12;
                default: return 0;
            }
        }

    // Hàm tải dữ liệu thống kê theo tháng từ cơ sở dữ liệu
    private void loadMonthlyData(int month) {
        // Kiểm tra xem tháng có hợp lệ không
        if (month < 1 || month > 12) {
            JOptionPane.showMessageDialog(null, "Tháng không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy năm hiện tại
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);

        try {
            // Kết nối đến cơ sở dữ liệu
            con = nhom13_shopquanaothethao.Connection.GetConnection();

            // Tạo câu lệnh SQL để tính tổng số hóa đơn và tổng thành tiền theo tháng đã chọn
            String sql = "SELECT " +
                         "COUNT(IDHoaDon) AS SoLuongHoaDon, " +
                         "SUM(ThanhTien) AS TotalThanhTien " +
                         "FROM HOADON " +
                         "WHERE MONTH(NgayLapHD) = '" + month + "' AND YEAR(NgayLapHD) = '" + year + "';";

            // Tạo statement và thực thi truy vấn
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            // Xử lý kết quả truy vấn
            if (rs.next()) {
                // Lấy số lượng hóa đơn và hiển thị lên lb_TotalBill_Month
                int soLuongHoaDon = rs.getInt("SoLuongHoaDon");
                lb_TotalBill_Month.setText(String.valueOf(soLuongHoaDon));

                // Lấy tổng thành tiền và hiển thị lên lb_Total_Month
                double totalThanhTien = rs.getDouble("TotalThanhTien");
                DecimalFormat df = new DecimalFormat("#,##0");
                String formattedTotalThanhTien = df.format(totalThanhTien);
                lb_Total_Month.setText(formattedTotalThanhTien);
            } else {
                // Nếu không có kết quả, đặt lb_TotalBill_Month và lb_Total_Month thành 0
                lb_TotalBill_Month.setText("0");
                lb_Total_Month.setText("0");
            }

            // Đóng các tài nguyên
            rs.close();

            // Tạo câu lệnh SQL để lấy danh sách hóa đơn trong tháng đã chọn
            String sqlDetail = "SELECT * FROM HOADON WHERE MONTH(NgayLapHD) = '" + month + "' AND YEAR(NgayLapHD) = '" + year + "';";

            // Thực thi truy vấn và cập nhật bảng
            rs = st.executeQuery(sqlDetail);
            tblModel.setRowCount(0); // Xóa các hàng hiện có trong bảng
            while (rs.next()) {
                String idKhachHang = rs.getString("IDKH");
                String tenKhachHang;
                String sdtKhachHang;

                if (idKhachHang == null || idKhachHang.trim().isEmpty()) {
                    tenKhachHang = "Khách vãng lai";
                    sdtKhachHang = ""; // Không có số điện thoại cho khách vãng lai
                } else {
                    // Truy vấn để lấy tên khách hàng và số điện thoại từ bảng khách hàng
                    String sqlKH = "SELECT TenKH, SDT FROM KHACHHANG WHERE IDKH = '" + idKhachHang + "'";
                    Statement stKH = con.createStatement();
                    ResultSet rsKH = stKH.executeQuery(sqlKH);

                    if (rsKH.next()) {
                        tenKhachHang = rsKH.getString("TenKH");
                        sdtKhachHang = rsKH.getString("SDT");
                    } else {
                        tenKhachHang = "Không tìm thấy";
                        sdtKhachHang = "Không tìm thấy";
                    }
                    rsKH.close();
                    stKH.close();
                }

                Object[] row = {
                    rs.getString("IDHoaDon"),
                    rs.getString("NgayLapHD"),
                    rs.getString("IDNhanVien"),
                    tenKhachHang,
                    sdtKhachHang,
                    rs.getString("DiaChi"),
                    rs.getString("HinhThuc"),
                    rs.getString("GiamGia"),
                    rs.getString("ThanhTien")
                };
                tblModel.addRow(row);
            }
            tbl_ThongKe.setModel(tblModel);

            // Đóng các tài nguyên
            rs.close();

            // Tạo câu lệnh SQL để tính số lượng hóa đơn và tổng thành tiền trong ngày đã chọn
            String sqlMonth = "SELECT SUM(SOLUONG * GiaNhap) As 'ThanhTienNhap' " +
                            "FROM SANPHAM sp, CHITIETHOADON ct, HOADON hd " +
                            "WHERE hd.IDHoaDon = ct.IDHoaDon AND sp.IDSanPham = ct.IDSanPham " +
                            "AND MONTH(hd.NgayLapHD) = '" + month + "' AND YEAR(hd.NgayLapHD) = '" + year + "';";

            // Tạo statement và thực thi truy vấn
            rs = st.executeQuery(sqlMonth);

            // Xử lý kết quả truy vấn
            if (rs.next()) {
                double allTongTien = rs.getDouble("ThanhTienNhap");
                DecimalFormat df = new DecimalFormat("#,##0");
                String formattedAllTotal = df.format(allTongTien);
                lb_AllTotalBuy.setText(formattedAllTotal);
            } else {
                load_lb_AllTotalBuy();
            }

            rs.close();
            st.close();
            con.close();

            load_lb_AllBill();
            load_lb_AllTotal();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void load_lb_AllBill()
    {
        try {
            int rowCount = tbl_ThongKe.getRowCount();
            lb_AllBill.setText(String.valueOf(rowCount));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void load_lb_AllTotal()
    {
        try {
            // Tính tổng thành tiền từ các hàng trong bảng tbl_ThongKe
            double totalThanhTien = 0;
            for (int i = 0; i < tbl_ThongKe.getRowCount(); i++) {
                totalThanhTien += Double.parseDouble(tbl_ThongKe.getValueAt(i, 8).toString()); // Thay columnIndex bằng chỉ số cột của cột "ThanhTien"
            }

            DecimalFormat df = new DecimalFormat("#,##0");
            String formattedTotalThanhTien = df.format(totalThanhTien);
            lb_AllTotal.setText(formattedTotalThanhTien);
                    
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void load_lb_AllTotalBuy()
    {
        try {
            con = nhom13_shopquanaothethao.Connection.GetConnection();

                // Tạo câu lệnh SQL để tính số lượng hóa đơn và tổng thành tiền trong ngày đã chọn
                String sql = "SELECT SUM(SOLUONG * GiaNhap) As 'ThanhTienNhap'\n" +
                                "FROM SANPHAM sp, CHITIETHOADON ct, HOADON hd\n" +
                                "WHERE hd.IDHoaDon = ct.IDHoaDon AND sp.IDSanPham = ct.IDSanPham AND YEAR(hd.NgayLapHD) = 2024;";

                // Tạo statement và thực thi truy vấn
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);

                // Xử lý kết quả truy vấn
                if (rs.next()) {
                    // Lấy số lượng hóa đơn và hiển thị lên lb_TotalBill
                    double AllTongTien = rs.getDouble("ThanhTienNhap");
                    DecimalFormat df = new DecimalFormat("#,##0");
                    String formattedAllTotal = df.format(AllTongTien);
                    lb_AllTotalBuy.setText(formattedAllTotal);
                    
                } else {
                    lb_AllTotalBuy.setText("0");
                }
                
                rs.close();
                st.close();
                con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_ThongKe = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lb_ToTal = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lb_TotalBill = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lb_Total_Month = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lb_TotalBill_Month = new javax.swing.JLabel();
        cbb_ChonThang = new javax.swing.JComboBox<>();
        cbb_Chon = new javax.swing.JComboBox<>();
        lb_AllTotal = new javax.swing.JLabel();
        lb_AllBill = new javax.swing.JLabel();
        btn_Excel = new javax.swing.JButton();
        Lable1 = new javax.swing.JLabel();
        Lable2 = new javax.swing.JLabel();
        Lable3 = new javax.swing.JLabel();
        lb_AllTotalBuy = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1390, 670));
        setPreferredSize(new java.awt.Dimension(1390, 670));
        setSize(new java.awt.Dimension(1390, 670));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THỐNG KÊ BÁN HÀNG");

        tbl_ThongKe.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tbl_ThongKe.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã Hóa Đơn", "Ngày Lập", "Mã NV", "Tên Khách Hàng", "Số Điện Thoại", "Địa Chỉ", "Hình Thức", "Giảm Giá", "Thành Tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbl_ThongKe);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Doanh thu theo ngày"));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 51, 255));
        jLabel2.setText("Tổng Hóa Đơn:");

        lb_ToTal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_ToTal.setForeground(new java.awt.Color(255, 0, 51));
        lb_ToTal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_ToTal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 0, 51));
        jLabel4.setText("Tổng Tiền:");

        jLabel5.setText("Mời bạn chọn ngày: ");

        lb_TotalBill.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_TotalBill.setForeground(new java.awt.Color(0, 51, 255));
        lb_TotalBill.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_TotalBill.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addGap(254, 254, 254))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lb_TotalBill, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                    .addComponent(lb_ToTal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(84, 84, 84))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_TotalBill, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_ToTal, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Doanh thu theo tháng"));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 51, 255));
        jLabel7.setText("Tổng Hóa Đơn:");

        lb_Total_Month.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_Total_Month.setForeground(new java.awt.Color(255, 0, 51));
        lb_Total_Month.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_Total_Month.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 0, 51));
        jLabel9.setText("Tổng Tiền:");

        jLabel10.setText("Mời bạn chọn tháng: ");

        lb_TotalBill_Month.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_TotalBill_Month.setForeground(new java.awt.Color(0, 51, 255));
        lb_TotalBill_Month.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_TotalBill_Month.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cbb_ChonThang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(170, 170, 170))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_Total_Month, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_TotalBill_Month, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbb_ChonThang, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(35, 35, 35))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbb_ChonThang, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(lb_TotalBill_Month, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb_Total_Month, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        cbb_Chon.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lb_AllTotal.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_AllTotal.setForeground(new java.awt.Color(255, 0, 51));
        lb_AllTotal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lb_AllBill.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_AllBill.setForeground(new java.awt.Color(0, 255, 51));
        lb_AllBill.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        btn_Excel.setText("Xuất FIle Excel");
        btn_Excel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ExcelActionPerformed(evt);
            }
        });

        Lable1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Lable1.setForeground(new java.awt.Color(0, 255, 51));
        Lable1.setText("Tổng Hóa Đơn:");
        Lable1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Lable2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Lable2.setForeground(new java.awt.Color(255, 0, 51));
        Lable2.setText("Tổng Tiền Bán:");
        Lable2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        Lable3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Lable3.setForeground(new java.awt.Color(51, 51, 255));
        Lable3.setText("Tổng Tiền Nhập:");
        Lable3.setEnabled(false);
        Lable3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lb_AllTotalBuy.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lb_AllTotalBuy.setForeground(new java.awt.Color(51, 51, 255));
        lb_AllTotalBuy.setEnabled(false);
        lb_AllTotalBuy.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1301, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(45, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(405, 405, 405)
                        .addComponent(cbb_Chon, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Lable1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Lable2, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lb_AllBill, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                            .addComponent(lb_AllTotal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btn_Excel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(123, 123, 123))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Lable3, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lb_AllTotalBuy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbb_Chon, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_AllBill, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lb_AllTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lb_AllTotalBuy, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Lable3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btn_Excel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Lable1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Lable2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(100, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ExcelActionPerformed
        // TODO add your handling code here:
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Chọn nơi lưu file Excel");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel files", "xlsx"));

            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                //Lưu File với đuôi ".xlsx"
                if (!fileToSave.getAbsolutePath().endsWith(".xlsx")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
                }

                try {
                    XSSFWorkbook workbook;
                    String sheetName = "Thống Kê";
                    XSSFSheet sheet;

                    if (fileToSave.exists()) {
                        try (FileInputStream fileIn = new FileInputStream(fileToSave)) {
                            workbook = new XSSFWorkbook(fileIn);
                        }
                        // Xóa sheet nếu tồn tại
                        sheet = workbook.getSheet(sheetName);
                        if (sheet != null) {
                            int sheetIndex = workbook.getSheetIndex(sheet);
                            workbook.removeSheetAt(sheetIndex);
                        }
                        // Tạo lại sheet mới
                        sheet = workbook.createSheet(sheetName);
                    } else {
                        workbook = new XSSFWorkbook();
                        sheet = workbook.createSheet(sheetName);
                    }
                    
                    // Tạo style in đậm và căn giữa
                    XSSFCellStyle boldCenteredStyle = workbook.createCellStyle();
                    XSSFFont boldFont = workbook.createFont();
                    boldFont.setBold(true);
                    boldFont.setFontHeightInPoints((short) 20);
                    boldCenteredStyle.setFont(boldFont);
                    boldCenteredStyle.setAlignment(HorizontalAlignment.CENTER);

                    // Tạo dòng tiêu đề
                    XSSFRow nameRow1 = sheet.createRow(0);
                    XSSFCell tilteCell = nameRow1.createCell(0);
                    tilteCell.setCellValue("THỐNG KÊ DOANH THU");
                    tilteCell.setCellStyle(boldCenteredStyle);
                    sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 8)); // Gộp ô từ cột 1 đến cột 3
                    
                    //Tạo tên cột.
                    XSSFRow headerRow = sheet.createRow(1);
                    for (int i = 0; i < tbl_ThongKe.getColumnCount(); i++) {
                        headerRow.createCell(i).setCellValue(tbl_ThongKe.getColumnName(i));
                    }
                    
                    // Điền dữ liệu vào các dòng
                    int rowNum = 2; // Bắt đầu từ dòng 2, dòng 0 là tiêu đề
                    for (int i = 0; i < tbl_ThongKe.getRowCount(); i++) {
                        XSSFRow row = sheet.createRow(rowNum++);
                        for (int j = 0; j < tbl_ThongKe.getColumnCount(); j++) {
                            Object value = tbl_ThongKe.getValueAt(i, j);
                            if (value != null) {
                                row.createCell(j).setCellValue(value.toString());
                            }
                        }
                    }
                    
                    // Tạo style in đậm và căn giữa cho các dòng tiền.
                    XSSFCellStyle boldCenteredStylePriceGreen = workbook.createCellStyle();
                    XSSFFont GreenFont = workbook.createFont();
                    //Thiết lập chữ Green
                    GreenFont.setBold(true);
                    GreenFont.setFontHeightInPoints((short) 12);
                    GreenFont.setColor(IndexedColors.GREEN.getIndex());
                    boldCenteredStylePriceGreen.setFont(GreenFont);
                    boldCenteredStylePriceGreen.setAlignment(HorizontalAlignment.CENTER);
                    
                    
                    XSSFCellStyle boldCenteredStylePriceRed = workbook.createCellStyle();
                    XSSFFont RedFont = workbook.createFont();
                    //Thiết lập chữ RED
                    RedFont.setBold(true);
                    RedFont.setFontHeightInPoints((short) 12);
                    RedFont.setColor(IndexedColors.RED.getIndex());
                    boldCenteredStylePriceRed.setFont(RedFont);
                    boldCenteredStylePriceRed.setAlignment(HorizontalAlignment.CENTER);
                    
                    
                    XSSFCellStyle boldCenteredStylePriceBlue = workbook.createCellStyle();
                    XSSFFont BlueFont = workbook.createFont();
                    //Thiết lập chữ BLUE
                    BlueFont.setBold(true);
                    BlueFont.setFontHeightInPoints((short) 12);
                    BlueFont.setColor(IndexedColors.BLUE.getIndex());
                    boldCenteredStylePriceBlue.setFont(BlueFont);
                    boldCenteredStylePriceBlue.setAlignment(HorizontalAlignment.CENTER);
                    
                    

                    // Xuống 2 dòng rồi ghi tổng hóa đơn và tổng tiền
                    rowNum += 2;
                    
                    //Tạo dòng "Tổng Hóa Đơn
                    XSSFRow totalBillRow = sheet.createRow(rowNum++);
                    XSSFCell totalBillLabelCell = totalBillRow.createCell(0);
                    totalBillLabelCell.setCellValue("Tổng Hóa Đơn:");
                    //Thiết lập in đậm cho cột "Tổng Hóa Đơn"
                    totalBillLabelCell.setCellStyle(boldCenteredStylePriceGreen);
                    XSSFCell totalBillValueCell = totalBillRow.createCell(1);
                    ////Thiết lập in đậm cho giá trị "Tổng Hóa Đơn"
                    totalBillValueCell.setCellValue(lb_AllBill.getText());
                    totalBillValueCell.setCellStyle(boldCenteredStylePriceGreen);
                    
                    //Tạo dòng Tổng Tiền 
                    XSSFRow totalMoneyRow = sheet.createRow(rowNum++);
                    XSSFCell totalMoneyLabelCell = totalMoneyRow.createCell(0);
                    totalMoneyLabelCell.setCellValue("Tổng Tiền:");
                    //Thiết lập in đậm cho cột "Tổng Tiền"
                    totalMoneyLabelCell.setCellStyle(boldCenteredStylePriceRed);
                    XSSFCell totalMoneyValueCell = totalMoneyRow.createCell(1);
                    ////Thiết lập in đậm cho giá trị "Tổng Tiền"
                    totalMoneyValueCell.setCellValue(lb_AllTotal.getText());
                    totalMoneyValueCell.setCellStyle(boldCenteredStylePriceRed);
                    
                    //Tạo dòng Tổng Tiền Nhập
                    XSSFRow totalBuyMoneyRow = sheet.createRow(rowNum++);
                    XSSFCell totalBuyMoneyLabelCell = totalBuyMoneyRow.createCell(0);
                    totalBuyMoneyLabelCell.setCellValue("Tổng Tiền Nhập:");
                    //Thiết lập in đậm cho cột "Tổng Tiền Nhập"
                    totalBuyMoneyLabelCell.setCellStyle(boldCenteredStylePriceBlue);
                    XSSFCell totalBuyMoneyValueCell = totalBuyMoneyRow.createCell(1);
                    ////Thiết lập in đậm cho giá trị "Tổng Tiền Nhập"
                    totalBuyMoneyValueCell.setCellValue(lb_AllTotalBuy.getText());
                    totalBuyMoneyValueCell.setCellStyle(boldCenteredStylePriceBlue);

                    // Ghi lại file
                    try (FileOutputStream out = new FileOutputStream(fileToSave)) {
                        workbook.write(out);
                    }

                    JOptionPane.showMessageDialog(this, "Xuất Hóa Đơn Excel Thành Công!");
                } catch (IOException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Xuất Hóa Đơn Excel Thất Bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
    }//GEN-LAST:event_btn_ExcelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ThongKeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThongKeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThongKeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThongKeForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ThongKeForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Lable1;
    private javax.swing.JLabel Lable2;
    private javax.swing.JLabel Lable3;
    private javax.swing.JButton btn_Excel;
    private javax.swing.JComboBox<String> cbb_Chon;
    private javax.swing.JComboBox<String> cbb_ChonThang;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lb_AllBill;
    private javax.swing.JLabel lb_AllTotal;
    private javax.swing.JLabel lb_AllTotalBuy;
    private javax.swing.JLabel lb_ToTal;
    private javax.swing.JLabel lb_TotalBill;
    private javax.swing.JLabel lb_TotalBill_Month;
    private javax.swing.JLabel lb_Total_Month;
    private javax.swing.JTable tbl_ThongKe;
    // End of variables declaration//GEN-END:variables
}
