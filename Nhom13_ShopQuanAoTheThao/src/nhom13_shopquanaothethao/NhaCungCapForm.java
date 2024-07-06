/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven_cell.TableActionCellEditor;
import raven_cell.TableActionCellRender;
import raven_cell.TableActionEvent;

public class NhaCungCapForm extends javax.swing.JFrame {

    Connection con;
    DefaultTableModel model;

    public NhaCungCapForm() {
        initComponents();
        
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                NhaCungCapModule ncc = new NhaCungCapModule(NhaCungCapForm.this, false);
                ncc.txt_MaNCC.setEnabled(false);
                ncc.txt_MaNCC.setText(model.getValueAt(row, 0).toString());
                ncc.txt_TenNCC.setText(model.getValueAt(row, 1).toString());
                ncc.txt_DiaChiNCC.setText(model.getValueAt(row, 2).toString());
                ncc.txt_SDT.setText(model.getValueAt(row, 3).toString());
                ncc.txt_EmailNCC.setText(model.getValueAt(row, 4).toString());
                ncc.txt_TenNCC.requestFocus();
                ncc.setVisible(true);
            }

            @Override
            public void onDelete(int row) {
                if (tblNhaCungCap.isEditing())
                {
                    tblNhaCungCap.getCellEditor().stopCellEditing();
                }
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn xóa nhà cung cấp này không?", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    try
                    {
                        con = nhom13_shopquanaothethao.Connection.GetConnection();
                        String sql = "DELETE FROM NHACUNGCAP WHERE IDNHACUNGCAP = '" + model.getValueAt(row, 0).toString() + "'";
                        Statement st = con.createStatement();
                        int x = st.executeUpdate(sql);
                        if (x >= 1)
                        {
                            JOptionPane.showMessageDialog(rootPane, "Xóa Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                            st.close();
                            con.close();
                            loadNhaCungCap();
                        }
                    }
                    catch (SQLException e)
                    {
                        JOptionPane.showMessageDialog(rootPane, e.toString(), "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                        
                    }
                }
            }
        };
        tblNhaCungCap.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        tblNhaCungCap.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event));
        model = (DefaultTableModel) tblNhaCungCap.getModel();
        
        enableEdt();
        loadNhaCungCap();
        loadDataCBB();
        txt_TimKiemNCC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TimKiem();
            }
        });
    }
    
    private void enableEdt() {
        txt_MaNCC.setEnabled(false);
        txt_TenNCC.setEnabled(false);
        txt_DiaChiNCC.setEnabled(false);
        txt_SDT.setEnabled(false);
        txt_EmailNCC.setEnabled(false);
    }

    private void loadDataCBB() {
        cbb_TimKiem.removeAllItems();
        for (int i = 0; i < tblNhaCungCap.getColumnCount(); i++) {
            cbb_TimKiem.addItem(tblNhaCungCap.getColumnName(i));
        }
    }

    public void loadNhaCungCap() {
        tblNhaCungCap.setRowHeight(35);
        tblNhaCungCap.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        try {
            model.setRowCount(0);
            con = (Connection) nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT * FROM NHACUNGCAP";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Object[] row = {rs.getString("IDNhaCungCap"), rs.getString("TenNhaCungCap"), rs.getString("DiaChi"), rs.getString("SDT"), rs.getString("Email")};
                model.addRow(row);
            }
            tblNhaCungCap.setModel(model);
            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void TimKiem() {
        String timKiem = txt_TimKiemNCC.getText().trim();
        Object selectedItem = cbb_TimKiem.getSelectedItem();
        if (selectedItem != null) {
            if (timKiem.isEmpty()) {
                loadNhaCungCap();
            } else {
                try {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "SELECT * FROM NHACUNGCAP WHERE ";
                    if (cbb_TimKiem.getSelectedItem().toString().equals("Mã nhà cung cấp")) {
                        sql += "IDNHACUNGCAP LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Tên nhà cung cấp")) {
                        sql += "TenNhaCungCap LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Địa chỉ")) {
                        sql += "DiaChi LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Số điện thoại")) {
                        sql += "SDT LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Email")) {
                        sql += "EMAIL LIKE ?";
                    }

                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, "%" + timKiem + "%");
                    ResultSet rs = preparedStatement.executeQuery();

                    model.setRowCount(0);
                    while (rs.next()) {
                        Object[] row = {rs.getString("IDNhaCungCap"), rs.getString("TenNhaCungCap"), rs.getString("DiaChi"), rs.getString("SDT"), rs.getString("Email")};
                        model.addRow(row);
                    }
                    preparedStatement.close();
                    rs.close();
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn tìm kiếm theo", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private String loadMaNCC(Connection con) {
        String IdAuto = "";
        try {
            String sql = "SELECT TOP 1 IDNhaCungCap FROM NhaCungCap ORDER BY IDNhaCungCap DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int lastID = Integer.parseInt(rs.getString("IDNhaCungCap").substring(3));
                int lastNumber = lastID + 1;
                IdAuto = "NCC" + String.format("%02d", lastNumber);
            } else {
                IdAuto = "NCC01";
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return IdAuto;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblNhaCungCap = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_MaNCC = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_TenNCC = new javax.swing.JTextField();
        txt_SDT = new javax.swing.JTextField();
        txt_DiaChiNCC = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt_TimKiemNCC = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbb_TimKiem = new javax.swing.JComboBox<>();
        btn_ThemSP = new javax.swing.JButton();
        btn_Reset = new javax.swing.JButton();
        txt_EmailNCC = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setPreferredSize(new java.awt.Dimension(1360, 679));

        tblNhaCungCap.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblNhaCungCap.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã nhà cung cấp", "Tên nhà cung cấp", "Địa chỉ", "Số điện thoại", "Email", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblNhaCungCap.setSelectionBackground(new java.awt.Color(57, 137, 111));
        tblNhaCungCap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNhaCungCapMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblNhaCungCap);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("Mã nhà cung cấp");

        txt_MaNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_MaNCC.setActionCommand("<Not Set>");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Tên nhà cung cấp");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Số điện thoại");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel6.setText("Địa chỉ");

        txt_TenNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TenNCC.setActionCommand("<Not Set>");

        txt_SDT.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_SDT.setActionCommand("<Not Set>");

        txt_DiaChiNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_DiaChiNCC.setActionCommand("<Not Set>");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel9.setText("Tìm kiếm theo");

        txt_TimKiemNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TimKiemNCC.setActionCommand("<Not Set>");

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel10.setText("Nhập");

        cbb_TimKiem.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        cbb_TimKiem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(cbb_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(txt_TimKiemNCC))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel9))
                        .addGap(32, 32, 32))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txt_TimKiemNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(cbb_TimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(8, Short.MAX_VALUE))
        );

        btn_ThemSP.setBackground(new java.awt.Color(3, 172, 220));
        btn_ThemSP.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_ThemSP.setForeground(new java.awt.Color(255, 255, 255));
        btn_ThemSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-add-administrator-26.png"))); // NOI18N
        btn_ThemSP.setText("Thêm");
        btn_ThemSP.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_ThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ThemSPActionPerformed(evt);
            }
        });

        btn_Reset.setBackground(new java.awt.Color(3, 172, 220));
        btn_Reset.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btn_Reset.setForeground(new java.awt.Color(255, 255, 255));
        btn_Reset.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/icons8-reset-26 (1).png"))); // NOI18N
        btn_Reset.setText("Làm mới");
        btn_Reset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_Reset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ResetActionPerformed(evt);
            }
        });

        txt_EmailNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_EmailNCC.setActionCommand("<Not Set>");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel7.setText("Email");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txt_MaNCC))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txt_TenNCC, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txt_DiaChiNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_EmailNCC)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addGap(115, 115, 115)
                .addComponent(btn_ThemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(103, 103, 103)
                .addComponent(btn_Reset, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_MaNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txt_TenNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_SDT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_DiaChiNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(txt_EmailNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_ThemSP)
                    .addComponent(btn_Reset))
                .addContainerGap())
        );

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Danh sách nhà cung cấp");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 349, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ThemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemSPActionPerformed
        NhaCungCapModule ncc = new NhaCungCapModule(this, true);
        ncc.txt_MaNCC.setText(loadMaNCC(nhom13_shopquanaothethao.Connection.GetConnection()));
        ncc.txt_MaNCC.setEnabled(false);
        ncc.setVisible(true);
    }//GEN-LAST:event_btn_ThemSPActionPerformed

    private void btn_ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ResetActionPerformed
        txt_MaNCC.setText("");
        txt_TenNCC.setText("");
        txt_SDT.setText("");
        txt_DiaChiNCC.setText("");
        txt_EmailNCC.setText("");
        
        txt_TimKiemNCC.setText("");
        cbb_TimKiem.setSelectedIndex(-1);
        loadNhaCungCap();
    }//GEN-LAST:event_btn_ResetActionPerformed

    private void tblNhaCungCapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNhaCungCapMouseClicked
        int selectedRow = tblNhaCungCap.getSelectedRow();
        if (selectedRow != -1) {
            String maNCC = tblNhaCungCap.getValueAt(selectedRow, 0).toString();
            String tenNCC = tblNhaCungCap.getValueAt(selectedRow, 1).toString();
            String diaChi = tblNhaCungCap.getValueAt(selectedRow, 2).toString();
            String soDienThoai = tblNhaCungCap.getValueAt(selectedRow, 3).toString();
            String email = tblNhaCungCap.getValueAt(selectedRow, 4).toString();

            txt_MaNCC.setText(maNCC);
            txt_TenNCC.setText(tenNCC);
            txt_DiaChiNCC.setText(diaChi);
            txt_SDT.setText(soDienThoai);
            txt_EmailNCC.setText(email);
        }
    }//GEN-LAST:event_tblNhaCungCapMouseClicked

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
            java.util.logging.Logger.getLogger(NhaCungCapForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NhaCungCapForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NhaCungCapForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NhaCungCapForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NhaCungCapForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Reset;
    private javax.swing.JButton btn_ThemSP;
    private javax.swing.JComboBox<String> cbb_TimKiem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblNhaCungCap;
    private javax.swing.JTextField txt_DiaChiNCC;
    private javax.swing.JTextField txt_EmailNCC;
    private javax.swing.JTextField txt_MaNCC;
    private javax.swing.JTextField txt_SDT;
    private javax.swing.JTextField txt_TenNCC;
    private javax.swing.JTextField txt_TimKiemNCC;
    // End of variables declaration//GEN-END:variables
}
