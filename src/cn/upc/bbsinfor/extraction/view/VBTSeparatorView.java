
package cn.upc.bbsinfor.extraction.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 视觉分块图展示类(有分隔条)
 * 
 * @author Belief
 */
public class VBTSeparatorView extends JFrame {
    private Container mContainer;

    private static final int h = 620;

    private static final int w = 210;

    private ImageViewPanel mPanel;
    private static final String TITLE = "视觉分块树(一)";

    public VBTSeparatorView() {
        mContainer = this.getContentPane();
        mPanel = new ImageViewPanel();
    }

    public void setImagePath(String s) {
        mPanel.setImagePath(s);
    }
    /**
     * 显示视觉分块树
     */
    public void displayImage() {
        mPanel.setPreferredSize(new Dimension(mPanel.getImgWidth(), mPanel.getImgHeight()));
        mContainer.add(mPanel, BorderLayout.CENTER);
        this.setBounds(10, 10, w, h);
        this.setTitle(TITLE);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);  // 禁用关闭功能
        this.setResizable(false);
        this.setVisible(true);
    }
    /**
     * 在panel上画出图片
     * @author Belief
     *
     */
    class ImageViewPanel extends JPanel {
        private Image image;

        private int imgWidth;

        private int imgHeight;

        public int getImgWidth() {
            return imgWidth;
        }

        public void setImgWidth(int imgWidth) {
            this.imgWidth = imgWidth;
        }

        public int getImgHeight() {
            return imgHeight;
        }

        public void setImgHeight(int imgHeight) {
            this.imgHeight = imgHeight;
        }

        public void setImagePath(String imgPath) {
            try {
                image = ImageIO.read(new FileInputStream(imgPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setImgWidth(image.getWidth(this));
            setImgHeight(image.getHeight(this));
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (null == image) {
                return;
            }
            Image newImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
            g.drawImage(newImage, 0, 0, w, h, this);
        }
    }
}
