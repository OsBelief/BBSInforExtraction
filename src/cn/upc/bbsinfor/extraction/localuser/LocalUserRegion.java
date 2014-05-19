
package cn.upc.bbsinfor.extraction.localuser;

import cn.upc.bbsinfor.extraction.util.FileIO;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 定位用户发言区
 * 
 * @author Belief
 */
public class LocalUserRegion {
    final Logger logger = LoggerFactory.getLogger(LocalUserRegion.class);

    private int pageRectWidth;

    private int pageRectHeight;

    private Document document;

    /**
     * 定位用户发言区
     * 
     * @param filename
     */
    public void localeRegion(String filename) throws DocumentException {
        document = FileIO.readXML(filename + "\\VIPSResult.xml");
        if (document != null) {
            Element rootElement = document.getRootElement();
            pageRectHeight = Integer.parseInt(rootElement.attributeValue("PageRectHeight"));
            pageRectWidth = Integer.parseInt(rootElement.attributeValue("PageRectWidth"));

            checkVipsBlock((Element)rootElement.elements().get(0)); // 观察文档得出VIPSPage只有一个子节点

            FileIO.writeXML(document, filename + "\\UserRegion.XML");
            logger.info("用户发言区定位完成！");
        } else {
            new Exception("读VIPSResult.xml文件时异常！");
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
}
