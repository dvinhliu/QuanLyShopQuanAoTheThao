/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;

/**
 *
 * @author ACER
 */
public class AccountModule extends javax.swing.JFrame {

    /**
     * Creates new form AccountModule
     */
    private boolean  isAddMode;
    AccountForm acf;
    Connection con;
    public AccountModule(AccountForm af, boolean isAddMode) {
        initComponents();
        this.acf = af;
        this.isAddMode = isAddMode;
        UpdateButtonState();
    }
    private void UpdateButtonState()
    {
        if (isAddMode)
        {
            // Nếu đang ở chế độ sửa, làm mờ nút Thêm và hiển thị nút Sửa
            btn_Them.setEnabled(true);
            btn_Sua.setEnabled(false);
            lbl_Tittle.setText("THÊM THÔNG TIN TÀI KHOẢN");
        }
        else
        {
            // Nếu đang ở chế độ sửa, làm mờ nút Thêm và hiển thị nút Sửa
            btn_Them.setEnabled(false);
            btn_Sua.setEnabled(true);
            lbl_Tittle.setText("SỬA THÔNG TIN TÀI KHOẢN");
        }
    }
    private boolean CheckInput()
    {
        if (txt_TK.getText().isEmpty() && txt_MK.getText().isEmpty())
        {
            return false;
        }
        return true;
    }
    private boolean KT_TenTK(String ten)
    {
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT * FROM TAIKHOAN WHERE TAIKHOAN = '" + txt_TK.getText().trim() + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next())
            {
                st.close();
                rs.close();
                con.close();
                return false;
            }
            else
            {
                st.close();
                rs.close();
                con.close();
                return true;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }
    private void Clear()
    {
        txt_IDTK.setText("");
        txt_TK.setText("");
        txt_MK.setText("");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lbl_Exit = new javax.swing.JLabel();
        lbl_Tittle = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_MK = new javax.swing.JTextField();
        txt_IDTK = new javax.swing.JTextField();
        txt_TK = new javax.swing.JTextField();
        btn_Them = new javax.swing.JButton();
        btn_Sua = new javax.swing.JButton();
        btn_Huy = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
                .addGap(0, 490, Short.MAX_VALUE)
                .addComponent(lbl_Exit))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(lbl_Exit))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 520, 30));

        lbl_Tittle.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbl_Tittle.setForeground(new java.awt.Color(3, 172, 220));
        lbl_Tittle.setText("jLabel2");
        getContentPane().add(lbl_Tittle, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("ID Tài Khoản:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Tài Khoản :");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, -1, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel4.setText("Mật Khẩu :");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        txt_MK.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_MK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_MKKeyTyped(evt);
            }
        });
        getContentPane().add(txt_MK, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 370, -1));

        txt_IDTK.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        getContentPane().add(txt_IDTK, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 90, 370, -1));

        txt_TK.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_TKKeyTyped(evt);
            }
        });
        getContentPane().add(txt_TK, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 130, 370, -1));

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
        getContentPane().add(btn_Them, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 230, 80, 30));

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
        getContentPane().add(btn_Sua, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 230, 80, 30));

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
        getContentPane().add(btn_Huy, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 230, 80, 30));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_ExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_ExitMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_lbl_ExitMouseClicked

    private void btn_ThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemActionPerformed
        // TODO add your handling code here:
        if (CheckInput())
        {
            try
            {
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn thêm thông tin tài khoản này không", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    if (KT_TenTK(txt_TK.getText().trim()))
                    {
                        con = nhom13_shopquanaothethao.Connection.GetConnection();
                        String sql = "INSERT INTO TAIKHOAN VALUES (?,?,?)";
                        PreparedStatement ps = con.prepareStatement(sql);
                        ps.setString(1, txt_IDTK.getText());
                        ps.setString(2, txt_TK.getText());
                        ps.setString(3, txt_MK.getText());
                        int x = ps.executeUpdate();
                        if (x >= 1)
                        {
                            JOptionPane.showMessageDialog(rootPane, "Thêm Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                            Clear();
                            acf.loadDatatbl();
                            ps.close();
                            con.close();
                            this.dispose();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Trùng tài khoản", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng kiểm tra lại thông tin", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btn_ThemActionPerformed

    private void btn_HuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_HuyActionPerformed
        // TODO add your handling code here:
        txt_TK.setText("");
        txt_MK.setText("");
    }//GEN-LAST:event_btn_HuyActionPerformed

    private void btn_SuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SuaActionPerformed
        // TODO add your handling code here:
        if (CheckInput())
        {
            try
            {
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn sửa thông tin tài khoản này không", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    if (KT_TenTK(txt_TK.getText().trim()))
                    {
                        con = nhom13_shopquanaothethao.Connection.GetConnection();
                        String sql = "UPDATE TAIKHOAN SET TAIKHOAN = '" + txt_TK.getText().trim() + "', MATKHAU = '" + txt_MK.getText().trim() + "' WHERE IDTAIKHOAN = '" + txt_IDTK.getText().trim() + "'";
                        Statement st = con.createStatement();
                        int x = st.executeUpdate(sql);
                        if (x >= 1)
                        {
                            JOptionPane.showMessageDialog(rootPane, "Sửa Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                            Clear();
                            acf.loadDatatbl();
                            st.close();
                            con.close();
                            this.dispose();
                        }
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(rootPane, "Trùng tài khoản", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng kiểm tra lại thông tin", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btn_SuaActionPerformed

    private void txt_TKKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_TKKeyTyped
        // TODO add your handling code here:
        if (!Character.isLetter(evt.getKeyChar()) && !Character.isDigit(evt.getKeyChar()))
        {
            evt.consume();
        }
    }//GEN-LAST:event_txt_TKKeyTyped

    private void txt_MKKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_MKKeyTyped
        // TODO add your handling code here:
        if (!Character.isLetter(evt.getKeyChar()) && !Character.isDigit(evt.getKeyChar()))
        {
            evt.consume();
        }
    }//GEN-LAST:event_txt_MKKeyTyped

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
            java.util.logging.Logger.getLogger(AccountModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AccountModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AccountModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AccountModule.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AccountForm accountForm = new AccountForm();
                new AccountModule(accountForm, false).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_Huy;
    public javax.swing.JButton btn_Sua;
    public javax.swing.JButton btn_Them;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JLabel lbl_Exit;
    public javax.swing.JLabel lbl_Tittle;
    public javax.swing.JTextField txt_IDTK;
    public javax.swing.JTextField txt_MK;
    public javax.swing.JTextField txt_TK;
    // End of variables declaration//GEN-END:variables
}
