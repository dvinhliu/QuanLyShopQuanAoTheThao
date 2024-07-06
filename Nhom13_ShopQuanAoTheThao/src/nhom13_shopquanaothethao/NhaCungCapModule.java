/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

public class NhaCungCapModule extends javax.swing.JFrame {

    private boolean isAddMode;
    NhaCungCapForm nccf;
    Connection con;

    public NhaCungCapModule(NhaCungCapForm nf, boolean isAddMode) {
        initComponents();
        this.nccf = nf;
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
        if (txt_TenNCC.getText().isEmpty() && txt_DiaChiNCC.getText().isEmpty() && txt_SDT.getText().isEmpty() && txt_EmailNCC.getText().isEmpty()) {
            JOptionPane.showMessageDialog(rootPane, "Thông tin không được bỏ trống", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        } else {
            if (txt_MaNCC.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Mã nhà cung cấp không được bỏ trống", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            if (txt_TenNCC.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Tên nhà cung cấp không được bỏ trống", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            if (txt_SDT.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Số điện thoại không được bỏ trống", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            if (txt_DiaChiNCC.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Địa chỉ nhà cung cấp không được bỏ trống", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }

            if (txt_EmailNCC.getText().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Email không được bỏ trống", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                return false;
            }
        }

        // Kiểm tra định dạng email
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!txt_EmailNCC.getText().matches(emailPattern)) {
            JOptionPane.showMessageDialog(rootPane, "Email không hợp lệ", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        // Kiểm tra định dạng số điện thoại
        String phoneNumberPattern = "^0\\d{9}$";
        if (!txt_SDT.getText().matches(phoneNumberPattern)) {
            JOptionPane.showMessageDialog(rootPane, "Số điện thoại không hợp lệ", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    private void Clear() {
        txt_MaNCC.setText("");
        txt_TenNCC.setText("");
        txt_DiaChiNCC.setText("");
        txt_SDT.setText("");
        txt_EmailNCC.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_Tittle = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        lbl_Exit = new javax.swing.JLabel();
        btn_Huy = new javax.swing.JButton();
        btn_Sua = new javax.swing.JButton();
        btn_Them = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        txt_MaNCC = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txt_TenNCC = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_SDT = new javax.swing.JTextField();
        txt_DiaChiNCC = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txt_EmailNCC = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        lbl_Tittle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_Tittle.setForeground(new java.awt.Color(3, 172, 220));
        lbl_Tittle.setText("jLabel2");

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

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("Mã nhà cung cấp");

        txt_MaNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_MaNCC.setActionCommand("<Not Set>");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Tên nhà cung cấp");

        txt_TenNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TenNCC.setActionCommand("<Not Set>");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Số điện thoại");

        txt_SDT.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_SDT.setActionCommand("<Not Set>");

        txt_DiaChiNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_DiaChiNCC.setActionCommand("<Not Set>");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel6.setText("Địa chỉ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel7.setText("Email");

        txt_EmailNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_EmailNCC.setActionCommand("<Not Set>");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(lbl_Tittle))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(406, 406, 406)
                        .addComponent(jLabel3)))
                .addGap(0, 75, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(136, 136, 136)
                .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btn_Huy, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_MaNCC))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(194, 305, Short.MAX_VALUE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txt_TenNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txt_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(347, 347, 347))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(txt_DiaChiNCC)
                                    .addGap(21, 21, 21)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(txt_EmailNCC))
                            .addContainerGap()))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(lbl_Tittle)
                .addGap(28, 28, 28)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Them, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Sua, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Huy, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(89, 89, 89)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_MaNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(txt_TenNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txt_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel6)
                                .addComponent(jLabel7))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_DiaChiNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addComponent(txt_EmailNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(65, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_ExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_ExitMouseClicked
        this.dispose();
    }//GEN-LAST:event_lbl_ExitMouseClicked

    private void btn_HuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_HuyActionPerformed
        txt_TenNCC.setText("");
        txt_DiaChiNCC.setText("");
        txt_SDT.setText("");
        txt_EmailNCC.setText("");
    }//GEN-LAST:event_btn_HuyActionPerformed

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        if (CheckInput()) {
            try {
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn sửa thông tin nhà cung cấp này không?", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "UPDATE NHACUNGCAP SET TenNhaCungCap = ?, DiaChi = ?, SDT = ?, Email = ? WHERE IDNhaCungCap = ?";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, txt_TenNCC.getText().trim());
                    ps.setString(2, txt_DiaChiNCC.getText().trim());
                    ps.setString(3, txt_SDT.getText().trim());
                    ps.setString(4, txt_EmailNCC.getText().trim());
                    ps.setString(5, txt_MaNCC.getText().trim());
                    int x = ps.executeUpdate();
                    if (x >= 1) {
                        JOptionPane.showMessageDialog(rootPane, "Sửa Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                        Clear();
                        nccf.loadNhaCungCap(); // Load lại dữ liệu sau khi cập nhật
                        ps.close();
                        con.close();
                        this.dispose();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }//GEN-LAST:event_btn_SuaActionPerformed

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed

        if (CheckInput()) {
            try {
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn thêm thông tin sản phẩm này không", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "INSERT INTO NHACUNGCAP VALUES (?,?,?,?,?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, txt_MaNCC.getText());
                    ps.setString(2, txt_TenNCC.getText());
                    ps.setString(3, txt_DiaChiNCC.getText());
                    ps.setString(4, txt_SDT.getText());
                    ps.setString(5, txt_EmailNCC.getText());
                    int x = ps.executeUpdate();
                    if (x >= 1) {
                        JOptionPane.showMessageDialog(rootPane, "Thêm Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                        Clear();
                        nccf.loadNhaCungCap();
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
            java.util.logging.Logger.getLogger(NhaCungCapModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NhaCungCapModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NhaCungCapModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NhaCungCapModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NhaCungCapForm nhaCungCapForm = new NhaCungCapForm();
                new NhaCungCapModule(nhaCungCapForm, false).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_Huy;
    public javax.swing.JButton btn_Sua;
    public javax.swing.JButton btn_Them;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbl_Exit;
    public javax.swing.JLabel lbl_Tittle;
    public javax.swing.JTextField txt_DiaChiNCC;
    public javax.swing.JTextField txt_EmailNCC;
    public javax.swing.JTextField txt_MaNCC;
    public javax.swing.JTextField txt_SDT;
    public javax.swing.JTextField txt_TenNCC;
    // End of variables declaration//GEN-END:variables
}
