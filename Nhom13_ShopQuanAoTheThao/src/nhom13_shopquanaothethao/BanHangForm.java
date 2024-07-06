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
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import javax.swing.JTable;
import raven_cell.TableActionCellEditor;
import raven_cell.TableActionCellEditor1;
import raven_cell.TableActionCellRender;
import raven_cell.TableActionCellRender1;
import raven_cell.TableActionEvent1;
import raven_cell.TableActionEvent;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ACER
 */
public class BanHangForm extends javax.swing.JFrame {

    /**
     * Creates new form BanHangForm
     */
    Connection con;
    public String OderType;
    public String phone = "";
    DefaultTableModel tblmodel;
    public List<Product> productlist;
    public BanHangForm() {
        initComponents();
        productlist = new ArrayList<>();
        scaleImage();
        LoadProduct();
        txt_Search.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchTK();
            }
        });
        tblmodel = (DefaultTableModel) tbl_Inf.getModel();
        TableActionEvent event1 = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                try
                {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql =  "SELECT SANPHAM.IDSanPham, SANPHAM.TenSanPham, HANGSANPHAM.TenHang, SANPHAM.IDNhaCungCap, SANPHAM.Size, SANPHAM.GiaBan, SANPHAM.HinhAnh " +
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
                        gia = rs.getInt("GIABAN");
                        img = rs.getString("HinhAnh");
                    }
                    st.close();
                    rs.close();
                    con.close();
                    Product pro = new Product(BanHangForm.this, null, idsp, tensp, cat, idncc, size, gia, img);
                    BanHangModule module = new BanHangModule(pro, BanHangForm.this);
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
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có chắc muốn xóa?", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
                {
                    tblmodel.removeRow(row);
                    loadSTT();
                    GetTotal();
                }
            }
        };

        tbl_Inf.getColumnModel().getColumn(6).setCellRenderer(new ImageRenderer());
        tbl_Inf.getColumnModel().getColumn(7).setCellRenderer(new TableActionCellRender());
        tbl_Inf.getColumnModel().getColumn(7).setCellEditor(new TableActionCellEditor(event1));
    }
    private class ImageRenderer extends DefaultTableCellRenderer
    {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
        {
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }
    private void scaleImage()
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/logo.jpg"));
        Image img = icon.getImage();
        Image imgScale = img.getScaledInstance(lblimg.getWidth(), lblimg.getHeight(), Image.SCALE_SMOOTH);
        ImageIcon scaleIcon = new ImageIcon(imgScale);
        lblimg.setIcon(scaleIcon);
    }

    private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) 
    {
        Image img = icon.getImage();
        Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImg);
    }
    
    private void SearchTK()
    {
        for (Product pro : productlist)
        {
            if (pro.getIdsp().toLowerCase().contains(txt_Search.getText().trim().toLowerCase()))
            {
                try
                {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql3 = "SELECT SOLUONGTON FROM SANPHAM WHERE IDSANPHAM = '" + pro.getIdsp() + "'";
                    Statement st1 = con.createStatement();
                    ResultSet rs1 = st1.executeQuery(sql3);
                    int slton = 0;
                    if (rs1.next())
                    {
                        slton = rs1.getInt("SOLUONGTON");
                    }
                    
                    if (slton != 0)
                    {
                        for (int i = 0; i < tblmodel.getRowCount(); i++)
                        {
                            if (tblmodel.getValueAt(i, 1).toString().equals(pro.getTensp()) && tblmodel.getValueAt(i, 3).toString().equals(pro.getProductSize()))
                            {
                                int quantity = Integer.parseInt(tblmodel.getValueAt(i, 2).toString()) + 1;
                                if (quantity > slton)
                                {
                                    JOptionPane.showMessageDialog(rootPane, "Vui lòng kiểm tra lại số lượng <= số lượng tồn kho", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                    return;
                                }
                                tblmodel.setValueAt(quantity , i, 2);
                                tblmodel.setValueAt(String.valueOf(Integer.parseInt(tblmodel.getValueAt(i, 2).toString()) * Integer.parseInt(tblmodel.getValueAt(i, 4).toString())), i, 5);
                                GetTotal();
                                
                                st1.close();
                                rs1.close();
                                con.close();
                                
                                return;
                            }
                        }
                        String imgPath = "/Images/" + pro.getImg();
                        ImageIcon imageIcon = new ImageIcon(getClass().getResource(imgPath));
                        ImageIcon resizedIcon = resizeImageIcon(imageIcon, tbl_Inf.getRowHeight(), tbl_Inf.getRowHeight());
                        Object[] row = { 0, pro.getTensp(), 1,  pro.getProductSize(), String.valueOf(pro.getGiaban()), String.valueOf(1 * pro.getGiaban()), resizedIcon };
                        tblmodel.addRow(row);
                        
                        st1.close();
                        rs1.close();
                        con.close();
                        
                        break;
                    }
                    else
                    {
                        st1.close();
                        rs1.close();
                        con.close();
                        JOptionPane.showMessageDialog(rootPane, "Sản phẩm hết hàng. Vui lòng chọn sản phẩm khác", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }
        loadSTT();
        GetTotal();
    }
    private void btnClear()
    {
        btn_takeaw.setForeground(Color.WHITE);
        btn_deli.setForeground(Color.WHITE);
    }

    private void AddProduct(String idsp, String tensp, String cat, String idncc, String size, int gia, String img)
    {
        Product sp = new Product(this, null, idsp, tensp, cat, idncc, size, gia, img);
        productlist.add(sp);
    }
    
    private void LoadProduct()
    {
        productlist.clear();
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql =  "SELECT SANPHAM.IDSanPham, SANPHAM.TenSanPham, HANGSANPHAM.TenHang, SANPHAM.IDNhaCungCap, SANPHAM.Size, SANPHAM.GiaBan, SANPHAM.HinhAnh " +
                            "FROM SANPHAM " +
                            "INNER JOIN HANGSANPHAM ON SANPHAM.IDHANGSANPHAM = HANGSANPHAM.IDHANGSANPHAM " +
                            "GROUP BY SANPHAM.IDSanPham, SANPHAM.TenSanPham, HANGSANPHAM.TenHang, SANPHAM.IDNhaCungCap, SANPHAM.Size, SANPHAM.GiaBan, SANPHAM.HinhAnh";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next())
            {
                String idsp = rs.getString("IDSANPHAM");
                String tensp = rs.getString("TENSANPHAM");
                String cat = rs.getString("TENHANG");
                String idncc = rs.getString("IDNHACUNGCAP");
                String size = rs.getString("SIZE");
                int gia = rs.getInt("GIABAN");
                String img = rs.getString("HinhAnh");
                AddProduct(idsp, tensp, cat, idncc, size, gia, img);
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
    public String loadMaHD()
    {            
        String MaHDAuto = "";
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT TOP 1 IDHOADON FROM HOADON ORDER BY IDHOADON DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if  (rs.next())
            {
                int lastID = Integer.parseInt(rs.getString("IDHOADON").substring(2));
                int lastNumber = lastID + 1;
                MaHDAuto = "HD" + String.format("%03d", lastNumber);
            }
            else
            {
                MaHDAuto = "HD001";
            }
            st.close();
            rs.close();
            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return MaHDAuto;
    }
    
    private String loadIDKH()
    {            
        String IDKHAuto = "";
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT TOP 1 IDKH FROM KHACHHANG ORDER BY IDKH DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if  (rs.next())
            {
                int lastID = Integer.parseInt(rs.getString("IDKH").substring(2));
                int lastNumber = lastID + 1;
                IDKHAuto = "KH" + String.format("%03d", lastNumber);
            }
            else
            {
                IDKHAuto = "KH001";
            }
            st.close();
            rs.close();
            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return IDKHAuto;
    }
    
    public String getIDKH()
    {
        String id = "";
        try
        {
            con = nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT * FROM KHACHHANG WHERE SDT = '" + phone + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if  (rs.next())
            {
                id = rs.getString("IDKH");
            }
            st.close();
            rs.close();
            con.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return id;
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
        btn_takeaw = new javax.swing.JButton();
        btn_deli = new javax.swing.JButton();
        btn_bill = new javax.swing.JButton();
        btn_new = new javax.swing.JButton();
        lbl_IDNV = new javax.swing.JLabel();
        lbl_TenNV = new javax.swing.JLabel();
        lbl_Role = new javax.swing.JLabel();
        lbl_Exit = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        chk_discount = new javax.swing.JCheckBox();
        txt_Discount = new javax.swing.JTextField();
        btn_TaoHoaDon = new javax.swing.JButton();
        lbl_Total = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Inf = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txt_Search = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txt_SDT = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txt_KH = new javax.swing.JTextField();
        btn_AddKH = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(3, 172, 220));

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Bán hàng");

        btn_takeaw.setBackground(new java.awt.Color(241, 85, 126));
        btn_takeaw.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_takeaw.setForeground(new java.awt.Color(255, 255, 255));
        btn_takeaw.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/takeaway.png"))); // NOI18N
        btn_takeaw.setText("Trực tiếp");
        btn_takeaw.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_takeaw.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_takeawActionPerformed(evt);
            }
        });

        btn_deli.setBackground(new java.awt.Color(241, 85, 126));
        btn_deli.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_deli.setForeground(new java.awt.Color(255, 255, 255));
        btn_deli.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Delivery.png"))); // NOI18N
        btn_deli.setText("Giao hàng");
        btn_deli.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_deli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deliActionPerformed(evt);
            }
        });

        btn_bill.setBackground(new java.awt.Color(241, 85, 126));
        btn_bill.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_bill.setForeground(new java.awt.Color(255, 255, 255));
        btn_bill.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/Bill.png"))); // NOI18N
        btn_bill.setText("Hóa đơn");
        btn_bill.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_bill.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_billActionPerformed(evt);
            }
        });

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

        lbl_IDNV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_IDNV.setForeground(new java.awt.Color(255, 255, 255));
        lbl_IDNV.setText("IDNV");

        lbl_TenNV.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_TenNV.setForeground(new java.awt.Color(255, 255, 255));
        lbl_TenNV.setText("TenNV");

        lbl_Role.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_Role.setForeground(new java.awt.Color(255, 255, 255));
        lbl_Role.setText("ViTriCongViec");

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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblimg, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(38, 38, 38)
                .addComponent(btn_new)
                .addGap(32, 32, 32)
                .addComponent(btn_takeaw)
                .addGap(37, 37, 37)
                .addComponent(btn_deli)
                .addGap(31, 31, 31)
                .addComponent(btn_bill)
                .addGap(142, 142, 142)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbl_IDNV, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbl_TenNV, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                        .addComponent(lbl_Exit))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_Role)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbl_IDNV)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbl_TenNV)
                            .addComponent(jLabel5)
                            .addComponent(btn_new)
                            .addComponent(btn_takeaw)
                            .addComponent(btn_deli)
                            .addComponent(btn_bill)))
                    .addComponent(lbl_Exit))
                .addGap(2, 2, 2)
                .addComponent(lbl_Role)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblimg, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, 90));

        jPanel3.setBackground(new java.awt.Color(3, 172, 220));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Tổng tiền:");

        chk_discount.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        chk_discount.setForeground(new java.awt.Color(255, 255, 255));
        chk_discount.setText("Giảm giá (%)");
        chk_discount.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_discountItemStateChanged(evt);
            }
        });

        txt_Discount.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        txt_Discount.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_Discount.setEnabled(false);
        txt_Discount.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_DiscountKeyTyped(evt);
            }
        });

        btn_TaoHoaDon.setBackground(new java.awt.Color(241, 85, 126));
        btn_TaoHoaDon.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        btn_TaoHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btn_TaoHoaDon.setText("Tạo hóa đơn");
        btn_TaoHoaDon.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btn_TaoHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_TaoHoaDonActionPerformed(evt);
            }
        });

        lbl_Total.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        lbl_Total.setForeground(new java.awt.Color(255, 255, 255));
        lbl_Total.setText("0.00");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(843, 843, 843)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lbl_Total))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(chk_discount)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_Discount, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                .addComponent(btn_TaoHoaDon)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chk_discount)
                    .addComponent(txt_Discount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lbl_Total))
                .addGap(44, 44, 44))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_TaoHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 660, 1370, 90));

        tbl_Inf.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tbl_Inf.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "STT", "Tên SP", "Số lượng", "Size", "Giá bán", "Thành tiền", "Ảnh", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tbl_Inf.setRowHeight(40);
        tbl_Inf.setSelectionBackground(new java.awt.Color(57, 137, 111));
        jScrollPane1.setViewportView(tbl_Inf);
        if (tbl_Inf.getColumnModel().getColumnCount() > 0) {
            tbl_Inf.getColumnModel().getColumn(0).setMinWidth(60);
            tbl_Inf.getColumnModel().getColumn(0).setMaxWidth(60);
            tbl_Inf.getColumnModel().getColumn(1).setMinWidth(350);
            tbl_Inf.getColumnModel().getColumn(1).setMaxWidth(350);
            tbl_Inf.getColumnModel().getColumn(3).setMinWidth(40);
            tbl_Inf.getColumnModel().getColumn(3).setMaxWidth(40);
            tbl_Inf.getColumnModel().getColumn(6).setMinWidth(40);
            tbl_Inf.getColumnModel().getColumn(6).setMaxWidth(40);
        }

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, 1350, 480));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Khách hàng:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 140, -1, 30));

        txt_Search.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        getContentPane().add(txt_Search, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 100, 130, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Mã sản phẩm:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 100, -1, 30));

        txt_SDT.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_SDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_SDTActionPerformed(evt);
            }
        });
        getContentPane().add(txt_SDT, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 100, 150, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel4.setText("SĐT:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 100, -1, 30));

        txt_KH.setEditable(false);
        txt_KH.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_KH.setText("Khách vãng lai");
        txt_KH.setEnabled(false);
        getContentPane().add(txt_KH, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 140, 150, -1));

        btn_AddKH.setBackground(new java.awt.Color(3, 172, 220));
        btn_AddKH.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        btn_AddKH.setForeground(new java.awt.Color(255, 255, 255));
        btn_AddKH.setText("+");
        btn_AddKH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddKHActionPerformed(evt);
            }
        });
        getContentPane().add(btn_AddKH, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 140, -1, -1));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void lbl_ExitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lbl_ExitMouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_lbl_ExitMouseClicked

    private void btn_takeawActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_takeawActionPerformed
        // TODO add your handling code here:
        if (btn_takeaw.getForeground() == Color.GREEN)
        {
            btnClear();
            OderType = null;
        }
        else
        {
            btnClear();
            btn_takeaw.setForeground(Color.GREEN);
            OderType = "Trực Tiếp";
        }
    }//GEN-LAST:event_btn_takeawActionPerformed

    private void btn_deliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deliActionPerformed
        // TODO add your handling code here:
        if (btn_deli.getForeground() == Color.GREEN)
        {
            btnClear();
            OderType = null;
        }
        else
        {
            btnClear();
            btn_deli.setForeground(Color.GREEN);
            OderType = "Giao Hàng";
        }
    }//GEN-LAST:event_btn_deliActionPerformed

    private void chk_discountItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_discountItemStateChanged
        // TODO add your handling code here:
        txt_Discount.setEnabled(chk_discount.isSelected());
        if (!chk_discount.isSelected())
        {
            txt_Discount.setText("");
        }
        else
        {
            txt_Discount.setText("1");
            txt_Discount.requestFocus();
        }
    }//GEN-LAST:event_chk_discountItemStateChanged

    private void btn_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newActionPerformed
        // TODO add your handling code here:
        tblmodel.setRowCount(0);
        OderType = null;
        btnClear();
        lbl_Total.setText("0.00");
        chk_discount.setSelected(false);
        phone = "";
        txt_SDT.setText("");
        txt_KH.setText("Khách vãng lai");
        txt_Search.setText("");
    }//GEN-LAST:event_btn_newActionPerformed

    private void btn_TaoHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_TaoHoaDonActionPerformed
        // TODO add your handling code here:
        if (tblmodel.getRowCount() > 0)
        {
            if (OderType != null)
            {
                if (txt_KH.getText().equals("Khách vãng lai"))
                {
                    if (OderType.equals("Trực Tiếp"))
                    {
                        String id;
                        id = loadMaHD();
                        if (chk_discount.isSelected())
                        {
                            if (!txt_Discount.getText().isEmpty() && Integer.parseInt(txt_Discount.getText()) < 101)
                            {
                                int tong = Integer.parseInt(lbl_Total.getText().replace(".", ""));
                                double discount = (double)tong - ((double)tong * (Double.parseDouble(txt_Discount.getText()) / 100));
                                try
                                {
                                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                                    String sql = "INSERT INTO HOADON (IDHOADON, NGAYLAPHD, IDNHANVIEN, HINHTHUC, GIAMGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?, ?)";
                                    PreparedStatement ps = con.prepareStatement(sql);
                                    ps.setString(1, id);

                                    Date date = new Date();
                                    Timestamp timestamp = new Timestamp(date.getTime());
                                    ps.setTimestamp(2, timestamp);

                                    ps.setString(3, lbl_IDNV.getText());
                                    ps.setString(4, OderType);
                                    ps.setInt(5, Integer.parseInt(txt_Discount.getText()));
                                    ps.setInt(6, (int) discount);

                                    ps.executeUpdate();
                                    ps.close();
                                    JOptionPane.showMessageDialog(rootPane, "Thêm thành công hóa đơn", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                    for (int i = 0; i < tblmodel.getRowCount(); i++)
                                    {
                                        String idsp = "";
                                        String check = "SELECT IDSanPham FROM SANPHAM WHERE TENSANPHAM = N'" + tblmodel.getValueAt(i, 1) + "' AND SIZE = '" + tblmodel.getValueAt(i, 3) + "'";
                                        Statement st = con.createStatement();
                                        ResultSet rs = st.executeQuery(check);
                                        if (rs.next())
                                        {
                                            idsp = rs.getString("IDSANPHAM");
                                        }
                                        rs.close();
                                        st.close();

                                        String sql2 = "INSERT INTO CHITIETHOADON VALUES (?, ?, ?, ?)";
                                        PreparedStatement ps2 = con.prepareStatement(sql2);
                                        ps2.setString(1, id);
                                        ps2.setString(2, idsp);
                                        ps2.setInt(3, Integer.parseInt(tblmodel.getValueAt(i, 2).toString()));
                                        ps2.setInt(4, Integer.parseInt(tblmodel.getValueAt(i, 5).toString()));

                                        ps2.executeUpdate();
                                        ps2.close();

                                        String sql3 = "SELECT SOLUONGTON FROM SANPHAM WHERE IDSANPHAM = '" + idsp + "'";
                                        Statement st1 = con.createStatement();
                                        ResultSet rs1 = st1.executeQuery(sql3);
                                        int slton = 0;
                                        if (rs1.next())
                                        {
                                            slton = rs1.getInt("SOLUONGTON");
                                        }
                                        st1.close();
                                        rs1.close();

                                        String sql4;
                                        int sl = slton - Integer.parseInt(tblmodel.getValueAt(i, 2).toString());
                                        if (sl != 0)
                                        {
                                            sql4 = "UPDATE SANPHAM SET SoLuongTon = " + sl + " , TinhTrang = N'Còn Hàng'" +  " WHERE IDSANPHAM = '" + idsp + "'";
                                        }
                                        else
                                        {
                                            sql4 = "UPDATE SANPHAM SET SoLuongTon = " + sl + " , TinhTrang = N'Hết Hàng'" +  " WHERE IDSANPHAM = '" + idsp + "'";
                                        }

                                        Statement st2 = con.createStatement();
                                        st2.executeUpdate(sql4);

                                        st2.close();
                                    }
                                    con.close();
                                    CallBtnNew();
                                }
                                catch (SQLException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập giảm giá và trong khoảng (1-100)", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }
                        else
                        {
                            int tong = Integer.parseInt(lbl_Total.getText().replace(".", ""));
                            try
                            {
                                con = nhom13_shopquanaothethao.Connection.GetConnection();
                                String sql = "INSERT INTO HOADON (IDHOADON, NGAYLAPHD, IDNHANVIEN, HINHTHUC, GIAMGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?, ?)";
                                PreparedStatement ps = con.prepareStatement(sql);
                                ps.setString(1, id);

                                Date date = new Date();
                                Timestamp timestamp = new Timestamp(date.getTime());
                                ps.setTimestamp(2, timestamp);

                                ps.setString(3, lbl_IDNV.getText());
                                ps.setString(4, OderType);
                                ps.setInt(5, 0);
                                ps.setInt(6, tong);

                                ps.executeUpdate();
                                ps.close();
                                JOptionPane.showMessageDialog(rootPane, "Thêm thành công hóa đơn", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                for (int i = 0; i < tblmodel.getRowCount(); i++)
                                {
                                    String idsp = "";
                                    String check = "SELECT IDSanPham FROM SANPHAM WHERE TENSANPHAM = N'" + tblmodel.getValueAt(i, 1) + "' AND SIZE = '" + tblmodel.getValueAt(i, 3) + "'";
                                    Statement st = con.createStatement();
                                    ResultSet rs = st.executeQuery(check);
                                    if (rs.next())
                                    {
                                        idsp = rs.getString("IDSANPHAM");
                                    }
                                    rs.close();
                                    st.close();

                                    String sql2 = "INSERT INTO CHITIETHOADON VALUES (?, ?, ?, ?)";
                                    PreparedStatement ps2 = con.prepareStatement(sql2);
                                    ps2.setString(1, id);
                                    ps2.setString(2, idsp);
                                    ps2.setInt(3, Integer.parseInt(tblmodel.getValueAt(i, 2).toString()));
                                    ps2.setInt(4, Integer.parseInt(tblmodel.getValueAt(i, 5).toString()));

                                    ps2.executeUpdate();
                                    ps2.close();

                                    String sql3 = "SELECT SOLUONGTON FROM SANPHAM WHERE IDSANPHAM = '" + idsp + "'";
                                    Statement st1 = con.createStatement();
                                    ResultSet rs1 = st1.executeQuery(sql3);
                                    int slton = 0;
                                    if (rs1.next())
                                    {
                                        slton = rs1.getInt("SOLUONGTON");
                                    }
                                    st1.close();
                                    rs1.close();

                                    String sql4;
                                    int sl = slton - Integer.parseInt(tblmodel.getValueAt(i, 2).toString());
                                    if (sl != 0)
                                    {
                                        sql4 = "UPDATE SANPHAM SET SoLuongTon = " + sl + " , TinhTrang = N'Còn Hàng'" +  " WHERE IDSANPHAM = '" + idsp + "'";
                                    }
                                    else
                                    {
                                        sql4 = "UPDATE SANPHAM SET SoLuongTon = " + sl + " , TinhTrang = N'Hết Hàng'" +  " WHERE IDSANPHAM = '" + idsp + "'";
                                    }

                                    Statement st2 = con.createStatement();
                                    st2.executeUpdate(sql4);

                                    st2.close();
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
                        JOptionPane.showMessageDialog(rootPane, "Khách vãng lai không thể mua hình thức giao hàng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else
                {
                    if (OderType.equals("Trực Tiếp"))
                    {
                        String id, idkh;
                        id = loadMaHD();
                        idkh = getIDKH();
                        if (chk_discount.isSelected())
                        {
                            if (!txt_Discount.getText().isEmpty() && Integer.parseInt(txt_Discount.getText()) < 101)
                            {
                                int tong = Integer.parseInt(lbl_Total.getText().replace(".", ""));
                                double discount = (double)tong - ((double)tong * (Double.parseDouble(txt_Discount.getText()) / 100));
                                try
                                {
                                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                                    String sql = "INSERT INTO HOADON (IDHOADON, NGAYLAPHD, IDNHANVIEN, IDKH, HINHTHUC, GIAMGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                    PreparedStatement ps = con.prepareStatement(sql);
                                    ps.setString(1, id);

                                    Date date = new Date();
                                    Timestamp timestamp = new Timestamp(date.getTime());
                                    ps.setTimestamp(2, timestamp);

                                    ps.setString(3, lbl_IDNV.getText());
                                    ps.setString(4, idkh);
                                    ps.setString(5, OderType);
                                    ps.setInt(6, Integer.parseInt(txt_Discount.getText()));
                                    ps.setInt(7, (int) discount);

                                    ps.executeUpdate();
                                    ps.close();
                                    
                                    JOptionPane.showMessageDialog(rootPane, "Thêm thành công hóa đơn", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                    for (int i = 0; i < tblmodel.getRowCount(); i++)
                                    {
                                        String idsp = "";
                                        String check = "SELECT IDSanPham FROM SANPHAM WHERE TENSANPHAM = N'" + tblmodel.getValueAt(i, 1) + "' AND SIZE = '" + tblmodel.getValueAt(i, 3) + "'";
                                        Statement st = con.createStatement();
                                        ResultSet rs = st.executeQuery(check);
                                        if (rs.next())
                                        {
                                            idsp = rs.getString("IDSANPHAM");
                                        }
                                        rs.close();
                                        st.close();

                                        String sql2 = "INSERT INTO CHITIETHOADON VALUES (?, ?, ?, ?)";
                                        PreparedStatement ps2 = con.prepareStatement(sql2);
                                        ps2.setString(1, id);
                                        ps2.setString(2, idsp);
                                        ps2.setInt(3, Integer.parseInt(tblmodel.getValueAt(i, 2).toString()));
                                        ps2.setInt(4, Integer.parseInt(tblmodel.getValueAt(i, 5).toString()));

                                        ps2.executeUpdate();
                                        ps2.close();

                                        String sql3 = "SELECT SOLUONGTON FROM SANPHAM WHERE IDSANPHAM = '" + idsp + "'";
                                        Statement st1 = con.createStatement();
                                        ResultSet rs1 = st1.executeQuery(sql3);
                                        int slton = 0;
                                        if (rs1.next())
                                        {
                                            slton = rs1.getInt("SOLUONGTON");
                                        }
                                        st1.close();
                                        rs1.close();

                                        String sql4;
                                        int sl = slton - Integer.parseInt(tblmodel.getValueAt(i, 2).toString());
                                        if (sl != 0)
                                        {
                                            sql4 = "UPDATE SANPHAM SET SoLuongTon = " + sl + " , TinhTrang = N'Còn Hàng'" +  " WHERE IDSANPHAM = '" + idsp + "'";
                                        }
                                        else
                                        {
                                            sql4 = "UPDATE SANPHAM SET SoLuongTon = " + sl + " , TinhTrang = N'Hết Hàng'" +  " WHERE IDSANPHAM = '" + idsp + "'";
                                        }

                                        Statement st2 = con.createStatement();
                                        st2.executeUpdate(sql4);

                                        st2.close();
                                    }
                                    con.close();
                                    CallBtnNew();
                                }
                                catch (SQLException e)
                                {
                                    JOptionPane.showMessageDialog(rootPane, "Lỗi SQL: " + e.getMessage(), "Thông Báo", JOptionPane.ERROR_MESSAGE);
                                    e.printStackTrace();
                                }
                            }
                            else
                            {
                                JOptionPane.showMessageDialog(rootPane, "Vui lòng nhập giảm giá và trong khoảng (1-100)", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                return;
                            }
                        }
                        else
                        {
                            int tong = Integer.parseInt(lbl_Total.getText().replace(".", ""));
                            try
                            {
                                con = nhom13_shopquanaothethao.Connection.GetConnection();
                                String sql = "INSERT INTO HOADON (IDHOADON, NGAYLAPHD, IDNHANVIEN, IDKH, HINHTHUC, GIAMGIA, THANHTIEN) VALUES (?, ?, ?, ?, ?, ?, ?)";
                                PreparedStatement ps = con.prepareStatement(sql);
                                ps.setString(1, id);

                                Date date = new Date();
                                Timestamp timestamp = new Timestamp(date.getTime());
                                ps.setTimestamp(2, timestamp);

                                ps.setString(3, lbl_IDNV.getText());
                                ps.setString(4, idkh);
                                ps.setString(5, OderType);
                                ps.setInt(6, 0);
                                ps.setInt(7, tong);

                                ps.executeUpdate();
                                ps.close();
                                
                                JOptionPane.showMessageDialog(rootPane, "Thêm thành công hóa đơn", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                                for (int i = 0; i < tblmodel.getRowCount(); i++)
                                {
                                    String idsp = "";
                                    String check = "SELECT IDSanPham FROM SANPHAM WHERE TENSANPHAM = N'" + tblmodel.getValueAt(i, 1) + "' AND SIZE = '" + tblmodel.getValueAt(i, 3) + "'";
                                    Statement st = con.createStatement();
                                    ResultSet rs = st.executeQuery(check);
                                    if (rs.next())
                                    {
                                        idsp = rs.getString("IDSANPHAM");
                                    }
                                    rs.close();
                                    st.close();

                                    String sql2 = "INSERT INTO CHITIETHOADON VALUES (?, ?, ?, ?)";
                                    PreparedStatement ps2 = con.prepareStatement(sql2);
                                    ps2.setString(1, id);
                                    ps2.setString(2, idsp);
                                    ps2.setInt(3, Integer.parseInt(tblmodel.getValueAt(i, 2).toString()));
                                    ps2.setInt(4, Integer.parseInt(tblmodel.getValueAt(i, 5).toString()));

                                    ps2.executeUpdate();
                                    ps2.close();

                                    String sql3 = "SELECT SOLUONGTON FROM SANPHAM WHERE IDSANPHAM = '" + idsp + "'";
                                    Statement st1 = con.createStatement();
                                    ResultSet rs1 = st1.executeQuery(sql3);
                                    int slton = 0;
                                    if (rs1.next())
                                    {
                                        slton = rs1.getInt("SOLUONGTON");
                                    }
                                    st1.close();
                                    rs1.close();

                                    String sql4;
                                    int sl = slton - Integer.parseInt(tblmodel.getValueAt(i, 2).toString());
                                    if (sl != 0)
                                    {
                                        sql4 = "UPDATE SANPHAM SET SoLuongTon = " + sl + " , TinhTrang = N'Còn Hàng'" +  " WHERE IDSANPHAM = '" + idsp + "'";
                                    }
                                    else
                                    {
                                        sql4 = "UPDATE SANPHAM SET SoLuongTon = " + sl + " , TinhTrang = N'Hết Hàng'" +  " WHERE IDSANPHAM = '" + idsp + "'";
                                    }

                                    Statement st2 = con.createStatement();
                                    st2.executeUpdate(sql4);

                                    st2.close();
                                }
                                con.close();
                                CallBtnNew();
                            }
                            catch (SQLException e)
                            {
                                JOptionPane.showMessageDialog(rootPane, "Lỗi SQL: " + e.getMessage(), "Thông Báo", JOptionPane.ERROR_MESSAGE);
                                e.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        OderDelivery odd = new OderDelivery(this);
                        odd.txt_TenKH.setText(txt_KH.getText());
                        odd.txt_SDT.setText(phone);
                        odd.setVisible(true);
                    }
                }
            }
            else
            {
                JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn hình thức trực tiếp hoặc giao hàng", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "Vui lòng chọn sản phẩm", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
    }//GEN-LAST:event_btn_TaoHoaDonActionPerformed

    private void txt_DiscountKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DiscountKeyTyped
        // TODO add your handling code here:
        if (!Character.isDigit(evt.getKeyChar()))
        {
            evt.consume();
        }
        else
        {
            String curr = txt_Discount.getText() + evt.getKeyChar();
            int discount = Integer.parseInt(curr.isEmpty() ? "0" : curr);
            if (discount < 1 || discount > 100)
            {
                evt.consume();
            }
        }
    }//GEN-LAST:event_txt_DiscountKeyTyped

    private void btn_billActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_billActionPerformed
        // TODO add your handling code here:
        BillListForm blf = new BillListForm();
        blf.setVisible(true);
    }//GEN-LAST:event_btn_billActionPerformed

    private void btn_AddKHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddKHActionPerformed
        // TODO add your handling code here:
        AddKHForm addkh = new AddKHForm();
        addkh.txt_IDKH.setText(loadIDKH());
        addkh.setVisible(true);
    }//GEN-LAST:event_btn_AddKHActionPerformed

    private void txt_SDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_SDTActionPerformed
        // TODO add your handling code here:
        if (!txt_SDT.getText().isEmpty())
        {
            try
            {
                con = nhom13_shopquanaothethao.Connection.GetConnection();
                String sql = "SELECT * FROM KHACHHANG WHERE SDT = '" + txt_SDT.getText() + "'";
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(sql);
                if (rs.next())
                {
                    phone = rs.getString("SDT");
                    txt_SDT.setText("");
                    txt_KH.setText(rs.getString("TenKH"));
                }
                else
                {
                    phone = "";
                    txt_SDT.setText("");
                    txt_KH.setText("Khách vãng lai");
                    JOptionPane.showMessageDialog(rootPane, "Không tìm thấy khách hàng", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
        else
        {
            phone = "";
            txt_KH.setText("Khách vãng lai");
        }
    }//GEN-LAST:event_txt_SDTActionPerformed

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
            java.util.logging.Logger.getLogger(BanHangForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BanHangForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BanHangForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BanHangForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BanHangForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_AddKH;
    public javax.swing.JButton btn_TaoHoaDon;
    public javax.swing.JButton btn_bill;
    public javax.swing.JButton btn_deli;
    public javax.swing.JButton btn_new;
    public javax.swing.JButton btn_takeaw;
    public javax.swing.JCheckBox chk_discount;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JLabel jLabel4;
    public javax.swing.JLabel jLabel5;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel3;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lbl_Exit;
    public javax.swing.JLabel lbl_IDNV;
    public javax.swing.JLabel lbl_Role;
    public javax.swing.JLabel lbl_TenNV;
    public javax.swing.JLabel lbl_Total;
    public javax.swing.JLabel lblimg;
    public javax.swing.JTable tbl_Inf;
    public javax.swing.JTextField txt_Discount;
    public javax.swing.JTextField txt_KH;
    public javax.swing.JTextField txt_SDT;
    public javax.swing.JTextField txt_Search;
    // End of variables declaration//GEN-END:variables
}
