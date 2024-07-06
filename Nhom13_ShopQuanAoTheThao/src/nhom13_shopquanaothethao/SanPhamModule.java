/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.JOptionPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SanPhamModule extends javax.swing.JFrame {

    private boolean isAddMode;
    SanPhamForm spf;
    Connection con;

    public SanPhamModule(SanPhamForm sf, boolean isAddMode) {
        initComponents();
        loadCBB();
        loadCBBNCC();
        this.spf = sf;
        this.isAddMode = isAddMode;
        UpdateButtonState();
    }

    private void UpdateButtonState() {
        if (isAddMode) {
            // Nếu đang ở chế độ sửa, làm mờ nút Thêm và hiển thị nút Sửa
            btn_Them.setEnabled(true);
            btn_Sua.setEnabled(false);
            lbl_Tittle.setText("THÊM THÔNG TIN NHÀ CUNG CẤP");
        } else {
            // Nếu đang ở chế độ sửa, làm mờ nút Thêm và hiển thị nút Sửa
            btn_Them.setEnabled(false);
            btn_Sua.setEnabled(true);
            lbl_Tittle.setText("SỬA THÔNG TIN NHÀ CUNG CẤP");
        }
    }

    private boolean CheckInput() {
        if (txt_TenSP.getText().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Tên sản phẩm không được bỏ trống", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        if (txt_GiaNhapSP.getText().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Giá nhập sản phẩm không được bỏ trống", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        try {
            double giaNhap = Double.parseDouble(txt_GiaNhapSP.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(rootPane, "Giá nhập sản phẩm phải là số", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        // Kiểm tra radio button trong rdoGroupSize
        boolean isRadioButtonSelected = false;
        Enumeration<AbstractButton> buttons = rdoGroupSize.getElements();
        while (buttons.hasMoreElements()) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                isRadioButtonSelected = true;
                break;
            }
        }
        if (!isRadioButtonSelected) {
            JOptionPane.showMessageDialog(rootPane, "Bạn phải chọn kích thước sản phẩm", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    private void loadCBB() {
        cbbHang.removeAllItems();
        try {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT TENHANG FROM HANGSANPHAM";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cbbHang.addItem(rs.getString("TENHANG"));
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadCBBNCC() {
        cbbNCC.removeAllItems();
        try {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT TenNhaCungCap FROM NhaCungCap";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                cbbNCC.addItem(rs.getString("TenNhaCungCap"));
            }
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void Clear() {
        txt_TenSP.setText("");
        txt_GiaNhapSP.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rdoGroupSize = new javax.swing.ButtonGroup();
        btn_Huy = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lbl_Exit = new javax.swing.JLabel();
        btn_Sua = new javax.swing.JButton();
        btn_Them = new javax.swing.JButton();
        lbl_Tittle = new javax.swing.JLabel();
        txt_MaSP = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_TenSP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_GiaNhapSP = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cbbHang = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        rdo_XL = new javax.swing.JRadioButton();
        rdo_L = new javax.swing.JRadioButton();
        rdo_S = new javax.swing.JRadioButton();
        rdo_M = new javax.swing.JRadioButton();
        lblHinhSP = new javax.swing.JLabel();
        btnLoadAnh = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cbbNCC = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        btn_Huy.setBackground(new java.awt.Color(153, 153, 153));
        btn_Huy.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btn_Huy.setForeground(new java.awt.Color(255, 255, 255));
        btn_Huy.setText("Hủy");
        btn_Huy.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Huy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_HuyActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(3, 172, 220));

        lbl_Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancel.png"))); // NOI18N
        lbl_Exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_Exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_ExitMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbl_Exit))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbl_Exit))
        );

        btn_Sua.setBackground(new java.awt.Color(255, 132, 44));
        btn_Sua.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btn_Sua.setForeground(new java.awt.Color(255, 255, 255));
        btn_Sua.setText("Sửa");
        btn_Sua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Sua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SuaActionPerformed(evt);
            }
        });

        btn_Them.setBackground(new java.awt.Color(3, 172, 220));
        btn_Them.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        btn_Them.setForeground(new java.awt.Color(255, 255, 255));
        btn_Them.setText("Thêm");
        btn_Them.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Them.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemActionPerformed(evt);
            }
        });

        lbl_Tittle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_Tittle.setForeground(new java.awt.Color(3, 172, 220));
        lbl_Tittle.setText("jLabel2");

        txt_MaSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_MaSP.setActionCommand("<Not Set>");

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("Mã sản phẩm");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Tên sản phẩm");

        txt_TenSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TenSP.setActionCommand("<Not Set>");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Tên hãng");

        txt_GiaNhapSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_GiaNhapSP.setActionCommand("<Not Set>");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel5.setText("Hình ảnh");

        cbbHang.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbbHang.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Size", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16))); // NOI18N

        rdoGroupSize.add(rdo_XL);
        rdo_XL.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdo_XL.setText("XL");
        rdo_XL.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdo_XL.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdo_XLItemStateChanged(evt);
            }
        });

        rdoGroupSize.add(rdo_L);
        rdo_L.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdo_L.setText("L");
        rdo_L.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdo_L.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdo_LItemStateChanged(evt);
            }
        });

        rdoGroupSize.add(rdo_S);
        rdo_S.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdo_S.setText("S");
        rdo_S.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdo_S.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdo_SItemStateChanged(evt);
            }
        });

        rdoGroupSize.add(rdo_M);
        rdo_M.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        rdo_M.setText("M");
        rdo_M.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        rdo_M.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                rdo_MItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(rdo_S)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(rdo_M)
                .addGap(56, 56, 56)
                .addComponent(rdo_L)
                .addGap(61, 61, 61)
                .addComponent(rdo_XL)
                .addGap(34, 34, 34))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdo_S)
                    .addComponent(rdo_M)
                    .addComponent(rdo_L)
                    .addComponent(rdo_XL))
                .addGap(0, 8, Short.MAX_VALUE))
        );

        btnLoadAnh.setBackground(new java.awt.Color(29, 105, 174));
        btnLoadAnh.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        btnLoadAnh.setForeground(new java.awt.Color(255, 255, 255));
        btnLoadAnh.setText("Chọn hình");
        btnLoadAnh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoadAnhActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel6.setText("Giá nhập");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel7.setText("Nhà cung cấp");

        cbbNCC.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        cbbNCC.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbl_Tittle)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel6)
                                            .addComponent(txt_GiaNhapSP, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cbbHang, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel3))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(cbbNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(113, 113, 113)
                                        .addComponent(txt_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblHinhSP, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLoadAnh)))
                        .addGap(42, 42, 42))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btn_Huy, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(213, 213, 213))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txt_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addGap(18, 18, 18)
                    .addComponent(jLabel2)
                    .addContainerGap(520, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Tittle)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(txt_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_GiaNhapSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(99, 99, 99)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(cbbHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cbbNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblHinhSP, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnLoadAnh, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Huy, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(23, 23, 23))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(89, 89, 89)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jLabel2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(txt_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(224, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_HuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_HuyActionPerformed
        txt_MaSP.setText("");
        txt_TenSP.setText("");
        txt_GiaNhapSP.setText("");
        rdoGroupSize.clearSelection();
    }//GEN-LAST:event_btn_HuyActionPerformed

    private void lbl_ExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_ExitMouseClicked
        this.dispose();
    }//GEN-LAST:event_lbl_ExitMouseClicked

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        String txt_Size = "S";
        if (rdo_S.isSelected()) {
            txt_Size = "S";
        } else if (rdo_M.isSelected()) {
            txt_Size = "M";
        } else if (rdo_L.isSelected()) {
            txt_Size = "L";
        } else if (rdo_XL.isSelected()) {
            txt_Size = "XL";
        }

        String txt_Hang = "";
        Object selectedValue = cbbHang.getSelectedItem();
        if (selectedValue != null) {
            if (selectedValue.toString().equals("Adidas")) {
                txt_Hang = "HSP01";
            } else if (selectedValue.toString().equals("Nike")) {
                txt_Hang = "HSP02";
            } else if (selectedValue.toString().equals("Puma")) {
                txt_Hang = "HSP03";
            }
        }
        
        String txt_NCC = "";
        Object selectedValue1 = cbbNCC.getSelectedItem();
        if (selectedValue1 != null) {
            if (selectedValue1.toString().equals("Hoàng Tây")) {
                txt_NCC = "NCC01";
            } else if (selectedValue1.toString().equals("Minh Tú")) {
                txt_NCC = "NCC02";
            } else txt_NCC = "NCC03";
        }

        if (CheckInput()) {
            try {
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn sửa thông tin sản phẩm này không?", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql;
                    if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                        sql = "UPDATE SANPHAM SET TENSANPHAM = ?, IDHANGSANPHAM = ?, IDNHACUNGCAP = ?, HINHANH = ?, SIZE = ?, GIANHAP = ?, GIABAN = ? WHERE IDSANPHAM = ?";
                    } else {
                        sql = "UPDATE SANPHAM SET TENSANPHAM = ?, IDHANGSANPHAM = ?, IDNHACUNGCAP = ?, SIZE = ?, GIANHAP = ?, GIABAN = ? WHERE IDSANPHAM = ?";
                    }

                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, txt_TenSP.getText().trim());
                    ps.setString(2, txt_Hang.trim());
                    ps.setString(3, txt_NCC.trim());
                    if (selectedImagePath != null && !selectedImagePath.isEmpty()) {
                        ps.setString(4, selectedImagePath.trim());
                        ps.setString(5, txt_Size.trim());
                        ps.setString(6, txt_GiaNhapSP.getText().trim());
                        int giaban = Integer.parseInt(txt_GiaNhapSP.getText()) * 2;
                        ps.setInt(7, (int) giaban);
                        ps.setString(8, txt_MaSP.getText().trim());
                    } else {
                        ps.setString(4, txt_Size.trim());
                        ps.setString(5, txt_GiaNhapSP.getText().trim());
                        int giaban = Integer.parseInt(txt_GiaNhapSP.getText()) * 2;
                        ps.setInt(6, (int) giaban);
                        ps.setString(7, txt_MaSP.getText().trim());
                    }

                    int x = ps.executeUpdate();
                    if (x > 0) {
                        JOptionPane.showMessageDialog(rootPane, "Sửa Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                        Clear();
                        spf.loadSanPham(); // Load lại dữ liệu sau khi cập nhật
                    }

                    // Đóng PreparedStatement tại đây
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    // Đóng kết nối tại đây trong khối finally
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }


    }//GEN-LAST:event_btn_SuaActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed

        String txt_Size = "";
        if (rdo_S.isSelected()) {
            txt_Size = "S";
        } else if (rdo_M.isSelected()) {
            txt_Size = "M";
        } else if (rdo_L.isSelected()) {
            txt_Size = "L";
        } else if (rdo_XL.isSelected()) {
            txt_Size = "XL";
        }

        String txt_Hang = "";
        if (cbbHang.getSelectedItem().toString().equals("Adidas")) {
            txt_Hang = "HSP01";
        } else if (cbbHang.getSelectedItem().toString().equals("Nike")) {
            txt_Hang = "HSP02";
        } else if (cbbHang.getSelectedItem().toString().equals("Puma")) {
            txt_Hang = "HSP03";
        }

        String txt_NCC = "";
        Object selectedValue1 = cbbNCC.getSelectedItem();
        if (selectedValue1 != null) {
            if (selectedValue1.toString().equals("Hoàng Tây")) {
                txt_NCC = "NCC01";
            } else if (selectedValue1.toString().equals("Minh Tú")) {
                txt_NCC = "NCC02";
            } else txt_NCC = "NCC03";
        }
        
        if (CheckInput()) {
            try {
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn thêm thông tin sản phẩm này không", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "INSERT INTO SANPHAM (IDSANPHAM, TENSANPHAM, IDHANGSANPHAM, IDNHACUNGCAP, HINHANH, SIZE, GIANHAP, GIABAN) VALUES (?,?,?,?,?,?,?,?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, txt_MaSP.getText());
                    ps.setString(2, txt_TenSP.getText());
                    ps.setString(3, txt_Hang);
                    ps.setString(4, txt_NCC);
                    ps.setString(5, selectedImagePath);
                    ps.setString(6, txt_Size);
                    ps.setString(7, txt_GiaNhapSP.getText());
                    int giaban = Integer.parseInt(txt_GiaNhapSP.getText()) * 2;
                    ps.setInt(8, (int) giaban);
                    int x = ps.executeUpdate();
                    if (x >= 1) {
                        JOptionPane.showMessageDialog(rootPane, "Thêm Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                        Clear();
                        spf.loadSanPham();
                        ps.close();
                        con.close();
                        this.dispose();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_ThemActionPerformed

    private void rdo_XLItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdo_XLItemStateChanged

    }//GEN-LAST:event_rdo_XLItemStateChanged

    private void rdo_LItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdo_LItemStateChanged

    }//GEN-LAST:event_rdo_LItemStateChanged

    private void rdo_MItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdo_MItemStateChanged

    }//GEN-LAST:event_rdo_MItemStateChanged

    private void rdo_SItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_rdo_SItemStateChanged

    }//GEN-LAST:event_rdo_SItemStateChanged

    private String selectedImagePath = "";

    private void btnLoadAnhActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoadAnhActionPerformed

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif");
        chooser.setFileFilter(filter);
//        File default = new File()
        int returnVal = chooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            selectedImagePath = file.getName();

            try {
                BufferedImage originalImage = ImageIO.read(file);
                int newWidth = 150;
                int newHeight = 150;
                Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
                ImageIcon icon = new ImageIcon(scaledImage);
                lblHinhSP.setIcon(icon);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btnLoadAnhActionPerformed

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
            java.util.logging.Logger.getLogger(SanPhamModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SanPhamModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SanPhamModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SanPhamModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SanPhamForm sanPhamForm = new SanPhamForm(null);
                new SanPhamModule(sanPhamForm, false).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoadAnh;
    public javax.swing.JButton btn_Huy;
    public javax.swing.JButton btn_Sua;
    public javax.swing.JButton btn_Them;
    public javax.swing.JComboBox<String> cbbHang;
    public javax.swing.JComboBox<String> cbbNCC;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    public javax.swing.JLabel lblHinhSP;
    private javax.swing.JLabel lbl_Exit;
    public javax.swing.JLabel lbl_Tittle;
    private javax.swing.ButtonGroup rdoGroupSize;
    public javax.swing.JRadioButton rdo_L;
    public javax.swing.JRadioButton rdo_M;
    public javax.swing.JRadioButton rdo_S;
    public javax.swing.JRadioButton rdo_XL;
    public javax.swing.JTextField txt_GiaNhapSP;
    public javax.swing.JTextField txt_MaSP;
    public javax.swing.JTextField txt_TenSP;
    // End of variables declaration//GEN-END:variables
}
