/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import raven_cell.TableActionCellEditor;
import raven_cell.TableActionCellRender;
import raven_cell.TableActionEvent;
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
public class AccountForm extends javax.swing.JFrame {
    
    Connection con;
    DefaultTableModel tblmodel;
    /**
     * Creates new form AccountForm
     */
    public AccountForm() {
        initComponents();
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                AccountModule acc = new AccountModule(AccountForm.this, false);
                acc.txt_IDTK.setEnabled(false);
                acc.txt_IDTK.setText(tblmodel.getValueAt(row, 0).toString());
                acc.txt_TK.setText(tblmodel.getValueAt(row, 1).toString());
                acc.txt_MK.setText(tblmodel.getValueAt(row, 2).toString());
                acc.txt_TK.requestFocus();
                acc.setVisible(true);
            }

            @Override
            public void onDelete(int row) {
                if (tblTK.isEditing())
                {
                    tblTK.getCellEditor().stopCellEditing();
                }
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn xóa tài khoản này không?", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    try
                    {
                        con = nhom13_shopquanaothethao.Connection.GetConnection();
                        String sql = "DELETE FROM TAIKHOAN WHERE TAIKHOAN = '" + tblmodel.getValueAt(row, 1).toString() + "'";
                        Statement st = con.createStatement();
                        int x = st.executeUpdate(sql);
                        if (x >= 1)
                        {
                            JOptionPane.showMessageDialog(rootPane, "Xóa Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                            st.close();
                            con.close();
                            loadDatatbl();
                        }
                    }
                    catch (SQLException e)
                    {
                        JOptionPane.showMessageDialog(rootPane, e.toString(), "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                }
            }
        };
        tblTK.getColumnModel().getColumn(3).setCellRenderer(new TableActionCellRender());
        tblTK.getColumnModel().getColumn(3).setCellEditor(new TableActionCellEditor(event));
        tblmodel = (DefaultTableModel) tblTK.getModel();
        loadDatatbl();
        loadDataCBB();
        txt_SearchTK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search();
            }
        });
    }
    public void loadDatatbl()
    {
        try
        {
            tblmodel.setRowCount(0);
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT * FROM TAIKHOAN";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                Object[] row = { rs.getString("IDTAIKHOAN"), rs.getString("TAIKHOAN"), rs.getString("MATKHAU") };
                tblmodel.addRow(row);
            }
            tblTK.setModel(tblmodel);
            st.close();
            rs.close();
            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        
    } 
    private void loadDataCBB()
    {
        cbb_sortSearch.removeAllItems();
        for (int i = 0; i < tblTK.getColumnCount() - 1; i++)
        {
            cbb_sortSearch.addItem(tblTK.getColumnName(i));
        }
    }
    private String loadIDTK()
    {            
        String IDTKAuto = "";
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT TOP 1 IDTAIKHOAN FROM TAIKHOAN ORDER BY IDTAIKHOAN DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if  (rs.next())
            {
                int lastID = Integer.parseInt(rs.getString("IDTAIKHOAN").substring(2));
                int lastNumber = lastID + 1;
                IDTKAuto = "TK" + String.format("%03d", lastNumber);
            }
            else
            {
                IDTKAuto = "TK001";
            }
            st.close();
            rs.close();
            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return IDTKAuto;
    }
    private void Search()
    {
        String txt_search = txt_SearchTK.getText().trim();
        Object selectedItem = cbb_sortSearch.getSelectedItem();
        if (selectedItem != null)
        {
            if (txt_search.isEmpty())
            {
                loadDatatbl();
            }
            else
            {
                try
                {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "SELECT * FROM TAIKHOAN WHERE ";
                    if (cbb_sortSearch.getSelectedItem().toString().equals("ID Tài Khoản"))
                    {
                        sql += "IDTAIKHOAN LIKE ?";
                    }
                    else if (cbb_sortSearch.getSelectedItem().toString().equals("Tài Khoản"))
                    {
                        sql += "TAIKHOAN LIKE ?";
                    }
                    else if (cbb_sortSearch.getSelectedItem().toString().equals("Mật Khẩu"))
                    {
                        sql += "MATKHAU LIKE ?";
                    }

                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, "%" + txt_search + "%");
                    ResultSet rs = preparedStatement.executeQuery();
                    
                    tblmodel.setRowCount(0);
                    while (rs.next())
                    {
                        Object[] row = { rs.getString("IDTAIKHOAN"), rs.getString("TAIKHOAN"), rs.getString("MATKHAU") };
                        tblmodel.addRow(row);
                    }
                    preparedStatement.close();
                    rs.close();
                    con.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn tìm kiếm theo", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_IDTK = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txt_TK = new javax.swing.JTextField();
        txt_MK = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txt_SearchTK = new javax.swing.JTextField();
        cbb_sortSearch = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        btn_AddTK = new javax.swing.JButton();
        btn_ResetSearch = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTK = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Quản lý tài khoản", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 18))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("ID Tài Khoản");

        txt_IDTK.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Tài Khoản");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Mật Khẩu");

        txt_TK.setEnabled(false);

        txt_MK.setEnabled(false);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 14))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel4.setText("Nhập :");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel5.setText("Tìm kiếm theo :");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(cbb_sortSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txt_SearchTK, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_SearchTK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbb_sortSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_AddTK.setBackground(new java.awt.Color(3, 172, 220));
        btn_AddTK.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_AddTK.setForeground(new java.awt.Color(255, 255, 255));
        btn_AddTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-add-administrator-26.png"))); // NOI18N
        btn_AddTK.setText("Thêm");
        btn_AddTK.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_AddTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddTKActionPerformed(evt);
            }
        });

        btn_ResetSearch.setBackground(new java.awt.Color(3, 172, 220));
        btn_ResetSearch.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_ResetSearch.setForeground(new java.awt.Color(255, 255, 255));
        btn_ResetSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-reset-26 (1).png"))); // NOI18N
        btn_ResetSearch.setText("Làm mới");
        btn_ResetSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ResetSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ResetSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(btn_AddTK, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addComponent(btn_ResetSearch))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_IDTK))
                        .addGap(38, 38, 38)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txt_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_MK, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(327, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_MK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_TK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_IDTK, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_AddTK)
                            .addComponent(btn_ResetSearch))
                        .addGap(18, 18, 18))))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Danh sách tài khoản");

        tblTK.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblTK.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID Tài Khoản", "Tài Khoản", "Mật Khẩu", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTK.setRowHeight(40);
        tblTK.setSelectionBackground(new java.awt.Color(57, 137, 111));
        tblTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTKMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTK);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 435, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTKMouseClicked
        // TODO add your handling code here:
        int row = tblTK.getSelectedRow();
        if (row != -1)
        {
            txt_IDTK.setText(tblTK.getValueAt(row, 0).toString());
            txt_TK.setText(tblTK.getValueAt(row, 1).toString());
            txt_MK.setText(tblTK.getValueAt(row, 2).toString());
        }
        else
        {
            txt_IDTK.setText("");
            txt_TK.setText("");
            txt_MK.setText("");
        }
    }//GEN-LAST:event_tblTKMouseClicked

    private void btn_ResetSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ResetSearchActionPerformed
        // TODO add your handling code here:
        txt_SearchTK.setText("");
        cbb_sortSearch.setSelectedIndex(-1);
    }//GEN-LAST:event_btn_ResetSearchActionPerformed

    private void btn_AddTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddTKActionPerformed
        // TODO add your handling code here:
        AccountModule acc = new AccountModule(this, true);
        acc.txt_IDTK.setText(loadIDTK());
        acc.txt_IDTK.setEnabled(false);
        acc.setVisible(true);
    }//GEN-LAST:event_btn_AddTKActionPerformed

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
            java.util.logging.Logger.getLogger(AccountForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AccountForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AccountForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AccountForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AccountForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_AddTK;
    public javax.swing.JButton btn_ResetSearch;
    public javax.swing.JComboBox<String> cbb_sortSearch;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JLabel jLabel6;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable tblTK;
    public javax.swing.JTextField txt_IDTK;
    public javax.swing.JTextField txt_MK;
    public javax.swing.JTextField txt_SearchTK;
    public javax.swing.JTextField txt_TK;
    // End of variables declaration//GEN-END:variables
}
