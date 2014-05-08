
package cn.upc.bbsinfor.extraction.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 显示滚动的文字提示
 * 
 * @author Belief
 */
public class ScrolledTextFrame extends JFrame {

    private JLabel label;

    private String scrolledText = "正在建立视觉分块树VBT,请稍等!  ";

    public JFrame frame = null;

    public ScrolledTextFrame() {
        label = new JLabel(scrolledText);
        label.setFont(new Font("宋体", Font.BOLD, 24));
        label.setForeground(Color.red);
        Thread thread = new Thread(new TextChanger(label));
        thread.start();
        frame = new JFrame("提示");
        frame.add(label, BorderLayout.CENTER);
        // 禁用关闭按钮
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // 禁用最小化按钮
        frame.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                if (frame.getState() == 1) {
                    frame.setState(0);
                }
            }
        });
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    public void showMessageView() {
        frame.setVisible(true);
    }
    /**
     * 实现字体滚动的任务
     * @author Belief
     *
     */
    class TextChanger implements Runnable {
        private JLabel label;

        public TextChanger(JLabel label) {
            this.label = label;
        }
        @Override
        public void run() {
            try {
                while (true) {
                    String text = label.getText();
                    if (text.length() > 1) {
                        text = text.substring(1, text.length()) + text.charAt(0);
                        label.setText(text);
                        label.repaint();
                    }
                    Thread.sleep(300);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}
