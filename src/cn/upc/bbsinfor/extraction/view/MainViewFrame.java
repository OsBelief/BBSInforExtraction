
package cn.upc.bbsinfor.extraction.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * 用户操作面板
 * 
 * @author Belief
 */
public class MainViewFrame extends JPanel {
    private static final String SOFTWARE_TITLE = "BBS用户发言信息抽取系统";

    private JTextField bbsUrlTextField = null;

    private JButton bbsUrlLoadButton = null;

    private JButton buildVBTButton = null;

    private JButton locateUserInfor = null;

    private JButton extractionBBSInfor = null;

    private JWebBrowser mWebBrowser = null;

    private JPanel webBrowserPanel = null;

    public static void main(String[] args) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame mFrame = new JFrame(SOFTWARE_TITLE);
                mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                MainViewFrame mainViewFrame = new MainViewFrame();
                mainViewFrame.initView(mFrame);
                mFrame.setBounds(5, 5, 1200, 680);
//                mFrame.setLocationByPlatform(true);
                mFrame.setVisible(true);
                System.out.println(mainViewFrame.bbsUrlTextField.getWidth() + " : "
                        + mainViewFrame.bbsUrlTextField.getHeight());
                System.out.println(mainViewFrame.extractionBBSInfor.getWidth() + " : "
                        + mainViewFrame.extractionBBSInfor.getHeight());
            }
        });
        NativeInterface.runEventPump();
    }

    public MainViewFrame() {
        super(new BorderLayout());
        // 初始化浏览器组件
        mWebBrowser = new JWebBrowser(null);
        webBrowserPanel = new JPanel(new BorderLayout());
        mWebBrowser.navigate("http://www.google.com"); // 默认URL
        mWebBrowser.setButtonBarVisible(false);
        mWebBrowser.setMenuBarVisible(false);
        mWebBrowser.setBarsVisible(false);
        mWebBrowser.setStatusBarVisible(false);
        webBrowserPanel.add(mWebBrowser, BorderLayout.CENTER);
        add(webBrowserPanel, BorderLayout.CENTER);
    }

    /**
     * 初始化布局及视图控件
     * 
     * @param jFrame
     */
    private void initView(JFrame jFrame) {
        JLabel bbsUrlLabel = new JLabel("BBS主题网页URL");
        bbsUrlLabel.setFont(new Font("宋体", Font.PLAIN, 18));
        bbsUrlTextField = new JTextField("http://");
        bbsUrlTextField.setFont(new Font("宋体", Font.PLAIN, 18));
        bbsUrlLoadButton = new JButton("加载");
        bbsUrlLoadButton.setFont(new Font("宋体", Font.PLAIN, 18));

        extractionBBSInfor = new JButton("抽取用户发言信息");
        extractionBBSInfor.setFont(new Font("宋体", Font.PLAIN, 18));
        extractionBBSInfor.setPreferredSize(new Dimension(180, 30));
        buildVBTButton = new JButton("建立视觉分块树");
        buildVBTButton.setFont(new Font("宋体", Font.PLAIN, 18));
        buildVBTButton.setPreferredSize(new Dimension(180, 30));
        locateUserInfor = new JButton("定位用户发言区");
        locateUserInfor.setFont(new Font("宋体", Font.PLAIN, 18));
        locateUserInfor.setPreferredSize(new Dimension(180, 30));
        // 顶部
        JPanel topPanel = new JPanel();
        BoxLayout topLayout = new BoxLayout(topPanel, BoxLayout.X_AXIS);
        topPanel.setLayout(topLayout);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(bbsUrlLabel);
        topPanel.add(Box.createHorizontalStrut(10));
        topPanel.add(bbsUrlTextField);
        topPanel.add(Box.createHorizontalStrut(50));
        topPanel.add(bbsUrlLoadButton);
        topPanel.add(Box.createHorizontalStrut(73));
        // 右侧
        JPanel rightPanel = new JPanel();
        Box rightBox = Box.createVerticalBox();
        rightBox.add(Box.createVerticalStrut(80));
        rightBox.add(buildVBTButton);
        rightBox.add(Box.createVerticalStrut(25));
        rightBox.add(locateUserInfor);
        rightBox.add(Box.createVerticalStrut(25));
        rightBox.add(extractionBBSInfor);
        rightBox.add(Box.createVerticalStrut(25));
        rightPanel.add(rightBox);

        jFrame.setLayout(new BorderLayout(3, 3));
        Container container = jFrame.getContentPane();
        container.add(this, BorderLayout.CENTER);
        container.add(topPanel, BorderLayout.NORTH);
        container.add(rightPanel, BorderLayout.EAST);
    }
}
