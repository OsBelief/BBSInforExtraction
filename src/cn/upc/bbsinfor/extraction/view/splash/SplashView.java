
package cn.upc.bbsinfor.extraction.view.splash;

import cn.upc.bbsinfor.extraction.view.MainViewFrame;

import javax.swing.UIManager;
/**
 * 显示过渡页
 * @author Belief
 *
 */
public class SplashView {
    public static void main(String args[]) {
        try {
            // 设置Java Swing外观风格
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        // 初始化闪屏Dialog时指定闪屏图片
        final SplashDialog splashDialog = new SplashDialog(
                System.getProperty("user.dir") + "\\file\\splash.png");
        // 启动一个线程来加载数据
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < 10; i++) {
                        splashDialog.updateProcess("  正在加载数据 . . .", i * 9);
                        Thread.sleep(300);
                    }
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                MainViewFrame mainViewFrame = new MainViewFrame();
                splashDialog.updateProcess("  正在启动应用程序. . .", 100);
                // 数据加载完成，显示主窗体
                mainViewFrame.initFrame(splashDialog);
                // 释放资源
                splashDialog.dispose();
            }
        }.start();
        // 显示闪屏Dialog
        splashDialog.setVisible(true);
    }
}
