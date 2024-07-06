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
import java.sql.PreparedStatement;
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
public class BillsListPNForm extends javax.swing.JFrame {

    /**
     * Creates new form BillsListPNForm
     */
    Connection con;
    DefaultTableModel tblmodel;
    public BillsListPNForm() {
        initComponents();
        tblmodel = (DefaultTableModel) tbl_Bills.getModel();
        loadtblBills();
        TableActionEvent2 event = new TableActionEvent2() {
            @Override
            public void onDetail(int row) {
                ChiTietPNForm ct = new ChiTietPNForm();
                ct.setVisible(true);
                
                try
                {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "SELECT PHIEUNHAP.IDPHIEUNHAP, PHIEUNHAP.NGAYNHAP, NHACUNGCAP.TENNHACUNGCAP, NHACUNGCAP.DIACHI, NHACUNGCAP.SDT, NHACUNGCAP.EMAIL, PHIEUNHAP.TIENNHAP " +
                                    "FROM PHIEUNHAP, NHACUNGCAP " +
                                    "WHERE PHIEUNHAP.IDNHACUNGCAP = NHACUNGCAP.IDNHACUNGCAP AND PHIEUNHAP.IDPHIEUNHAP = '" + tblmodel.getValueAt(row, 1) + "'";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    
                    if (rs.next())
                    {
                        NumberFormat numberFormat = NumberFormat.getNumberInstance();
                        
                        ct.lbl_MaPN.setText(rs.getString("IDPHIEUNHAP"));
                        
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                        String ngaynhap = dateFormat.format(rs.getTimestamp("NGAYNHAP"));
                        ct.lbl_NgayNhap.setText(ngaynhap);
                        
                        ct.lbl_TenNCC.setText(rs.getString("TENNHACUNGCAP"));
                        ct.lbl_DiaChi.setText(rs.getString("DIACHI"));
                        ct.lbl_SDT.setText(rs.getString("SDT"));
                        ct.lbl_Email.setText(rs.getString("EMAIL"));
                        
                        String formattedTongTien = numberFormat.format(rs.getInt("TIENNHAP"));
                        ct.lbl_TongTien.setText(formattedTongTien + " VNĐ");
                    }
                    st.close();
                    rs.close();
                    
                    String sql1 = "SELECT SANPHAM.TENSANPHAM, CHITIET_PN.SOLUONG, SANPHAM.SIZE, SANPHAM.GIANHAP, CHITIET_PN.THANHTIEN " +
                                    "FROM CHITIET_PN, SANPHAM " +
                                    "WHERE CHITIET_PN.IDSanPham = SANPHAM.IDSanPham AND CHITIET_PN.IDPHIEUNHAP = '" + tblmodel.getValueAt(row, 1) + "'";
                    Statement st1 = con.createStatement();
                    ResultSet rs1 = st1.executeQuery(sql1);
                    
                    DefaultTableModel tblct;
                    tblct = (DefaultTableModel) ct.tbl_CTPN.getModel();
                    int count = 1;
                    while (rs1.next())
                    {
                        Object[] rowct = {count,  rs1.getString("TENSANPHAM"), rs1.getString("SOLUONG"), rs1.getString("SIZE"), rs1.getString("GIANHAP"), rs1.getString("THANHTIEN") };
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
                        
                        String sql = "DELETE FROM CHITIET_PN WHERE IDPHIEUNHAP ='" + tblmodel.getValueAt(row, 1) + "'";
                        Statement st = con.createStatement();
                        st.executeUpdate(sql);
                        st.close();
                        
                        String sql1 = "DELETE FROM PHIEUNHAP WHERE IDPHIEUNHAP ='" + tblmodel.getValueAt(row, 1) + "'";
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
        tbl_Bills.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender2());
        tbl_Bills.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor2(event));
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
                String sql = "SELECT * FROM PHIEUNHAP WHERE IDPHIEUNHAP = '" + txt_search.getText() + "'";
                
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                
                int count = 1;
                while (rs.next())
                {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                    String ngayLapHD = dateFormat.format(rs.getTimestamp("NGAYNHAP"));
                    Object[] row = { count, rs.getString("IDPHIEUNHAP"), ngayLapHD, rs.getString("IDNHACUNGCAP"), rs.getString("TIENNHAP") };
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
            String sql = "SELECT * FROM PHIEUNHAP";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            int count = 1;
            while (rs.next())
            {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                String ngayLapHD = dateFormat.format(rs.getTimestamp("NGAYNHAP"));
                Object[] row = { count, rs.getString("IDPHIEUNHAP"), ngayLapHD, rs.getString("IDNHACUNGCAP"), rs.getString("TIENNHAP") };
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

        jPanel3 = new javax.swing.JPanel();
        lbl_Exit2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Bills = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txt_search = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel3.setBackground(new java.awt.Color(3, 172, 220));
        jPanel3.setPreferredSize(new java.awt.Dimension(30, 30));

        lbl_Exit2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancel.png"))); // NOI18N
        lbl_Exit2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_Exit2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_Exit2MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 700, Short.MAX_VALUE)
                .addComponent(lbl_Exit2))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lbl_Exit2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(3, 172, 220));
        jLabel2.setText("Danh sách phiếu nhập");

        tbl_Bills.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbl_Bills.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "ID Phiếu Nhập", "Ngày Nhập", "ID Nhà CC", "Tổng Tiền Nhập", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
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
            tbl_Bills.getColumnModel().getColumn(1).setPreferredWidth(30);
            tbl_Bills.getColumnModel().getColumn(2).setPreferredWidth(140);
            tbl_Bills.getColumnModel().getColumn(3).setPreferredWidth(35);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("Tìm kiếm:");

        txt_search.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_search, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 730, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_Exit2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_Exit2MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_lbl_Exit2MouseClicked

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
            java.util.logging.Logger.getLogger(BillsListPNForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BillsListPNForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BillsListPNForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BillsListPNForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BillsListPNForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JPanel jPanel4;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lbl_Exit2;
    public javax.swing.JTable tbl_Bills;
    public javax.swing.JTextField txt_search;
    // End of variables declaration//GEN-END:variables
}
