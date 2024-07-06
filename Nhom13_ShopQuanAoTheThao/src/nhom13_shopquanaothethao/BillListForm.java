/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven_cell.TableActionCellEditor2;
import raven_cell.TableActionCellRender2;
import raven_cell.TableActionEvent2;
/**
 *
 * @author ACER
 */
public class BillListForm extends javax.swing.JFrame {

    /**
     * Creates new form BillListForm
     */
    Connection con;
    DefaultTableModel tblmodel;
    public BillListForm() {
        initComponents();
        tblmodel = (DefaultTableModel) tbl_Bills.getModel();
        loadtblBills();
        TableActionEvent2 event = new TableActionEvent2() {
            @Override
            public void onDetail(int row) {
                ChiTietHDForm ct = new ChiTietHDForm();
                ct.setVisible(true);
                
                try
                {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sqlKTid = "SELECT IDKH FROM HOADON WHERE IDHOADON = '" + tblmodel.getValueAt(row, 1) + "'";
                    Statement stKTid = con.createStatement();
                    ResultSet rsKTid = stKTid.executeQuery(sqlKTid);
                    
                    String sql = "";
                    String idKH = null;
                    if (rsKTid.next())
                    {
                        idKH = rsKTid.getString("IDKH");
                        if (idKH == null || idKH.isEmpty())
                        {
                            sql = "SELECT HOADON.IDHOADON, HOADON.NGAYLAPHD, NHANVIEN.TENNHANVIEN, HOADON.HINHTHUC, HOADON.GIAMGIA, HOADON.THANHTIEN " +
                                    "FROM HOADON, NHANVIEN " +
                                    "WHERE HOADON.IDNhanVien = NHANVIEN.IDNHANVIEN AND HOADON.IDHOADON = '" + tblmodel.getValueAt(row, 1) + "'";
                        }
                        else
                        {
                            sql = "SELECT HOADON.IDHOADON, HOADON.NGAYLAPHD, NHANVIEN.TENNHANVIEN, KHACHHANG.TENKH, KHACHHANG.SDT, HOADON.DIACHI, HOADON.HINHTHUC, HOADON.GIAMGIA, HOADON.THANHTIEN " +
                                        "FROM HOADON, NHANVIEN, KHACHHANG " +
                                        "WHERE HOADON.IDNhanVien = NHANVIEN.IDNHANVIEN AND HOADON.IDKH = KHACHHANG.IDKH AND HOADON.IDHOADON = '" + tblmodel.getValueAt(row, 1) + "'";
                        }
                    }
                    
                    stKTid.close();
                    rsKTid.close();
                    
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    
                    if (rs.next())
                    {
                        if (rs.getString("HINHTHUC").equals("Trực Tiếp"))
                        {
                            double tong = Double.parseDouble(rs.getString("THANHTIEN")) / (1 - (Double.parseDouble(rs.getString("GIAMGIA")) / 100));
                            NumberFormat numberFormat = NumberFormat.getNumberInstance();
                            
                            ct.lbl_MaHD.setText(rs.getString("IDHOADON"));
                            ct.lbl_MaNV.setText(rs.getString("TENNHANVIEN"));
                            
                            if (idKH == null)
                            {
                                ct.lbl_TenKH.setText("Khách vãng lai");
                                ct.lbl_SDT.setText("Khách vãng lai");
                                ct.lbl_DiaChi.setText("Khách vãng lai");
                            }
                            else
                            {
                                ct.lbl_TenKH.setText(rs.getString("TENKH"));
                                ct.lbl_SDT.setText(rs.getString("SDT"));
                                ct.lbl_DiaChi.setText(rs.getString("DIACHI"));
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                            String ngayLapHD = dateFormat.format(rs.getTimestamp("NGAYLAPHD"));
                            ct.lbl_NgayMua.setText(ngayLapHD);

                            ct.lbl_HinhThuc.setText(rs.getString("HINHTHUC"));

                            String formattedTongT = numberFormat.format(tong);
                            ct.lbl_TongTien.setText(formattedTongT + " VNĐ");

                            ct.lbl_GiamGia.setText(rs.getString("GIAMGIA") + "%");
                            ct.lbl_PhiShip.setText("0 VNĐ");

                            String formattedThanhT = numberFormat.format(rs.getInt("THANHTIEN"));
                            ct.lbl_ThanhTien.setText(formattedThanhT + " VNĐ");
                        }
                        else
                        {
                            double tong = (Double.parseDouble(rs.getString("THANHTIEN")) - 100000) / (1 - (Double.parseDouble(rs.getString("GIAMGIA")) / 100));
                            NumberFormat numberFormat = NumberFormat.getNumberInstance();

                            ct.lbl_MaHD.setText(rs.getString("IDHOADON"));
                            ct.lbl_MaNV.setText(rs.getString("TENNHANVIEN"));
                            ct.lbl_TenKH.setText(rs.getString("TENKH"));
                            ct.lbl_SDT.setText(rs.getString("SDT"));
                            ct.lbl_DiaChi.setText(rs.getString("DIACHI"));

                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                            String ngayLapHD = dateFormat.format(rs.getTimestamp("NGAYLAPHD"));
                            ct.lbl_NgayMua.setText(ngayLapHD);

                            ct.lbl_HinhThuc.setText(rs.getString("HINHTHUC"));

                            String formattedTongT = numberFormat.format(tong);
                            ct.lbl_TongTien.setText(formattedTongT + " VNĐ");

                            ct.lbl_GiamGia.setText(rs.getString("GIAMGIA") + "%");
                            ct.lbl_PhiShip.setText("100.000 VNĐ");

                            String formattedThanhT = numberFormat.format(rs.getInt("THANHTIEN"));
                            ct.lbl_ThanhTien.setText(formattedThanhT + " VNĐ");
                        }
                    }
                    st.close();
                    rs.close();
                    
                    String sql1 = "SELECT SANPHAM.TENSANPHAM, CHITIETHOADON.SOLUONG, SANPHAM.SIZE, SANPHAM.GIABAN, CHITIETHOADON.THANHTIEN " +
                                    "FROM CHITIETHOADON, SANPHAM " +
                                    "WHERE CHITIETHOADON.IDSanPham = SANPHAM.IDSanPham AND CHITIETHOADON.IDHOADON = '" + tblmodel.getValueAt(row, 1) + "'";
                    Statement st1 = con.createStatement();
                    ResultSet rs1 = st1.executeQuery(sql1);
                    
                    DefaultTableModel tblct;
                    tblct = (DefaultTableModel) ct.tbl_CTHD.getModel();
                    int count = 1;
                    while (rs1.next())
                    {
                        Object[] rowct = {count,  rs1.getString("TENSANPHAM"), rs1.getString("SOLUONG"), rs1.getString("SIZE"), rs1.getString("GIABAN"), rs1.getString("THANHTIEN") };
                        tblct.addRow(rowct);
                        count++;
                        
                        
                    }
                    st1.close();
                    rs1.close();
                    con.close();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
           
            @Override
            public void onDelete(int row) {
                if (tbl_Bills.isEditing())
                {
                    tbl_Bills.getCellEditor().stopCellEditing();
                }
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn xóa", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    try
                    {
                        con = nhom13_shopquanaothethao.Connection.GetConnection();
                        
                        String sql = "DELETE FROM CHITIETHOADON WHERE IDHOADON ='" + tblmodel.getValueAt(row, 1) + "'";
                        Statement st = con.createStatement();
                        st.executeUpdate(sql);
                        st.close();
                        
                        String sql1 = "DELETE FROM HOADON WHERE IDHOADON ='" + tblmodel.getValueAt(row, 1) + "'";
                        Statement st1 = con.createStatement();
                        st1.executeUpdate(sql1);
                        st1.close();
                        
                        con.close();
                        JOptionPane.showMessageDialog(rootPane, "Xóa thành công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                        loadtblBills();
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        tbl_Bills.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender2());
        tbl_Bills.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor2(event));
        
        txt_search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Search();
            }
        });
    }
    private void Search()
    {
        if (txt_search.getText().isEmpty())
        {
            loadtblBills();
        }
        else
        {
            try
            {
                tblmodel.setRowCount(0);
                
                con = nhom13_shopquanaothethao.Connection.GetConnection();
                String sql = "SELECT IDHOADON, NGAYLAPHD, IDNHANVIEN, HINHTHUC, THANHTIEN FROM HOADON WHERE IDHOADON = '" + txt_search.getText() + "'";
                
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                
                int count = 1;
                while (rs.next())
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    String ngayLapHD = dateFormat.format(rs.getTimestamp("NGAYLAPHD"));
                    Object[] row = { count, rs.getString("IDHOADON"), ngayLapHD, rs.getString("IDNHANVIEN"), rs.getString("HINHTHUC"), rs.getString("THANHTIEN") };
                    tblmodel.addRow(row);
                    count++;
                }

                st.close();
                rs.close();
                con.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }  
    }
    private void loadtblBills()
    {
        tblmodel.setRowCount(0);
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT IDHOADON, NGAYLAPHD, IDNHANVIEN, HINHTHUC, THANHTIEN FROM HOADON";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int count = 1;
            while (rs.next())
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                String ngayLapHD = dateFormat.format(rs.getTimestamp("NGAYLAPHD"));
                Object[] row = { count, rs.getString("IDHOADON"), ngayLapHD, rs.getString("IDNHANVIEN"), rs.getString("HINHTHUC"), rs.getString("THANHTIEN") };
                tblmodel.addRow(row);
                count++;
            }
            st.close();
            rs.close();
            con.close();
            tbl_Bills.setModel(tblmodel);
        }
        catch (SQLException e)
        {
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

        jPanel1 = new javax.swing.JPanel();
        lbl_Exit = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Bills = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_search = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

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
            .addComponent(lbl_Exit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(3, 172, 220));
        jLabel2.setText("Danh sách hóa đơn");

        tbl_Bills.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbl_Bills.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "ID Hóa Đơn", "Ngày Lập HĐ", "NV Tiếp Nhận", "Hình Thức", "Thành Tiền", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_Bills.setRowHeight(40);
        tbl_Bills.setSelectionBackground(new java.awt.Color(57, 137, 111));
        jScrollPane1.setViewportView(tbl_Bills);
        if (tbl_Bills.getColumnModel().getColumnCount() > 0) {
            tbl_Bills.getColumnModel().getColumn(0).setPreferredWidth(6);
            tbl_Bills.getColumnModel().getColumn(1).setPreferredWidth(50);
            tbl_Bills.getColumnModel().getColumn(2).setPreferredWidth(150);
            tbl_Bills.getColumnModel().getColumn(3).setPreferredWidth(50);
            tbl_Bills.getColumnModel().getColumn(4).setPreferredWidth(60);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("Tìm kiếm:");

        txt_search.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addGap(7, 7, 7)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_ExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_ExitMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_lbl_ExitMouseClicked

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
            java.util.logging.Logger.getLogger(BillListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BillListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BillListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BillListForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BillListForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lbl_Exit;
    public javax.swing.JTable tbl_Bills;
    public javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}
