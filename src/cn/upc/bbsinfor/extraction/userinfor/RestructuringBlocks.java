
package cn.upc.bbsinfor.extraction.userinfor;

import org.dom4j.Element;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过块重组抽取用户发言信息
 * 
 * @author Belief
 */
public class RestructuringBlocks {
    /**
     * 通过块重组抽取用户发言信息 , 并形成用户发言组
     * 
     * @param filterRedundantBlock
     * @return List<List<String>>表示最后的用户发言的组,其中每个List<String>表示一条发言信息
     */
    public List<List<String>> regroupingBlocks(FilterRedundantBlock filterRedundantBlock) {
        List<List<Element>> blocksList = filterRedundantBlock.getBlocksList();
        List<List<String>> speechList = new ArrayList<List<String>>();
        for (int i = 0, size = blocksList.size(); i < size; i++) {
            List<Element> elementList = blocksList.get(i);
            for (int j = 0, length = elementList.size(); j < length; j++) {
                Element e = elementList.get(j);
                List<String> list = null;
                if (i == 0) {
                    list = new ArrayList<String>();
                } else {
                    list = speechList.get(j);
                }
                list.add(e.attributeValue("content"));
                speechList.add(list);
            }
        }
        return speechList;
    }
}
