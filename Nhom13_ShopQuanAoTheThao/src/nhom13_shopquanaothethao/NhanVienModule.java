/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public class NhanVienModule extends javax.swing.JFrame {

    /**
     * Creates new form NhanVienModule
     */
    
    private boolean isAddMode;
    NhanVienForm nvf;
    Connection con;
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
    DefaultComboBoxModel<String> model1 = new DefaultComboBoxModel<>();

    
    public NhanVienModule(NhanVienForm nf, boolean isAddMode) throws SQLException {
        initComponents();
        this.nvf = nf;
        this.isAddMode = isAddMode;
        UpdateButtonState();
        load_cbb_VTCV();
        load_cbb_IDTK();
        loadCbb_GT();
        cbb_VTCV.setSelectedIndex(0);
    }

    private void UpdateButtonState()
    {
        if (isAddMode)
        {
            // Nếu đang ở chế độ sửa, làm mờ nút Thêm và hiển thị nút Sửa
            btn_Them1.setEnabled(true);
            btn_Sua1.setEnabled(false);
            lbl_Title.setText("THÊM THÔNG TIN TÀI KHOẢN");
        }
        else
        {
            // Nếu đang ở chế độ sửa, làm mờ nút Thêm và hiển thị nút Sửa
            btn_Them1.setEnabled(false);
            btn_Sua1.setEnabled(true);
            lbl_Title.setText("SỬA THÔNG TIN TÀI KHOẢN");
        }
    }
    
    private boolean checkInput()
    {
        if(txt_MaNV.getText().isEmpty() || txt_TenNV.getText().isEmpty() || cbb_GT.getSelectedItem().toString().isEmpty() || txt_SDT.getText().isEmpty() 
                || txt_NgaySinh.getText().isEmpty() || txt_Email.getText().isEmpty()  || txt_DiaChi.getText().isEmpty() || cbb_VTCV.getSelectedItem().toString().isEmpty()     
                || txt_Luong.getText().isEmpty() || txt_NVL.getText().isEmpty() || cbb_IDTaiKhoan.getSelectedItem().toString().isEmpty())
        {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng kiểm tra lại thông tin, không được để trống!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        if(!isValidPhoneNumber(txt_SDT.getText().toString().trim()))
        {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng kiểm tra SDT!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        if(!isValidEmail(txt_Email.getText().toString().trim()))
        {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng kiểm tra Email!", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        else
        {
            return true;
        }
    }
    
    
    private void load_cbb_VTCV() throws SQLException
    {
        try {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT ViTriCongViec FROM NHANVIEN GROUP BY ViTriCongViec";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            ArrayList<String> positions = new ArrayList<>();
            while(rs.next())
            {
                String vitri = rs.getString("ViTriCongViec");
                model.addElement(vitri);
            }
            con.close();
            st.close();
            rs.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        cbb_VTCV.setModel(model);
    }
    
    private void load_cbb_IDTK() throws SQLException
    {
        try {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql1 = "SELECT IDTaiKhoan FROM TAIKHOAN";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql1);        
            while(rs.next())
            {
                String IdString = rs.getString("IDTaiKhoan");
                model1.addElement(IdString);
            }
            con.close();
            st.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        cbb_IDTaiKhoan.setModel(model1);
    }
    
    private void loadCbb_GT()
    {
        cbb_GT.removeAllItems();
        cbb_GT.addItem("Nam");
        cbb_GT.addItem("Nữ");
        
        cbb_GT.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED)
                {
                    String selected = (String) cbb_GT.getSelectedItem();
                    if(selected.equals("Nam"))
                    {
                        model1.addElement("Nam");
                    }
                    else if(selected.equals("Nữ"))
                    {
                        model1.addElement("Nữ");
                    }
                }
            }
        });
    }
    
    // Phương thức kiểm tra số điện thoại không chứa số 0 ở đầu
    public static boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy để kiểm tra số điện thoại không chứa số 0 ở đầu
        String phoneNumberRegex = "^0\\d{9}$"; // Số điện thoại có 10 chữ số, chứa số 0 ở đầu

        // Tạo pattern từ biểu thức chính quy
        Pattern pattern = Pattern.compile(phoneNumberRegex);
        Matcher matcher = pattern.matcher(phoneNumber);

        // Kiểm tra nếu số điện thoại khớp với biểu thức chính quy
        return matcher.matches();
    }

    // Phương thức kiểm tra định dạng email
    public static boolean isValidEmail(String email) {
        // Biểu thức chính quy để kiểm tra định dạng email
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Tạo pattern từ biểu thức chính quy
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        // Kiểm tra nếu email khớp với biểu thức chính quy
        return matcher.matches();
    }
   
    private void Clear()
    {
        txt_MaNV.setText("");
        txt_TenNV.setText("");
        txt_NgaySinh.setText("");
        cbb_GT.setSelectedItem(0);
        txt_DiaChi.setText("");
        txt_SDT.setText("");
        txt_Email.setText("");
        cbb_VTCV.setSelectedIndex(0);
        txt_Luong.setText("");
        txt_NVL.setText("");
        cbb_IDTaiKhoan.setSelectedIndex(0);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_Title = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txt_MaNV = new javax.swing.JTextField();
        txt_NgaySinh = new javax.swing.JTextField();
        txt_TenNV = new javax.swing.JTextField();
        txt_SDT = new javax.swing.JTextField();
        txt_DiaChi = new javax.swing.JTextField();
        txt_Luong = new javax.swing.JTextField();
        txt_NVL = new javax.swing.JTextField();
        txt_Email = new javax.swing.JTextField();
        btn_Them1 = new javax.swing.JButton();
        btn_Sua1 = new javax.swing.JButton();
        btn_Huy1 = new javax.swing.JButton();
        btn_Close = new javax.swing.JButton();
        cbb_VTCV = new javax.swing.JComboBox<>();
        cbb_IDTaiKhoan = new javax.swing.JComboBox<>();
        cbb_GT = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(850, 330));
        setSize(new java.awt.Dimension(850, 330));

        lbl_Title.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lbl_Title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbl_Title.setText("jLabel1");

        jLabel2.setText("Mã Nhân Viên: ");
        jLabel2.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel2.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel2.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel3.setText("Giới Tính:");
        jLabel3.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel3.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel3.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel4.setText("Ngày Sinh:");
        jLabel4.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel4.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel4.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel5.setText("Vị Trí Công Việc:");
        jLabel5.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel5.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel5.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel6.setText("ID Tài Khoản:");
        jLabel6.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel6.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel6.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel7.setText("Tên Nhân Viên:");
        jLabel7.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel7.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel7.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel8.setText("SĐT:");
        jLabel8.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel8.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel8.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel9.setText("Địa Chỉ:");
        jLabel9.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel9.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel9.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel10.setText("Lương:");
        jLabel10.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel10.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel10.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel11.setText("Ngày Vào Làm:");
        jLabel11.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel11.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel11.setPreferredSize(new java.awt.Dimension(96, 18));

        jLabel12.setText("Email:");
        jLabel12.setMaximumSize(new java.awt.Dimension(96, 18));
        jLabel12.setMinimumSize(new java.awt.Dimension(96, 18));
        jLabel12.setPreferredSize(new java.awt.Dimension(96, 18));

        txt_MaNV.setEditable(false);
        txt_MaNV.setEnabled(false);
        txt_MaNV.setMaximumSize(new java.awt.Dimension(193, 25));
        txt_MaNV.setMinimumSize(new java.awt.Dimension(193, 25));
        txt_MaNV.setPreferredSize(new java.awt.Dimension(193, 25));

        txt_NgaySinh.setMaximumSize(new java.awt.Dimension(193, 25));
        txt_NgaySinh.setMinimumSize(new java.awt.Dimension(193, 25));
        txt_NgaySinh.setPreferredSize(new java.awt.Dimension(193, 25));

        txt_TenNV.setMaximumSize(new java.awt.Dimension(193, 25));
        txt_TenNV.setMinimumSize(new java.awt.Dimension(193, 25));
        txt_TenNV.setPreferredSize(new java.awt.Dimension(193, 25));

        txt_SDT.setMaximumSize(new java.awt.Dimension(193, 25));
        txt_SDT.setMinimumSize(new java.awt.Dimension(193, 25));
        txt_SDT.setPreferredSize(new java.awt.Dimension(193, 25));

        txt_DiaChi.setMaximumSize(new java.awt.Dimension(193, 25));
        txt_DiaChi.setMinimumSize(new java.awt.Dimension(193, 25));
        txt_DiaChi.setPreferredSize(new java.awt.Dimension(193, 25));

        txt_Luong.setMaximumSize(new java.awt.Dimension(193, 25));
        txt_Luong.setMinimumSize(new java.awt.Dimension(193, 25));
        txt_Luong.setPreferredSize(new java.awt.Dimension(193, 25));

        txt_NVL.setMaximumSize(new java.awt.Dimension(193, 25));
        txt_NVL.setMinimumSize(new java.awt.Dimension(193, 25));
        txt_NVL.setPreferredSize(new java.awt.Dimension(193, 25));

        txt_Email.setMaximumSize(new java.awt.Dimension(193, 25));
        txt_Email.setMinimumSize(new java.awt.Dimension(193, 25));
        txt_Email.setPreferredSize(new java.awt.Dimension(193, 25));

        btn_Them1.setBackground(new java.awt.Color(3, 172, 220));
        btn_Them1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btn_Them1.setForeground(new java.awt.Color(255, 255, 255));
        btn_Them1.setText("Thêm");
        btn_Them1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Them1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Them1ActionPerformed(evt);
            }
        });

        btn_Sua1.setBackground(new java.awt.Color(255, 132, 44));
        btn_Sua1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btn_Sua1.setForeground(new java.awt.Color(255, 255, 255));
        btn_Sua1.setText("Sửa");
        btn_Sua1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Sua1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Sua1ActionPerformed(evt);
            }
        });

        btn_Huy1.setBackground(new java.awt.Color(153, 153, 153));
        btn_Huy1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btn_Huy1.setForeground(new java.awt.Color(255, 255, 255));
        btn_Huy1.setText("Hủy");
        btn_Huy1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Huy1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_Huy1ActionPerformed(evt);
            }
        });

        btn_Close.setBackground(new java.awt.Color(153, 153, 153));
        btn_Close.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btn_Close.setForeground(new java.awt.Color(255, 255, 255));
        btn_Close.setText("Đóng");
        btn_Close.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Close.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CloseActionPerformed(evt);
            }
        });

        cbb_VTCV.setMaximumSize(new java.awt.Dimension(193, 25));
        cbb_VTCV.setMinimumSize(new java.awt.Dimension(193, 25));
        cbb_VTCV.setPreferredSize(new java.awt.Dimension(193, 25));
        cbb_VTCV.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbb_VTCVItemStateChanged(evt);
            }
        });

        cbb_IDTaiKhoan.setMaximumSize(new java.awt.Dimension(193, 25));
        cbb_IDTaiKhoan.setMinimumSize(new java.awt.Dimension(193, 25));
        cbb_IDTaiKhoan.setPreferredSize(new java.awt.Dimension(193, 25));

        cbb_GT.setMaximumSize(new java.awt.Dimension(193, 25));
        cbb_GT.setMinimumSize(new java.awt.Dimension(193, 25));
        cbb_GT.setPreferredSize(new java.awt.Dimension(193, 25));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbb_GT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_NgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbb_VTCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbb_IDTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(45, 45, 45)
                        .addComponent(txt_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_NVL, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_DiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(151, 151, 151)
                                .addComponent(txt_Email, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txt_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(45, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(26, 233, Short.MAX_VALUE)
                .addComponent(btn_Them1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btn_Sua1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btn_Huy1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(253, 253, 253))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_Title, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_DiaChi, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_Luong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_NVL, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_MaNV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbb_GT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_NgaySinh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbb_VTCV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbb_IDTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(81, 81, 81)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Them1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Sua1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_Huy1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_Close, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_Them1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Them1ActionPerformed
       // TODO add your handling code here:
            try
            {
                if (checkInput())
                   {
                        if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn thêm thông tin tài khoản này không", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                        {

                                con = nhom13_shopquanaothethao.Connection.GetConnection();
                                String sql = "SET DATEFORMAT dmy INSERT INTO NHANVIEN VALUES (?,?,?,?,?,?,?,?,?,?,?)";
                                PreparedStatement ps = con.prepareStatement(sql);
                                ps.setString(1, txt_MaNV.getText());
                                ps.setString(2, txt_TenNV.getText());
                                ps.setString(3, txt_NgaySinh.getText());
                                ps.setString(4, cbb_GT.getSelectedItem().toString());
                                ps.setString(5, txt_DiaChi.getText());
                                ps.setString(6, txt_SDT.getText());
                                ps.setString(7, txt_Email.getText());
                                ps.setString(8, cbb_VTCV.getSelectedItem().toString());
                                ps.setInt(9, Integer.parseInt(txt_Luong.getText().toString()));
                                ps.setString(10, txt_NVL.getText());
                                ps.setString(11, cbb_IDTaiKhoan.getSelectedItem().toString());

                                int x = ps.executeUpdate();
                                if (x >= 1)
                                {
                                    JOptionPane.showMessageDialog(rootPane, "Thêm Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                    Clear();
                                    nvf.loadDataTbl();
                                    ps.close();
                                    con.close();
                                }
                        }
                    }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        
        
    }//GEN-LAST:event_btn_Them1ActionPerformed

    private void btn_Sua1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Sua1ActionPerformed
        // TODO add your handling code here:
      
            try
            {
                if (checkInput())
                {
                    if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn sửa thông tin tài khoản này không", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                    {

                            con = nhom13_shopquanaothethao.Connection.GetConnection();
                            String sql = "SET DATEFORMAT dmy UPDATE NHANVIEN SET TenNhanVien = N'" + txt_TenNV.getText().trim() + "', NgaySinh = '" + txt_NgaySinh.getText().trim() + "' "
                                    + " , GioiTinh = N'" + cbb_GT.getSelectedItem().toString()+ "' , DiaChi = N'" + txt_DiaChi.getText().trim() + "' , SDT = '" + txt_SDT.getText().trim() + "'"
                                    + ", Email = N'" + txt_Email.getText().trim() + "' , ViTriCongViec = N'" + cbb_VTCV.getSelectedItem().toString().trim() + "' , Luong = '" + txt_Luong.getText().trim() + "'"
                                    + ", NgayVaoLam = '" + txt_NVL.getText().trim() + "' , IDTaiKhoan = '" + cbb_IDTaiKhoan.getSelectedItem().toString() + "'  WHERE IDNhanVien = '" + txt_MaNV.getText().trim() + "'";
                            Statement st = con.createStatement();
                            int x = st.executeUpdate(sql);
                            if (x >= 1)
                            {
                                JOptionPane.showMessageDialog(rootPane, "Sửa Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                Clear();
                                nvf.loadDataTbl();
                                st.close();
                                con.close();
                                this.dispose();
                            }
                    }
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
 
        
    }//GEN-LAST:event_btn_Sua1ActionPerformed

    private void btn_Huy1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_Huy1ActionPerformed
       // TODO add your handling code here:
        txt_TenNV.setText("");
        txt_NgaySinh.setText("");
        cbb_GT.setSelectedItem(0);
        txt_DiaChi.setText("");
        txt_SDT.setText("");
        txt_Email.setText("");
        cbb_VTCV.setSelectedIndex(1);
        txt_Luong.setText("");
        txt_NVL.setText("");
        cbb_IDTaiKhoan.setSelectedIndex(0);
    }//GEN-LAST:event_btn_Huy1ActionPerformed

    private void btn_CloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CloseActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_btn_CloseActionPerformed

    private void cbb_VTCVItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbb_VTCVItemStateChanged
        // TODO add your handling code here:
        if(cbb_VTCV.getSelectedIndex()== 0)
        {
            cbb_IDTaiKhoan.setEnabled(false);
        }
        else
            cbb_IDTaiKhoan.setEnabled(true);
    }//GEN-LAST:event_cbb_VTCVItemStateChanged

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
            java.util.logging.Logger.getLogger(NhanVienModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NhanVienModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NhanVienModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NhanVienModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NhanVienForm nhanVienForm = new NhanVienForm();
                try {
                    new NhanVienModule(nhanVienForm,false).setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(NhanVienModule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Close;
    private javax.swing.JButton btn_Huy1;
    private javax.swing.JButton btn_Sua1;
    private javax.swing.JButton btn_Them1;
    public javax.swing.JComboBox<String> cbb_GT;
    public javax.swing.JComboBox<String> cbb_IDTaiKhoan;
    public javax.swing.JComboBox<String> cbb_VTCV;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel lbl_Title;
    public javax.swing.JTextField txt_DiaChi;
    public javax.swing.JTextField txt_Email;
    public javax.swing.JTextField txt_Luong;
    public javax.swing.JTextField txt_MaNV;
    public javax.swing.JTextField txt_NVL;
    public javax.swing.JTextField txt_NgaySinh;
    public javax.swing.JTextField txt_SDT;
    public javax.swing.JTextField txt_TenNV;
    // End of variables declaration//GEN-END:variables
}
