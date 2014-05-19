
package cn.upc.bbsinfor.extraction.userinfor;

import cn.upc.bbsinfor.extraction.localuser.LocalUserRegion;
import cn.upc.bbsinfor.extraction.util.FileIO;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 该类用来实现用户信息抽取过程中的附加块过滤
 * 
 * @author Belief
 */
public class FilterAdditionalBlocks {
    private static final Logger logger = LoggerFactory.getLogger(LocalUserRegion.class);

    private int rectTop = 0;

    private int rectHeight = 0;

    private int rectLeft = 0;

    private int rectWidth = 0;

    /**
     * 过滤附加块
     * 
     * @param filepath
     */
    public void filterAddtional(String filepath) throws DocumentException {
        Document doc = FileIO.readXML(filepath + "\\UserRegion.XML");
        Element rootElement = doc.getRootElement();
        Element userRegionElement = (Element)rootElement.elements().get(0);
        rectTop = Integer.parseInt(userRegionElement.attributeValue("ObjectRectTop"));
        rectHeight = Integer.parseInt(userRegionElement.attributeValue("ObjectRectHeight"));
        rectLeft = Integer.parseInt(userRegionElement.attributeValue("ObjectRectLeft"));
        rectWidth = Integer.parseInt(userRegionElement.attributeValue("ObjectRectWidth"));

        List<Element> listElements = userRegionElement.elements();
        for (Element e : listElements) {
            if (checkFeature_5(e) && checkFeature_3(e) == false) {
                // e.getParent().remove(e);
            }
        }
        userRegionElement.remove(listElements.get(0));
        FileIO.writeXML(doc, filepath + "\\Filter_Addtional.xml");
        logger.info("过滤冗余块结束！");
    }

    /**
     * 是否满足特征5
     * 
     * @param e
     * @return
     */
    private boolean checkFeature_5(Element e) {
        int rect_Top = Integer.parseInt(e.attributeValue("ObjectRectTop"));
        int rect_Height = Integer.parseInt(e.attributeValue("ObjectRectHeight"));
        if (rect_Top == rectTop || rect_Height == rectHeight) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否满足特征3
     * 
     * @param e
     * @return
     */
    private boolean checkFeature_3(Element e) {
        int rect_Left = Integer.parseInt(e.attributeValue("ObjectRectLeft"));
        int rect_Width = Integer.parseInt(e.attributeValue("ObjectRectWidth"));
        if (rect_Left == rectLeft && rect_Width == rectWidth) {
            return true;
        } else {
            return false;
        }
    }
}
