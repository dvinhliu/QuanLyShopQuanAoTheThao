/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import raven_cell.TableActionCellEditor;
import raven_cell.TableActionCellRender;
import raven_cell.TableActionEvent;

public class SanPhamForm extends javax.swing.JFrame {

    Connection con;
    DefaultTableModel model;
    MainForm mnf;

    public SanPhamForm(MainForm mf) {
        initComponents();
        mnf = mf;
        model = (DefaultTableModel) tblSanPham.getModel();

        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                SanPhamModule sp = new SanPhamModule(SanPhamForm.this, false);
                sp.txt_MaSP.setEnabled(false);
                sp.txt_MaSP.setText(model.getValueAt(row, 0).toString());
                sp.txt_TenSP.setText(model.getValueAt(row, 1).toString());

                String txt_HangSP = model.getValueAt(row, 2).toString();
                sp.cbbHang.setSelectedItem(txt_HangSP);
                
                String txt_NCC = model.getValueAt(row, 3).toString();
                sp.cbbNCC.setSelectedItem(txt_NCC);

                String txt_SizeSP = model.getValueAt(row, 4).toString();
                if (txt_SizeSP.equals("S")) {
                    sp.rdo_S.setSelected(true);
                } else if (txt_SizeSP.equals("M")) {
                    sp.rdo_M.setSelected(true);
                } else if (txt_SizeSP.equals("L")) {
                    sp.rdo_L.setSelected(true);
                } else if (txt_SizeSP.equals("XL")) {
                    sp.rdo_XL.setSelected(true);
                }

                sp.txt_GiaNhapSP.setText(model.getValueAt(row, 5).toString());
//                sp.txt_GiaBanSP.setText(model.getValueAt(row, 5).toString());
                sp.txt_TenSP.requestFocus();

                try {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "SELECT HINHANH FROM SANPHAM WHERE IDSANPHAM = '" + sp.txt_MaSP.getText() + "'";
                    Statement st = con.createStatement();
                    ResultSet rs = st.executeQuery(sql);
                    String path = "";
                    if (rs.next()) {
                        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + rs.getString("HinhAnh")));
                        Image img = icon.getImage();
                        Image imgScale = img.getScaledInstance(sp.lblHinhSP.getWidth(), sp.lblHinhSP.getHeight(), Image.SCALE_SMOOTH);
                        ImageIcon scaleIcon = new ImageIcon(imgScale);
                        sp.lblHinhSP.setIcon(scaleIcon);
                    }
                    st.close();
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                sp.setVisible(true);
            }

            @Override
            public void onDelete(int row) {
                if (tblSanPham.isEditing()) {
                    tblSanPham.getCellEditor().stopCellEditing();
                }
                if (JOptionPane.showConfirmDialog(rootPane, "Bạn có muốn xóa sản phẩm này không?", "Thông Báo", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == JOptionPane.YES_OPTION) {
                    try {
                        con = nhom13_shopquanaothethao.Connection.GetConnection();
                        String sql = "DELETE FROM SANPHAM WHERE IDSANPHAM = '" + model.getValueAt(row, 0).toString() + "'";
                        Statement st = con.createStatement();
                        int x = st.executeUpdate(sql);
                        if (x >= 1) {
                            JOptionPane.showMessageDialog(rootPane, "Xóa Thành Công", "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
                            st.close();
                            con.close();
                            loadSanPham();
                        }
                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(rootPane, e.toString(), "Thông Báo", JOptionPane.INFORMATION_MESSAGE);

                    }
                }
            }
        };
        
        if(mnf.lbl_Role.getText().equals("Chức Vụ: Quản Lý Cửa Hàng")) {
            tblSanPham.getColumnModel().getColumn(10).setCellRenderer(new TableActionCellRender());
            tblSanPham.getColumnModel().getColumn(10).setCellEditor(new TableActionCellEditor(event));  
            btn_ThemSP.setVisible(true);
        } else {
            btn_ThemSP.setVisible(false);
        }

        enableEdt();
        loadSanPham();
        loadDataCBB();
        txt_TimKiemSP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timKiem();
            }
        });
    }

    private void enableEdt() {
        txt_MaSP.setEnabled(false);
        txt_TenSP.setEnabled(false);
        txt_TenHangSP.setEnabled(false);
        txt_SizeSP.setEnabled(false);
        txt_GiaNhapSP.setEnabled(false);
        txt_GiaBanSP.setEnabled(false);
        txt_SLTon.setEnabled(false);
        txt_TinhTrangSP.setEnabled(false);
        txt_TenNCC.setEnabled(false);
    }

    private void loadDataCBB() {
        cbb_TimKiem.removeAllItems();
        for (int i = 0; i < tblSanPham.getColumnCount() - 1; i++) {
            cbb_TimKiem.addItem(tblSanPham.getColumnName(i));
        }
    }

    public void loadSanPham() {
        tblSanPham.setRowHeight(40);
        tblSanPham.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        class ImageRenderer extends DefaultTableCellRenderer {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                if (value instanceof ImageIcon) {
                    label.setIcon((ImageIcon) value);
                } else {
                    label.setText((String) value);
                }
                return label;
            }
        }

        try {
            model.setRowCount(0);
            con = (Connection) nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT * FROM SANPHAM JOIN HANGSANPHAM ON SANPHAM.IDHANGSANPHAM = HANGSANPHAM.IDHANGSANPHAM JOIN NHACUNGCAP ON SANPHAM.IDNhaCungCap = NHACUNGCAP.IDNhaCungCap";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + rs.getString("HinhAnh")));
                Image imgs = icon.getImage();
                Image scaledImg = imgs.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImg);
                Object[] row = {
                    rs.getString("IDSanPham"),
                    rs.getString("TenSanPham"),
                    rs.getString("TenHang"),
                    rs.getString("TenNhaCungCap"),
                    rs.getString("Size"),
                    rs.getString("GiaNhap"),
                    rs.getString("GiaBan"),
                    rs.getString("SoLuongTon"),
                    rs.getString("TinhTrang"),
                    scaledIcon
                };
                model.addRow(row);
            }

            tblSanPham.setModel(model);

            tblSanPham.getColumnModel().getColumn(9).setCellRenderer(new ImageRenderer());

            st.close();
            rs.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void timKiem() {

        class ImageRenderer extends DefaultTableCellRenderer {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel();
                if (value instanceof ImageIcon) {
                    label.setIcon((ImageIcon) value);
                } else {
                    label.setText((String) value);
                }
                return label;
            }
        }

        String tk = txt_TimKiemSP.getText().trim();
        Object selectedItem = cbb_TimKiem.getSelectedItem();
        if (selectedItem != null) {
            if (tk.isEmpty()) {
                loadSanPham();
            } else {
                try {
                    con = nhom13_shopquanaothethao.Connection.GetConnection();
                    String sql = "SELECT * FROM SANPHAM JOIN HANGSANPHAM ON SANPHAM.IDHANGSANPHAM = HANGSANPHAM.IDHANGSANPHAM JOIN NHACUNGCAP ON SANPHAM.IDNhaCungCap = NHACUNGCAP.IDNhaCungCap WHERE ";
                    if (cbb_TimKiem.getSelectedItem().toString().equals("Mã sản phẩm")) {
                        sql += "IDSanPham LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Tên sản phẩm")) {
                        sql += "TenSanPham LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Tên hãng")) {
                        sql += "TenHang LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Size")) {
                        sql += "Size LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Giá nhập")) {
                        sql += "GiaNhap LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Giá bán")) {
                        sql += "GiaBan LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Số lượng tồn")) {
                        sql += "SoLuongTon LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Tình trạng")) {
                        sql += "TinhTrang LIKE ?";
                    } else if (cbb_TimKiem.getSelectedItem().toString().equals("Nhà cung cấp")) {
                        sql += "TenNhaCungCap LIKE ?";
                    }

                    PreparedStatement preparedStatement = con.prepareStatement(sql);
                    preparedStatement.setString(1, "%" + tk + "%"); // corrected the placeholder position
                    ResultSet rs = preparedStatement.executeQuery();

                    model.setRowCount(0);
                    while (rs.next()) {
                        ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + rs.getString("HinhAnh")));
                        Image imgs = icon.getImage();
                        Image scaledImg = imgs.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        ImageIcon scaledIcon = new ImageIcon(scaledImg);
                        Object[] row = {
                            rs.getString("IDSanPham"),
                            rs.getString("TenSanPham"),
                            rs.getString("TenHang"),
                            rs.getString("TenNhaCungCap"),
                            rs.getString("Size"),
                            rs.getString("GiaNhap"),
                            rs.getString("GiaBan"),
                            rs.getString("SoLuongTon"),
                            rs.getString("TinhTrang"),
                            scaledIcon
                        };
                        model.addRow(row);
                    }

                    tblSanPham.setModel(model);

                    tblSanPham.getColumnModel().getColumn(9).setCellRenderer(new ImageRenderer());
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

    private String loadMaSP(Connection con) {
        String IdAuto = "";
        try {
            String sql = "SELECT TOP 1 IDSANPHAM FROM SANPHAM ORDER BY IDSANPHAM DESC";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                int lastID = Integer.parseInt(rs.getString("IDSANPHAM").substring(3));
                int lastNumber = lastID + 1;
                IdAuto = "SP" + String.format("%03d", lastNumber);
            } else {
                IdAuto = "SP001";
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txt_MaSP = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_TenSP = new javax.swing.JTextField();
        txt_TenHangSP = new javax.swing.JTextField();
        txt_SizeSP = new javax.swing.JTextField();
        txt_GiaNhapSP = new javax.swing.JTextField();
        txt_SLTon = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_GiaBanSP = new javax.swing.JTextField();
        txt_TinhTrangSP = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        txt_TimKiemSP = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        cbb_TimKiem = new javax.swing.JComboBox<>();
        lbl_HinhAnh = new javax.swing.JLabel();
        btn_ThemSP = new javax.swing.JButton();
        btn_Reset = new javax.swing.JButton();
        txt_TenNCC = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin sản phẩm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel1.setText("Mã sản phẩm");

        txt_MaSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_MaSP.setActionCommand("<Not Set>");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel2.setText("Tên sản phẩm");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel3.setText("Tên hãng");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel5.setText("Giá nhập");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel6.setText("Size");

        txt_TenSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TenSP.setActionCommand("<Not Set>");

        txt_TenHangSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TenHangSP.setActionCommand("<Not Set>");

        txt_SizeSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_SizeSP.setActionCommand("<Not Set>");

        txt_GiaNhapSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_GiaNhapSP.setActionCommand("<Not Set>");

        txt_SLTon.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_SLTon.setActionCommand("<Not Set>");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel7.setText("Số lượng tồn");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel8.setText("Giá bán");

        txt_GiaBanSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_GiaBanSP.setActionCommand("<Not Set>");

        txt_TinhTrangSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TinhTrangSP.setActionCommand("<Not Set>");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel4.setText("Tình trạng");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 16))); // NOI18N

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel9.setText("Tìm kiếm theo");

        txt_TimKiemSP.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TimKiemSP.setActionCommand("<Not Set>");

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
                    .addComponent(txt_TimKiemSP))
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
                        .addComponent(txt_TimKiemSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        txt_TenNCC.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        txt_TenNCC.setActionCommand("<Not Set>");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel12.setText("Tên nhà cung cấp");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(txt_TenSP))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txt_TenHangSP, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12)
                            .addComponent(txt_TenNCC, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(txt_SizeSP, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_GiaNhapSP, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txt_GiaBanSP, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(0, 89, Short.MAX_VALUE))
                            .addComponent(txt_SLTon))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(txt_TinhTrangSP, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addComponent(lbl_HinhAnh, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_ThemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btn_Reset, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txt_TenSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(32, 32, 32))
                            .addComponent(txt_TenHangSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(txt_MaSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(32, 32, 32))
                    .addComponent(txt_TenNCC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(26, 26, 26)
                                    .addComponent(txt_TinhTrangSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel5))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txt_SizeSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txt_GiaNhapSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_GiaBanSP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_SLTon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btn_ThemSP)
                            .addComponent(btn_Reset)))
                    .addComponent(lbl_HinhAnh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel11.setText("Danh sách sản phẩm");

        tblSanPham.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã sản phẩm", "Tên sản phẩm", "Tên hãng", "Nhà cung cấp", "Size", "Giá nhập", "Giá bán", "Số lượng tồn", "Tình trạng", "Hình", ""
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham.setSelectionBackground(new java.awt.Color(57, 137, 111));
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPham);
        if (tblSanPham.getColumnModel().getColumnCount() > 0) {
            tblSanPham.getColumnModel().getColumn(1).setPreferredWidth(300);
            tblSanPham.getColumnModel().getColumn(8).setPreferredWidth(120);
            tblSanPham.getColumnModel().getColumn(9).setPreferredWidth(1);
            tblSanPham.getColumnModel().getColumn(10).setPreferredWidth(120);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
                .addGap(14, 14, 14))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_ThemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ThemSPActionPerformed
        SanPhamModule spm = new SanPhamModule(this, true);
        spm.txt_MaSP.setText(loadMaSP(nhom13_shopquanaothethao.Connection.GetConnection()));
        spm.txt_MaSP.setEnabled(false);
        spm.setVisible(true);
    }//GEN-LAST:event_btn_ThemSPActionPerformed

    private void btn_ResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ResetActionPerformed
        txt_TimKiemSP.setText("");
        cbb_TimKiem.setSelectedIndex(1);
    }//GEN-LAST:event_btn_ResetActionPerformed

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        int selectedRow = tblSanPham.getSelectedRow();
        if (selectedRow != -1) {

        }
        // Lấy thông tin từ dòng được chọn
        String maSP = tblSanPham.getValueAt(selectedRow, 0).toString();
        String tenSP = tblSanPham.getValueAt(selectedRow, 1).toString();
        String tenHang = tblSanPham.getValueAt(selectedRow, 2).toString();
        String tenNCC = tblSanPham.getValueAt(selectedRow, 3).toString();
        String size = tblSanPham.getValueAt(selectedRow, 4).toString();
        String giaNhap = tblSanPham.getValueAt(selectedRow, 5).toString();
        String giaBan = tblSanPham.getValueAt(selectedRow, 6).toString();
        String soLuongTon = tblSanPham.getValueAt(selectedRow, 7).toString();
        String tinhTrang = tblSanPham.getValueAt(selectedRow, 8).toString();

        try {
            con = (Connection) nhom13_shopquanaothethao.Connection.GetConnection();
            String sql = "SELECT HINHANH FROM SANPHAM WHERE IDSANPHAM = '" + maSP + "'";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            String path = "";
            if (rs.next()) {

                ImageIcon icon = new ImageIcon(getClass().getResource("/Images/" + rs.getString("HinhAnh")));
                Image img = icon.getImage();
                Image imgScale = img.getScaledInstance(lbl_HinhAnh.getWidth(), lbl_HinhAnh.getHeight(), Image.SCALE_SMOOTH);
                ImageIcon scaleIcon = new ImageIcon(imgScale);

                txt_MaSP.setText(maSP);
                txt_TenSP.setText(tenSP);
                txt_TenHangSP.setText(tenHang);
                txt_TenNCC.setText(tenNCC);
                txt_SizeSP.setText(size);
                txt_GiaNhapSP.setText(giaNhap);
                txt_GiaBanSP.setText(giaBan);
                txt_SLTon.setText(soLuongTon);
                txt_TinhTrangSP.setText(tinhTrang);
                lbl_HinhAnh.setIcon(scaleIcon);
            }
            st.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_tblSanPhamMouseClicked

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
            java.util.logging.Logger.getLogger(SanPhamForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SanPhamForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SanPhamForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SanPhamForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainForm mf = new MainForm();
                new SanPhamForm(mf).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Reset;
    private javax.swing.JButton btn_ThemSP;
    private javax.swing.JComboBox<String> cbb_TimKiem;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_HinhAnh;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txt_GiaBanSP;
    private javax.swing.JTextField txt_GiaNhapSP;
    private javax.swing.JTextField txt_MaSP;
    private javax.swing.JTextField txt_SLTon;
    private javax.swing.JTextField txt_SizeSP;
    private javax.swing.JTextField txt_TenHangSP;
    private javax.swing.JTextField txt_TenNCC;
    private javax.swing.JTextField txt_TenSP;
    private javax.swing.JTextField txt_TimKiemSP;
    private javax.swing.JTextField txt_TinhTrangSP;
    // End of variables declaration//GEN-END:variables
}
