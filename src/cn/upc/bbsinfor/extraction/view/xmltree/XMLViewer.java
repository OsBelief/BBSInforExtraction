
package cn.upc.bbsinfor.extraction.view.xmltree;

import org.dom4j.Attribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

/**
 * 显示XML文档对应的树形结构
 * 
 * @author Belief
 */
public class XMLViewer extends JFrame {
    public static final Logger logger = LoggerFactory.getLogger(XMLViewer.class);

    private final XMLTree xmlTree;

    private final String TITLE = "显示XML文档对应的树形结构";

    private File file;

    private JTree tree;

    private JTextArea textPane;

    private Exception exception;

    private final int windowHeight = 600;

    private final int leftWidth = 380;

    private final int rightWidth = 600;

    private final int windowWidth = leftWidth + rightWidth;

    private final Font treeFont = new Font("宋体", Font.BOLD, 16);

    private final Font textFont = new Font("宋体", Font.PLAIN, 18);

    public XMLViewer() {
        setTitle(TITLE);
        setPreferredSize(new Dimension(windowWidth, windowHeight));
        setFocusable(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(this.getToolkit().getImage(
                System.getProperty("user.dir") + "\\file\\icon.png"));
        xmlTree = new XMLTree();
    }

    public void makeFrame(String filename) {
        getContentPane().removeAll();
        try {
            file = new File(filename);
            xmlTree.parseFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tree = xmlTree.getTree();
        display();
        tree.addTreeSelectionListener(new NodeSelectionListener());
    }

    class NodeSelectionListener implements TreeSelectionListener {
        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DOMAdapterNode node = (DOMAdapterNode)tree.getLastSelectedPathComponent();
            List<Attribute> list = node.getElementAttr();
            StringBuffer sb = new StringBuffer();
            sb.append("*****显示元素的名称和属性*****\n");
            sb.append("******************************\n");
            sb.append("元素名称: ").append(node.getElementName()).append("\n\n");
            sb.append("属性                           值\n");
            for (Attribute attr : list) {
                sb.append(attr.getName()).append("              ").append(attr.getValue())
                        .append("\n");
            }
            textPane.setText(sb.toString());
        }
    }

    /**
     * Displays the tree.
     * 
     * @param tree JTree to display
     */
    public void display() {
        try {
            JScrollPane treeScrollPane = null;
            JScrollPane textScrollPane = null;

            // Build left-side view
            if (tree != null) {
                tree.setFont(treeFont);
                treeScrollPane = new JScrollPane(tree);
                treeScrollPane.setPreferredSize(new Dimension(leftWidth, windowHeight));
            } else {
                JEditorPane errorMessagePane = new JEditorPane();
                errorMessagePane.setEditable(false);
                errorMessagePane.setContentType("text/plain");
                errorMessagePane.setText("Error: unable to build tree from xml:\n"
                        + exception.toString());
                errorMessagePane.setCaretPosition(0);
                treeScrollPane = new JScrollPane(errorMessagePane);
            }

            // Build right-side view
            if (file != null) {
                StringBuilder sb = new StringBuilder();

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    reader.close();
                } catch (Exception e) {
                    System.err.println("exception when reading file for display");
                    e.printStackTrace();
                }

                textPane = new JTextArea();
                textPane.setEditable(false);
                textPane.setText(sb.toString());
                textPane.setCaretPosition(0);
                textPane.setFont(textFont);
                textScrollPane = new JScrollPane(textPane);
                textScrollPane.setPreferredSize(new Dimension(rightWidth, windowHeight));
            }

            // Build split-pane view
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeScrollPane,
                    textScrollPane);

            splitPane.setContinuousLayout(true);
            splitPane.setDividerLocation(leftWidth);
            splitPane.setPreferredSize(new Dimension(windowWidth + 10, windowHeight + 10));

            // Add GUI components
            setLayout(new BorderLayout());
            add("Center", splitPane);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        } catch (Exception e) {
            System.err.println("error when updating xml viewer");
            e.printStackTrace();
        }
    }
    /**
     * 只关闭当前窗口
     */
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.dispose();
        } else {
            super.processWindowEvent(e);
        }
    }
}
