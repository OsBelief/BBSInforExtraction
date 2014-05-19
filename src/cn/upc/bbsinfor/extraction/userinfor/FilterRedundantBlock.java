
package cn.upc.bbsinfor.extraction.userinfor;

import org.dom4j.Attribute;
import org.dom4j.Element;
import java.util.List;

/**
 * 冗余块过滤
 * 
 * @author Belief
 */
public class FilterRedundantBlock {
    // 视觉簇的集合,其中每个元素表示一个视觉簇
    private List<List<Element>> blocksList = null;

    /**
     * 过滤冗余块
     * 
     * @param cVisualBlocks
     * @return
     */

    private void filterRedundantBlock(ClusteringVisualBlocks cVisualBlocks) {
        List<List<Element>> list = cVisualBlocks.clustersList;
        for (List<Element> listElements : list) {
            Element e0 = listElements.get(0);
            boolean flag = true;
            for (int i = 1, size = listElements.size(); i < size; i++) {
                if (judgeElementEquals(e0, listElements.get(i)) == false) {
                    flag = false;
                    break;
                }
            }
            if (flag == true) {
                list.remove(listElements);
            }
        }
        blocksList = list;
    }

    /**
     * 判断两个元素是否相等
     * 
     * @param e1
     * @param e2
     * @return
     */
    public boolean judgeElementEquals(Element e1, Element e2) {
        List<Attribute> list1 = e1.attributes();
        List<Attribute> list2 = e2.attributes();
        for (int i = 0, size = list1.size(); i < size; i++) {
            if (list1.get(i).getValue().equals(list2.get(i).getValue()) == false) {
                return false;
            }
        }
        return true;
    }

    public List<List<Element>> getBlocksList() {
        return blocksList;
    }
}
