
package cn.upc.bbsinfor.extraction.view;

import cn.upc.bbsinfor.extraction.buildvbt.Vips;
import cn.upc.bbsinfor.extraction.localuser.LocalUserRegion;
import cn.upc.bbsinfor.extraction.view.xmltree.XMLViewer;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
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

    private static MainViewFrame mainViewFrame = null;

    private String url = null;

    private Vips vips = null;

    private Executor vipsExecutor = null;

    private ScrolledTextFrame messageframe = null;

    public static void main(String[] args) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame mFrame = new JFrame(SOFTWARE_TITLE);
                mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainViewFrame = new MainViewFrame();
                mainViewFrame.initView(mFrame);
                mFrame.setBounds(5, 5, 800, 480);
                mFrame.setLocationByPlatform(true);
                mFrame.setVisible(true);

                mainViewFrame.setActionEventListener();
            }
        });
        NativeInterface.runEventPump();
    }

    public MainViewFrame() {
        super(new BorderLayout());
        // 初始化浏览器组件
        mWebBrowser = new JWebBrowser();
        webBrowserPanel = new JPanel(new BorderLayout());
        mWebBrowser.navigate(System.getProperty("user.dir") + "\\file\\welcome.html"); // 默认URL
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
        bbsUrlTextField = new JTextField("http://tieba.baidu.com/p/3000414094");
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

    /**
     * 添加事件监听器
     */
    private void setActionEventListener() {
        this.bbsUrlLoadButton.addActionListener(new LoadUrlHandler());
        this.buildVBTButton.addActionListener(new BuildVBTHandler());
        this.locateUserInfor.addActionListener(new LocateUserHandler());
    }

    /**
     * 加载Web页面显示事件类
     * 
     * @author Belief
     */
    class LoadUrlHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            url = bbsUrlTextField.getText();
            mWebBrowser.navigate(url);
        }

    }

    /**
     * 构建视觉分块树事件类
     * 
     * @author Belief
     */
    class BuildVBTHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            vipsExecutor = Executors.newSingleThreadExecutor(); // 初始化执行者框架
            // 显示可滚动提示信息的窗口
            messageframe = new ScrolledTextFrame();
            messageframe.showMessageView();

            Runnable vipsTask = new Runnable() {
                @Override
                public void run() {
                    try {
                        vips = new Vips();
                        // 获得图像
                        vips.enableGraphicsOutput(true);
                        // 创建新的文件夹
                        vips.enableOutputToFolder(true);
                        // 设置预定义关联度
                        vips.setPredefinedDoC(8);
                        // 开始页面分块
                        vips.startSegmentation(url);

                        closeWaitMessage();
                        showVBTFrame();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            // 执行者框架执行VIPS算法
            vipsExecutor.execute(vipsTask);
        }
    }

    /**
     * 显示VBT树的矩形形式
     */
    private void showVBTFrame() {
        // 有分隔条的形式
        VBTSeparatorView vbtSeparatorView = new VBTSeparatorView();
        vbtSeparatorView.setImagePath(System.getProperty("user.dir") + "\\" + vips.outputFolder
                + "\\iteration10.png");
        vbtSeparatorView.displayImage();
        // 没有分隔条的形式
        VBTShowView vbtShowView = new VBTShowView();
        vbtShowView.setImagePath(System.getProperty("user.dir") + "\\" + vips.outputFolder
                + "\\blocks10.png");
        vbtShowView.displayImage();
    }

    /**
     * 关闭显示对话框
     */
    private void closeWaitMessage() {
        messageframe.frame.setVisible(false);
    }

    /**
     * 定位用户发言区的事件类
     * 
     * @author Belief
     */
    class LocateUserHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LocalUserRegion localUserRegion = new LocalUserRegion();
            localUserRegion.parseVIPSXML(System.getProperty("user.dir") + "\\" + vips.outputFolder);
            XMLViewer xmlViewer = new XMLViewer();
            xmlViewer.makeFrame(System.getProperty("user.dir") + "\\" + vips.outputFolder + "\\UserRegion.XML");
        }
    }
}
