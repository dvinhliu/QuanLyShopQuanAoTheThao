/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package nhom13_shopquanaothethao;

import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;

/**
 *
 * @author nguye
 */
public class ImageRenderer {

    JLabel lbl = new JLabel();

    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus,
            int row, int column) {
        if (value != null) {
            ImageIcon icon = createImageIcon(value.toString()); // Tạo ImageIcon từ đường dẫn
            if (icon != null) {
                lbl.setIcon(icon);
                lbl.setHorizontalAlignment(JLabel.CENTER);
            } else {
                lbl.setText("Image not found");
                lbl.setHorizontalAlignment(JLabel.CENTER);
            }
        } else {
            lbl.setIcon(null);
            lbl.setText(null);
        }

        return lbl;
    }

    private ImageIcon createImageIcon(String imagePath) {
        ImageIcon icon = null;
        try {
            // Lấy đường dẫn thư mục hiện tại của ứng dụng
            String currentDir = System.getProperty("user.dir");
            // Kết hợp đường dẫn tương đối với thư mục hiện tại
            String absolutePath = currentDir + File.separator + imagePath;

            BufferedImage originalImg = ImageIO.read(new File(absolutePath));
            int imgWidth = originalImg.getWidth();
            int imgHeight = originalImg.getHeight();
            int scaledWidth = 50; // Chiều rộng mới
            int scaledHeight = 50 * imgHeight / imgWidth; // Tính toán chiều cao mới
            Image scaledImg = originalImg.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            icon = new ImageIcon(scaledImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icon;
    }
}
