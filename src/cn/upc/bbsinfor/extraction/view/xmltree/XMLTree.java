
package cn.upc.bbsinfor.extraction.view.xmltree;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import java.io.File;

import javax.swing.JTree;
/**
 * XML文档对应的Tree
 * @author Belief
 *
 */
public class XMLTree {
    // keep handles on the documents and readers
    private static Document document;

    private static SAXReader saxReader;

    // tree to be displayed
    private static JTree tree;

    public XMLTree() {
        saxReader = new SAXReader();
    }

    public JTree getTree() {
        return tree;
    }

    /**
     * 读取显示在树形结构中的XML文档
     * 
     * @param xmlFile Path to an XML file.
     */
    public void parseFile(File xmlFile) throws Exception {
        try {
            document = saxReader.read(xmlFile);

            // 将文档对象转化成Jtree
            DOMToTreeModelAdapter model = new DOMToTreeModelAdapter(document);
            tree = new JTree(model);
            tree.setCellRenderer(new XMLTreeCellRenderer());

        } catch (Exception e) {
            tree = null;
            throw e;
        }
    }
}
