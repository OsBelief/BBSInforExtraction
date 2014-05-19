
package cn.upc.bbsinfor.extraction.view;

import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cn.upc.bbsinfor.extraction.buildvbt.Vips;
import cn.upc.bbsinfor.extraction.localuser.LocalUserRegion;
import cn.upc.bbsinfor.extraction.userinfor.FilterAdditionalBlocks;
import cn.upc.bbsinfor.extraction.util.HttpUtil;
import cn.upc.bbsinfor.extraction.view.splash.SplashDialog;
import cn.upc.bbsinfor.extraction.view.xmltree.XMLViewer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * 用户操作面板
 * 
 * @author Belief
 */
public class MainViewFrame extends JPanel {
    private static Logger logger = LoggerFactory.getLogger(MainViewFrame.class);

    private static final String SOFTWARE_TITLE = "BBS用户发言信息抽取系统";

    private static final String _url = "http://tieba.baidu.com/p/3000414094";

    private JTextField bbsUrlTextField = null;

    private JButton bbsUrlLoadButton = null;

    private JButton buildVBTButton = null;

    private JButton locateUserInfor = null;

    private JButton extractionBBSInfor = null;

    private JWebBrowser mWebBrowser = null;

    private JPanel webBrowserPanel = null;

    private JFrame mFrame = null;

    private static MainViewFrame mainViewFrame = null;

    private String url = null;

    private Vips vips = null;

    private Executor vipsExecutor = null;

    private ScrolledTextFrame messageframe = null;

    public void initFrame(final SplashDialog splashDialog) {
        UIUtils.setPreferredLookAndFeel();
        NativeInterface.open();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mFrame = new JFrame(SOFTWARE_TITLE);
                mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainViewFrame = new MainViewFrame();
                mainViewFrame.initView(mFrame);
                // 更改应用程序的图标
                mFrame.setIconImage(mFrame.getToolkit().getImage(
                        System.getProperty("user.dir") + "\\file\\icon.png"));
                // 初始化全屏显示
                mFrame.setSize(800, 600);
                mFrame.setLocationRelativeTo(null);
                mainViewFrame.setActionEventListener();
                splashDialog.setVisible(false); // 关闭过渡页
                mFrame.setVisible(true);
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
        bbsUrlTextField = new JTextField(_url);
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
        topPanel.setBackground(new Color(245, 245, 245));
        // 右侧
        JPanel rightPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Image image = new ImageIcon(System.getProperty("user.dir")
                        + "\\file\\right_background.png").getImage();
                g.drawImage(image, 0, 0, this);
            };
        };
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
        container.setBackground(new Color(245, 245, 245));
    }

    /**
     * 添加事件监听器
     */
    private void setActionEventListener() {
        this.bbsUrlLoadButton.addActionListener(new LoadUrlHandler());
        this.buildVBTButton.addActionListener(new BuildVBTHandler());
        this.locateUserInfor.addActionListener(new LocateUserHandler());
        this.extractionBBSInfor.addActionListener(new ExtractInforHandler());
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
            if (url == null) {
                displayException("请先加载BBS主题网页,然后再建立视觉分块树VBT");
            } else {
                vipsExecutor = Executors.newSingleThreadExecutor(); // 初始化执行者框架
                // 显示可滚动提示信息的窗口
                messageframe = new ScrolledTextFrame("正在建立视觉分块树VBT,请稍等!  ");
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
                            logger.info("视觉分块树VBT已经构建完成！");
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
            if (vips == null) {
                displayException("表示视觉分块树的XML文件不存在,请先建立VBT视觉分块树");
            } else {
                // 显示可滚动提示信息的窗口
                messageframe = new ScrolledTextFrame("正在定位用户发言区,请稍等!  ");
                messageframe.showMessageView();

                Runnable localUserRegionTask = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            LocalUserRegion localUserRegion = new LocalUserRegion();
                            localUserRegion.localeRegion(System.getProperty("user.dir") + "\\"
                                    + vips.outputFolder);
                            closeWaitMessage();
                            // 显示XML文档对应的树形结构
                            XMLViewer xmlViewer = new XMLViewer();
                            xmlViewer.makeFrame(System.getProperty("user.dir") + "\\"
                                    + vips.outputFolder + "\\UserRegion.XML");
                        } catch (DocumentException e1) {
                            displayException("表示视觉分块树的XML文件不存在,请先建立VBT视觉分块树");
                            logger.info("表示视觉分块树的XML文件不存在,请先建立VBT视觉分块树");
                            e1.printStackTrace();
                        }
                    }
                };
                // 执行者框架执行定位用户发言区任务
                vipsExecutor.execute(localUserRegionTask);
            }
        }
    }

    /**
     * 抽取用户发言信息事件类
     * 
     * @author Belief
     */
    class ExtractInforHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (vips == null) {
                displayException("表示用户发言区的XML文件不存在,请先定位用户发言区");
            } else {
                // 显示可滚动提示信息的窗口
                messageframe = new ScrolledTextFrame("正在抽取用户发言信息,请稍等!  ");
                messageframe.showMessageView();

                Runnable inforExtractTask = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FilterAdditionalBlocks filterAdditional = new FilterAdditionalBlocks();
                            filterAdditional.filterAddtional(System.getProperty("user.dir") + "\\"
                                    + vips.outputFolder);
                            ExtractInforView view = new ExtractInforView(
                                    HttpUtil.ExtractUserSpeach(HttpUtil.getHttpContent(_url, "gbk")));
                            closeWaitMessage();
                            view.display();
                        } catch (DocumentException e1) {
                            displayException("表示用户发言区的XML文件不存在,请先定位用户发言区");
                            logger.info("表示用户发言区的XML文件不存在,请先定位用户发言区");
                            e1.printStackTrace();
                        }
                    }
                };
                // 执行者框架执行定位用户发言区任务
                vipsExecutor.execute(inforExtractTask);
            }
        }
    }

    /**
     * 显示异常信息对话框
     * 
     * @param mesg
     */
    public void displayException(String mesg) {
        JDialog dialog = new JDialog();
        dialog.setTitle("显示异常信息");
        JTextArea mTextArea = new JTextArea();
        mTextArea.setEditable(false);
        mTextArea.setFont(new Font("宋体", Font.PLAIN, 16));
        mTextArea.setLineWrap(true);
        mTextArea.setWrapStyleWord(true);
        mTextArea.setText("    " + mesg);
        JScrollPane scrollPane = new JScrollPane(mTextArea);
        dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
