
package cn.upc.bbsinfor.extraction.view.xmltree;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 * 树节点的显示样式
 * 
 * @author Belief
 */
public class XMLTreeCellRenderer extends DefaultTreeCellRenderer {

    // colors for tree items
    private final Color elementColor = new Color(0, 0, 128);

    private final Color textColor = new Color(0, 128, 0);

    // remove icons
    public XMLTreeCellRenderer() {
        // 设置不同节点的图标
        /*setOpenIcon(new ImageIcon());
        setClosedIcon(new ImageIcon());
        setLeafIcon(new ImageIcon());*/
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
        DOMAdapterNode adapterNode = (DOMAdapterNode)value;
        if (adapterNode.node.isRootElement()) {
            value = adapterNode.node.getName();
        } else if (adapterNode.node.elements().size() > 0) {
            value = adapterNode.node.getName();
        } else {
            value = adapterNode.node.getName();
            leaf = true;
        }

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        if (!selected) {
            if (adapterNode.node.getTextTrim().length() == 0) {
                setForeground(elementColor);
            } else {
                setForeground(textColor);
            }
        }

        return this;
    }
}
