
package cn.upc.bbsinfor.extraction.view;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.OutputStreamAppender;

import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * 显示滚动的文字提示
 * 
 * @author Belief
 */
public class ScrolledTextFrame extends JFrame {
    static final org.slf4j.Logger logger = LoggerFactory.getLogger(ScrolledTextFrame.class);

    private JLabel label;

    public JFrame frame = null;

    private JTextArea textArea;

    private JScrollPane scroll;

    public ScrolledTextFrame(String scrolledText) {
        label = new JLabel(scrolledText);
        label.setFont(new Font("宋体", Font.BOLD, 24));
        label.setForeground(Color.red);
        label.setSize(400, 150);
        Thread thread = new Thread(new TextChanger(label));
        thread.start();
        textArea = new JTextArea();
        textArea.setRows(2);
        textArea.setSize(400, 50);
        scroll = new JScrollPane();
        scroll.setViewportView(textArea);
        try {
            Thread textAreaLog = new TextAreaLogAppender();
            textAreaLog.start();
        } catch (IOException e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, e1, "绑定日志输出组件错误", JOptionPane.ERROR_MESSAGE);
        }
        frame = new JFrame("提示");
        // 更改应用程序的图标
        frame.setIconImage(frame.getToolkit().getImage(
                System.getProperty("user.dir") + "\\file\\icon.png"));
        frame.setSize(400, 200);
        frame.add(label, BorderLayout.CENTER);
        frame.add(scroll, BorderLayout.SOUTH);
        // 鼠标形状为等待，告知用户程序已经在很努力的加载了，此时不可操作
        setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
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
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    public void showMessageView() {
        frame.setVisible(true);
    }

    /**
     * 实现字体滚动的任务
     * 
     * @author Belief
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

    /**
     * 将日志重定向到GUI组件
     * 
     * @author Belief
     */
    class TextAreaLogAppender extends Thread {

        private PipedInputStream readInputStream;

        public TextAreaLogAppender() throws IOException {
            LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
            Logger root = lc.getLogger("root");
            // 获得根记录器的输出源
            Appender appender = root.getAppender("FILE");
            // 定义一个未连接的输入流管道
            readInputStream = new PipedInputStream();
            // 定义一个已连接的输出流管理，并连接到reader
            OutputStream writer = new PipedOutputStream(readInputStream);
            // 设置 appender 输出流
            ((OutputStreamAppender)appender).setOutputStream(writer);
        }

        @Override
        public void run() {
            // 不间断地扫描输入流
            Scanner scanner = new Scanner(readInputStream);
            // 将扫描到的字符流输出到指定的JTextArea组件
            while (scanner.hasNextLine()) {
                try {
                    String line = scanner.nextLine();
                    textArea.append(line);
                    textArea.append("\n");
                    line = null;
                    // 使垂直滚动条自动向下滚动
                    scroll.getVerticalScrollBar().setValue(
                            scroll.getVerticalScrollBar().getMaximum());
                } catch (Exception ex) {
                    // 异常不做处理
                    ex.printStackTrace();
                }
            }
            scanner.close();
        }
    }
}
