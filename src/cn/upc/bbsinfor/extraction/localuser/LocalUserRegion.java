
package cn.upc.bbsinfor.extraction.localuser;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * 定位用户发言区
 * 
 * @author Belief
 */
public class LocalUserRegion {
    public static final Logger logger = LoggerFactory.getLogger(LocalUserRegion.class);

    private int pageRectWidth;

    private int pageRectHeight;

    private Document document;

    public void parseVIPSXML(String filename) {
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(filename + "\\VIPSResult.xml");
            Element rootElement = document.getRootElement();
            pageRectHeight = Integer.parseInt(rootElement.attributeValue("PageRectHeight"));
            pageRectWidth = Integer.parseInt(rootElement.attributeValue("PageRectWidth"));

            checkVipsBlock((Element)rootElement.elements().get(0)); // 观察文档得出VIPSPage只有一个子节点

            writeUserRegionXML(document, filename);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归检查是否是用户发言区
     * 
     * @param e
     */
    private void checkVipsBlock(Element e) {
        @SuppressWarnings("unchecked")
        List<Element> list = e.elements();
        // 定位用户发言区只针对三级节点
        for (Element el : list) {
            if (checkPosition(el) && checkBackgroundColor(el) && checkBlockArea(el) == false) {
                el.getParent().remove(el); // 删除不符合条件的节点
            }
        }
        removeTopAndBottom(e);
    }

    /**
     * 是否满足条件(一)
     * 
     * @param e
     * @return
     */
    private boolean checkPosition(Element e) {
        int objectRectLeft = Integer.parseInt(e.attributeValue("ObjectRectLeft"));
        int objectRectWidth = Integer.parseInt(e.attributeValue("ObjectRectWidth"));
        float d = Math.abs((float)pageRectWidth / 2
                - ((float)objectRectLeft + (float)objectRectWidth) / 2);
        float t = (float)pageRectWidth / 5;
        if (d < t) {
            return true;
        } else {
            return true;
        }
    }

    /**
     * 是否满足条件(二)
     * 
     * @param e
     * @return
     */
    private boolean checkBackgroundColor(Element e) {
        String ownerBgColor = e.attributeValue("BgColor");
        String parentBgColor = e.getParent().attributeValue("BgColor");
        if (ownerBgColor.equals(parentBgColor)) {
            return true;
        } else {
            return true;
        }
    }

    /**
     * 是否满足条件(三)
     * 
     * @param e
     * @return
     */
    private boolean checkBlockArea(Element e) {
        float t = 0.1f;
        float objectRectWidth = Float.parseFloat(e.attributeValue("ObjectRectWidth"));
        float objectRectHeight = Float.parseFloat(e.attributeValue("ObjectRectHeight"));
        float elementArea = objectRectHeight * objectRectWidth;
        float pageArea = (float)pageRectHeight * (float)objectRectWidth;
        if ((elementArea / pageArea) >= t) {
            return true;
        } else {
            return true;
        }
    }

    /**
     * 将用户发言区输出成XML的形式
     * 
     * @param document
     */
    private void writeUserRegionXML(Document document, String filename) {
        Writer fileWriter;
        try {
            fileWriter = new FileWriter(filename + "\\UserRegion.XML");
            // fileWriter = new FileWriter(System.getProperty("user.dir") +
            // "\\file\\user_egion.XML");
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setIndent(true); // 设置是否缩进
            format.setIndent("   "); // 以空格方式实现缩进
            format.setNewlines(true); // 设置是否换行
            XMLWriter xmlWriter = new XMLWriter(fileWriter, format);
            xmlWriter.write(document);
            xmlWriter.flush();
            xmlWriter.close();
            logger.info("用户发言区定位模块结束");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 去头去尾(论文中的方法行不通,只能耍点小聪明了)
     * 
     * @param e
     */
    private void removeTopAndBottom(Element el) {
        List<Element> list = el.elements();
        el.remove(list.get(0));
        el.remove(list.get(1));
        int size = list.size();
        el.remove(list.get(--size));
        el.remove(list.get(--size));
        el.remove(list.get(--size));
        el.remove(list.get(--size));
    }

    // public static void main(String[] args) {
    // LocalUserRegion region = new LocalUserRegion();
    // region.parseVIPSXML("E:\\eclipse4.2\\BBSInforExtraction\\07_05_2014_21_09_08_tieba_baidu_com");
    // }
}
