
package cn.upc.bbsinfor.extraction.view;

import cn.upc.bbsinfor.extraction.bean.UserSpeech;
import cn.upc.bbsinfor.extraction.util.IntegertoChina;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/**
 * 显示最后抽取到的用户发言信息
 * 
 * @author Belief
 */
public class ExtractInforView extends JFrame {
    private JFrame mFrame;

    private JTable mTable;

    public ExtractInforView() {
        super();
    }

    public ExtractInforView(List<UserSpeech> list) {
        mFrame = new ExtractInforView();
        mFrame.setTitle("显示抽取到的用户发言信息");
        // 更改应用程序的图标
        mFrame.setIconImage(mFrame.getToolkit().getImage(
                System.getProperty("user.dir") + "\\file\\icon.png"));
        mTable = new JTable(new MyTableModel(list));
        mTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        mTable.setFont(new Font("宋体", Font.PLAIN, 16));
        // 获得表头
        JTableHeader tableH = mTable.getTableHeader();
        // 设置表头的背景色
        tableH.setBackground(new Color(211, 211, 211));
        // 设置表头的文字颜色
        tableH.setForeground(new Color(255, 0, 0));
        tableH.setFont(new Font("宋体", Font.BOLD, 18));
        // 设置行高,列宽
        mTable.setRowHeight(30);
        mTable.setColumnModel(this.setColumnWidth(mTable, new int[] {
                300, 300, 600
        }));
        mTable.addMouseListener(new TableMouseAdapter());

        JScrollPane scrollPane = new JScrollPane(mTable); // 这样才能显示列名
        mTable.setFillsViewportHeight(true);
        scrollPane.setAutoscrolls(true);
        mFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        mFrame.setSize(1200, 1200);
        mFrame.pack();
        mFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mFrame.setLocationRelativeTo(null);
    }

    /**
     * 设置列宽
     * 
     * @param table
     * @param width
     * @return
     */
    public TableColumnModel setColumnWidth(JTable table, int[] width) {
        TableColumnModel columns = table.getColumnModel();
        for (int i = 0; i < width.length; i++) {
            TableColumn column = columns.getColumn(i);
            column.setPreferredWidth(width[i]);
        }
        return columns;
    }

    public void display() {
        mFrame.setVisible(true);
    }

    /**
     * 表格模型类
     * 
     * @author Belief
     */
    class MyTableModel extends AbstractTableModel {
        private String[] columnNames = new String[] {
                "发言(组)", "用户信息", "用户发言"
        };

        private List<UserSpeech> list = null;

        public MyTableModel(List<UserSpeech> list) {
            this.list = list;
        }

        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return list.size();
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        /**
         * 设置显示的数据
         */
        public Object getValueAt(int row, int col) {
            UserSpeech u = list.get(row);
            String v = "";
            switch (col) {
                case 0:
                    v = "发言" + IntegertoChina.convertInteger(row + 1);
                    break;
                case 1:
                    v = u.getUsername() + " " + u.getLevel();
                    break;
                case 2:
                    v = u.getSpeaking();
                    break;
            }
            return v;
        }

        public Class<?> getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    /**
     * 监听鼠标事件
     * 
     * @author Belief
     */
    class TableMouseAdapter extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            // 双击鼠标左键
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (e.getClickCount() == 2) {
                    String message = mTable.getModel()
                            .getValueAt(mTable.getSelectedRow(), mTable.getSelectedColumn())
                            .toString();
                    JDialog dialog = new JDialog();
                    dialog.setTitle("显示详细内容");
                    JTextArea mTextArea = new JTextArea();
                    mTextArea.setEditable(false);
                    mTextArea.setFont(new Font("宋体", Font.PLAIN, 16));
                    mTextArea.setLineWrap(true);
                    mTextArea.setWrapStyleWord(true);
                    mTextArea.setText("    " + message);
                    JScrollPane scrollPane = new JScrollPane(mTextArea);
                    dialog.getContentPane().add(scrollPane, BorderLayout.CENTER);
                    dialog.setSize(300, 150);
                    dialog.setLocationRelativeTo(null);
                    dialog.setVisible(true);
                }
            }
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
