
package cn.upc.bbsinfor.extraction.view.xmltree;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.List;
/**
 * 结点适配类
 * @author Belief
 *
 */
public class DOMAdapterNode {
    /** the Element encapsulated by this node */
    public Element node;

    /**
     * Creates a new instance of the JDOMAdapterNode class
     * 
     * @param Element node
     */
    public DOMAdapterNode(Element node) {
        this.node = node;
    }

    /**
     * Finds index of child in this node.
     * 
     * @param child The child to look for
     * @return index of child, -1 if not present (error)
     */
    public int index(DOMAdapterNode child) {

        int count = childCount();
        for (int i = 0; i < count; i++) {
            DOMAdapterNode n = this.child(i);
            if (child.node == n.node) {
                return i;
            }
        }
        return -1; // Should never get here.
    }

    /**
     * Returns an adapter node given a valid index found through the method:
     * public int index(JDOMAdapterNode child)
     * 
     * @param searchIndex find this by calling index(JDOMAdapterNode)
     * @return the desired child
     */
    public DOMAdapterNode child(int searchIndex) {
        Element child = (Element)node.elements().get(searchIndex);
        return new DOMAdapterNode(child);
    }

    /**
     * Return the number of children for this element/node
     * 
     * @return int number of children
     */
    public int childCount() {
        return node.elements().size();
    }
    /**
     * 获得节点的名称
     * @return
     */
    public String getElementName() {
        return node.getName();
    }
    /**
     * 获得节点的属性
     * @return
     */
    public List<Attribute> getElementAttr() {
        return node.attributes();
    }
}
