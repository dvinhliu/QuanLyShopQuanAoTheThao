/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import raven_cell.TableActionCellEditor;
import raven_cell.TableActionCellEditor1;
import raven_cell.TableActionCellRender;
import raven_cell.TableActionCellRender1;
import raven_cell.TableActionEvent1;
import raven_cell.TableActionEvent;
/**
 *
 * @author ACER
 */
public class KhoHangForm extends javax.swing.JFrame {

    /**
     * Creates new form KhoHangForm
     */
    Connection con;
    DefaultTableModel tblmodel;
    public KhoHangForm() {
        initComponents();
        tblmodel = (DefaultTableModel) tbl_Inf.getModel();
        scaleImage();
        loadDataCBB();
        AddCategory();
        
        cbb_NCC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNCC = (String) cbb_NCC.getSelectedItem();
                if (selectedNCC != null && !selectedNCC.isEmpty()) 
                {
                    LoadProductByNCC(selectedNCC);
                    CallBtnNew();
                }
            }
        });
        
        if (cbb_NCC.getItemCount() > 0) 
        {
            cbb_NCC.setSelectedIndex(0);
            String firstNCC = (String) cbb_NCC.getSelectedItem();
            if (firstNCC != null && !firstNCC.isEmpty()) {
                LoadProductByNCC(firstNCC);
            }
        }
        
        txt_Search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchTK();
            }
        });
        TableActionEvent event1 = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                try
                {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql =  "SELECT SANPHAM.IDSanPham, SANPHAM.TenSanPham, HANGSANPHAM.TenHang, SANPHAM.IDNhaCungCap, SANPHAM.Size, SANPHAM.GiaNhap, SANPHAM.HinhAnh " +
                                    "FROM SANPHAM " +
                                    "INNER JOIN HANGSANPHAM ON SANPHAM.IDHANGSANPHAM = HANGSANPHAM.IDHANGSANPHAM " +
                                    "WHERE SANPHAM.TenSanPham = N'" + tblmodel.getValueAt(row, 1) + "' AND SANPHAM.Size = '" + tblmodel.getValueAt(row, 3) + "'";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    String idsp = "";
                    String tensp = "";
                    String cat = "";
                    String idncc = "";
                    String size = "";
                    int gia = 0;
                    String img = "";
                    while (rs.next())
                    {
                        idsp = rs.getString("IDSANPHAM");
                        tensp = rs.getString("TENSANPHAM");
                        cat = rs.getString("TENHANG");
                        idncc = rs.getString("IDNHACUNGCAP");
                        size = rs.getString("SIZE");
                        gia = rs.getInt("GIANHAP");
                        img = rs.getString("HinhAnh");
                    }
                    st.close();
                    rs.close();
                    con.close();
                    Product pro = new Product(null, KhoHangForm.this, idsp, tensp, cat, idncc, size, gia, img);
                    KhoHangModule module = new KhoHangModule(pro, KhoHangForm.this);
                    module.setVisible(true);
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onDelete(int row) {
                if (tbl_Inf.isEditing())
                {
                    tbl_Inf.getCellEditor().stopCellEditing();
                }
                if (JOptionPane.showConfirmDialog(ProductPanel, "Bạn có chắc muốn xóa?", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    tblmodel.removeRow(row);
                    GetTotal();
                }
            }
        };
        
        tbl_Inf.getColumnModel().getColumn(6).setCellRenderer(new TableActionCellRender());
        tbl_Inf.getColumnModel().getColumn(6).setCellEditor(new TableActionCellEditor(event1));
    }
    
    private void scaleImage()
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/logo.jpg"));
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(lblimg.getWidth(), lblimg.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaleIcon = new ImageIcon(imgScale);
        lblimg.setIcon(scaleIcon);
    }
    
    private void loadDataCBB()
    {
        cbb_NCC.removeAllItems();
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT TENNHACUNGCAP FROM NHACUNGCAP";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                cbb_NCC.addItem(rs.getString("TENNHACUNGCAP"));
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
    
    private void AddCategory()
    {
        CategoryPanel.removeAll();
        JButton d = new JButton("All Categories");
        d.setPreferredSize(new Dimension(CategoryPanel.getWidth(), 45));
        d.setBackground(new Color(50, 55, 89));
        d.setForeground(Color.WHITE);
        d.setFont(new Font("Tahoma", Font.PLAIN, 14));
        d.setCursor(new Cursor(Cursor.HAND_CURSOR));
        d.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txt_Search.setText("");
                SearchTK();
            }
        });
        CategoryPanel.add(d);
        
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT DISTINCT TENHANG FROM HANGSANPHAM";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                JButton b = new JButton(rs.getString("TENHANG"));
                b.setPreferredSize(new Dimension(CategoryPanel.getWidth(), 45));
                b.setBackground(new Color(50, 55, 89));
                b.setForeground(Color.WHITE);
                b.setFont(new Font("Tahoma", Font.PLAIN, 14));
                b.setCursor(new Cursor(Cursor.HAND_CURSOR));
                b.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        for (Component component : ProductPanel.getComponents())
                        {
                            if (component instanceof Product)
                            {
                                Product pro = (Product) component;
                                pro.setVisible(pro.getCategory().toLowerCase().contains(e.getActionCommand().toLowerCase().trim()));
                            }
                        }
                    }
                });
                CategoryPanel.add(b);
            }
            st.close();
            rs.close();
            con.close();
            CategoryPanel.revalidate();
            CategoryPanel.repaint();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    private void SearchTK()
    {
        for (Component component : ProductPanel.getComponents())
        {
            Product pro = (Product) component;
            pro.setVisible(pro.getIdsp().toLowerCase().contains(txt_Search.getText().trim().toLowerCase()));
        }
    }
    
    private void AddProduct(String idsp, String tensp, String cat, String idncc, String size, int gia, String img)
    {
        Product sp = new Product(null, this, idsp, tensp, cat, idncc, size, gia, img);
        ProductPanel.add(sp);
        
    }
    
    private void LoadProductByNCC(String ncc)
    {
        ProductPanel.removeAll();
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT SANPHAM.IDSanPham, SANPHAM.TenSanPham, HANGSANPHAM.TenHang, SANPHAM.IDNhaCungCap, SANPHAM.Size, SANPHAM.GiaNhap, SANPHAM.HinhAnh " +
                        "FROM SANPHAM " +
                        "INNER JOIN HANGSANPHAM ON SANPHAM.IDHANGSANPHAM = HANGSANPHAM.IDHANGSANPHAM " +
                        "INNER JOIN NHACUNGCAP ON SANPHAM.IDNhaCungCap = NHACUNGCAP.IDNhaCungCap " +
                        "WHERE NHACUNGCAP.TENNHACUNGCAP = ?";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, ncc);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String idsp = rs.getString("IDSANPHAM");
                String tensp = rs.getString("TENSANPHAM");
                String cat = rs.getString("TENHANG");
                String idncc = rs.getString("IDNHACUNGCAP");
                String size = rs.getString("SIZE");
                int gia = rs.getInt("GIANHAP");
                String img = rs.getString("HinhAnh");
                AddProduct(idsp, tensp, cat, idncc, size, gia, img);
            }
            rs.close();
            pst.close();
            con.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
        ProductPanel.revalidate();
        ProductPanel.repaint();
    }
    
    public void GetTotal()
    {
        int sum = 0;
        for (int i = 0; i < tblmodel.getRowCount(); i++)
        {
            sum += Integer.parseInt(tblmodel.getValueAt(i, 5).toString());
        }
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        String formattedSum = numberFormat.format(sum);
        lbl_Total.setText(formattedSum);
    }
    public void loadSTT()
    {
        for (int i = 0; i < tblmodel.getRowCount(); i++)
        {
            tblmodel.setValueAt(i + 1, i, 0);
        }
    }
    public String loadMaPN()
    {            
        String MaPNAuto = "";
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT TOP 1 IDPHIEUNHAP FROM PHIEUNHAP ORDER BY IDPHIEUNHAP DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if  (rs.next())
            {
                int lastID = Integer.parseInt(rs.getString("IDPHIEUNHAP").substring(2));
                int lastNumber = lastID + 1;
                MaPNAuto = "PN" + String.format("%03d", lastNumber);
            }
            else
            {
                MaPNAuto = "PN001";
            }
            st.close();
            rs.close();
            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return MaPNAuto;
    }
    public void CallBtnNew()
    {
        btn_newActionPerformed(null);
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
        lblimg = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btn_new = new javax.swing.JButton();
        btn_bill = new javax.swing.JButton();
        lbl_Exit = new javax.swing.JLabel();
        lbl_IDNV = new javax.swing.JLabel();
        lbl_TenNV = new javax.swing.JLabel();
        lbl_Role = new javax.swing.JLabel();
        cbb_NCC = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lbl_Total = new javax.swing.JLabel();
        btn_TaoPhieuNhap = new javax.swing.JButton();
        CategoryPanel = new javax.swing.JPanel();
        ProductPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Inf = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txt_Search = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(3, 172, 220));
        jPanel1.setPreferredSize(new java.awt.Dimension(1358, 91));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Nhập hàng");

        btn_new.setBackground(new java.awt.Color(241, 85, 126));
        btn_new.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_new.setForeground(new java.awt.Color(255, 255, 255));
        btn_new.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/order.png"))); // NOI18N
        btn_new.setText("Làm mới");
        btn_new.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newActionPerformed(evt);
            }
        });

        btn_bill.setBackground(new java.awt.Color(241, 85, 126));
        btn_bill.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_bill.setForeground(new java.awt.Color(255, 255, 255));
        btn_bill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Bill.png"))); // NOI18N
        btn_bill.setText("Phiếu nhập");
        btn_bill.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_bill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_billActionPerformed(evt);
            }
        });

        lbl_Exit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Cancel.png"))); // NOI18N
        lbl_Exit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        lbl_Exit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lbl_ExitMouseClicked(evt);
            }
        });

        lbl_IDNV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_IDNV.setForeground(new java.awt.Color(255, 255, 255));
        lbl_IDNV.setText("IDNV");

        lbl_TenNV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_TenNV.setForeground(new java.awt.Color(255, 255, 255));
        lbl_TenNV.setText("TenNV");

        lbl_Role.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_Role.setForeground(new java.awt.Color(255, 255, 255));
        lbl_Role.setText("ViTriCongViec");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblimg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(42, 42, 42)
                .addComponent(btn_new)
                .addGap(37, 37, 37)
                .addComponent(btn_bill)
                .addGap(43, 43, 43)
                .addComponent(cbb_NCC, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(204, 204, 204)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_IDNV, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_Role))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
                .addComponent(lbl_Exit)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblimg, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_bill)
                            .addComponent(btn_new)
                            .addComponent(jLabel5)
                            .addComponent(cbb_NCC, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lbl_Exit)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_IDNV)
                        .addGap(14, 14, 14)
                        .addComponent(lbl_TenNV)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Role)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 90));

        jPanel2.setBackground(new java.awt.Color(3, 172, 220));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tổng tiền:");

        lbl_Total.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lbl_Total.setForeground(new java.awt.Color(255, 255, 255));
        lbl_Total.setText("0.00");

        btn_TaoPhieuNhap.setBackground(new java.awt.Color(241, 85, 126));
        btn_TaoPhieuNhap.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_TaoPhieuNhap.setForeground(new java.awt.Color(255, 255, 255));
        btn_TaoPhieuNhap.setText("Tạo phiếu nhập");
        btn_TaoPhieuNhap.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_TaoPhieuNhap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TaoPhieuNhapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(843, 843, 843)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lbl_Total)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 233, Short.MAX_VALUE)
                .addComponent(btn_TaoPhieuNhap)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(lbl_Total)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_TaoPhieuNhap, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 1370, 90));
        getContentPane().add(CategoryPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 160, 530));

        ProductPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        getContentPane().add(ProductPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 660, 530));

        tbl_Inf.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbl_Inf.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên SP", "Số lượng", "Size", "Giá nhập", "Thành tiền", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_Inf.setRowHeight(40);
        tbl_Inf.setSelectionBackground(new java.awt.Color(57, 137, 111));
        jScrollPane1.setViewportView(tbl_Inf);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 120, 530, 530));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Tìm kiếm:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 90, -1, 30));

        txt_Search.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        getContentPane().add(txt_Search, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 380, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btn_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newActionPerformed
        // TODO add your handling code here:
        tblmodel.setRowCount(0);
        lbl_Total.setText("0.00");
    }//GEN-LAST:event_btn_newActionPerformed

    private void lbl_ExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_ExitMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_lbl_ExitMouseClicked

    private void btn_TaoPhieuNhapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TaoPhieuNhapActionPerformed
        // TODO add your handling code here:
        if (tblmodel.getRowCount() > 0)
        {
            if (cbb_NCC.getSelectedIndex() != -1)
            {
                String idncc = "";
                int tong = Integer.parseInt(lbl_Total.getText().replace(".", ""));
                String id = "";
                id = loadMaPN();
                try
                {  
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "SELECT IDNHACUNGCAP FROM NHACUNGCAP WHERE TENNHACUNGCAP = N'" + cbb_NCC.getSelectedItem().toString() + "'";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    if (rs.next())
                    {
                        idncc = rs.getString("IDNHACUNGCAP");
                    }
                    st.close();
                    rs.close();

                    String sql1 = "INSERT INTO PHIEUNHAP VALUES (?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(sql1);
                    ps.setString(1, id);

                    Date date = new Date();
                    Timestamp timestamp = new Timestamp(date.getTime());
                    ps.setTimestamp(2, timestamp);

                    ps.setString(3, idncc);
                    ps.setInt(4, tong);

                    ps.executeUpdate();
                    ps.close();

                    JOptionPane.showMessageDialog(rootPane, "Thêm thành công phiếu nhập", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                    for (int i = 0; i < tblmodel.getRowCount(); i++)
                    {
                        String idsp = "";
                        String check = "SELECT IDSanPham FROM SANPHAM WHERE TENSANPHAM = N'" + tblmodel.getValueAt(i, 1) + "' AND SIZE = '" + tblmodel.getValueAt(i, 3) + "'";
                        Statement st1 = con.createStatement();
                        ResultSet rs1 = st1.executeQuery(check);
                        if (rs1.next())
                        {
                            idsp = rs1.getString("IDSANPHAM");
                        }
                        rs1.close();
                        st1.close();

                        String sql2 = "INSERT INTO CHITIET_PN VALUES (?, ?, ?, ?)";
                        PreparedStatement ps2 = con.prepareStatement(sql2);
                        ps2.setString(1, id);
                        ps2.setString(2, idsp);
                        ps2.setInt(3, Integer.parseInt(tblmodel.getValueAt(i, 2).toString()));
                        ps2.setInt(4, Integer.parseInt(tblmodel.getValueAt(i, 5).toString()));

                        ps2.executeUpdate();
                        ps2.close();
                    }
                    con.close();
                    CallBtnNew();
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        else
        {
            JOptionPane.showMessageDialog(ProductPanel, "Vui lòng chọn sản phẩm", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btn_TaoPhieuNhapActionPerformed

    private void btn_billActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_billActionPerformed
        // TODO add your handling code here:
        BillsListPNForm bl = new BillsListPNForm();
        bl.setVisible(true);
    }//GEN-LAST:event_btn_billActionPerformed

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
            java.util.logging.Logger.getLogger(KhoHangForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(KhoHangForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(KhoHangForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(KhoHangForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new KhoHangForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JPanel CategoryPanel;
    public javax.swing.JPanel ProductPanel;
    public javax.swing.JButton btn_TaoPhieuNhap;
    public javax.swing.JButton btn_bill;
    public javax.swing.JButton btn_new;
    public javax.swing.JComboBox<String> cbb_NCC;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel2;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lbl_Exit;
    public javax.swing.JLabel lbl_IDNV;
    public javax.swing.JLabel lbl_Role;
    public javax.swing.JLabel lbl_TenNV;
    public javax.swing.JLabel lbl_Total;
    public javax.swing.JLabel lblimg;
    public javax.swing.JTable tbl_Inf;
    public javax.swing.JTextField txt_Search;
    // End of variables declaration//GEN-END:variables
}
