
package cn.upc.bbsinfor.extraction.userinfor;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过外观相似度聚类视觉块
 * 
 * @author Belief
 */
public class ClusteringVisualBlocks {
    // 视觉簇的集合,其中每个元素表示一个视觉簇
    public List<List<Element>> clustersList = new ArrayList<List<Element>>();
    // 阈值
    private float T_Cluster;
    /**
     * 计算两个视觉块的外观相似度
     * 
     * @param e1
     * @param e2
     * @return
     */
    public float simBlocks(Element e1, Element e2) {
        float sim = 0f;
        // 位置
        float e1_Left = Float.parseFloat(e1.attributeValue("ObjectLeft"));
        float e2_Left = Float.parseFloat(e2.attributeValue("ObjectLeft"));
        float pos = (float)(e1_Left / e2_Left * 0.3);
        // 图片
        float e1_img_h = Float.parseFloat(e1.attributeValue("ImgHeight"));
        float e1_img_w = Float.parseFloat(e1.attributeValue("ImgWidth"));
        float e2_img_h = Float.parseFloat(e1.attributeValue("ImgHeight"));
        float e2_img_w = Float.parseFloat(e2.attributeValue("ImgWidth"));
        float e1_h = Float.parseFloat(e1.attributeValue("ObjectHeight"));
        float e1_w = Float.parseFloat(e2.attributeValue("ObjectWidth"));
        float e2_h = Float.parseFloat(e1.attributeValue("ObjectHeight"));
        float e2_w = Float.parseFloat(e2.attributeValue("ObjectWidth"));
        float e1_img_s = e1_img_w * e1_img_h;
        float e2_img_s = e2_img_w * e2_img_h;
        float e1_s = e1_h * e1_w;
        float e2_s = e2_h * e2_w;
        float img = (Math.min(e1_img_s, e1_img_s)) / (Math.max(e1_img_s, e1_img_s))
                * ((e1_img_s + e2_img_s) / (e1_s + e2_s));
        // 纯文本
        float e1_text_h = Float.parseFloat(e1.attributeValue("TextHeight"));
        float e1_text_w = Float.parseFloat(e1.attributeValue("TextWidth"));
        float e1_text_l = Float.parseFloat(e1.attributeValue("TextLength"));
        float e2_text_h = Float.parseFloat(e1.attributeValue("TextHeight"));
        float e2_text_w = Float.parseFloat(e2.attributeValue("TextWidth"));
        float e2_text_l = Float.parseFloat(e1.attributeValue("TextLength"));
        float e1_text_s = e1_text_w * e1_text_h;
        float e2_text_s = e2_text_w * e2_text_h;
        float text = (Math.min(e1_text_l, e2_text_l)) / (Math.max(e1_text_l, e2_text_l))
                * ((e1_text_s + e2_text_s) / (e1_s + e2_s));
        // 链接文本
        float e1_link_h = Float.parseFloat(e1.attributeValue("LinkHeight"));
        float e1_link_w = Float.parseFloat(e1.attributeValue("LinkWidth"));
        float e1_link_l = Float.parseFloat(e1.attributeValue("LinkLength"));
        float e2_link_h = Float.parseFloat(e1.attributeValue("LinkHeight"));
        float e2_link_w = Float.parseFloat(e2.attributeValue("LinkWidth"));
        float e2_link_l = Float.parseFloat(e1.attributeValue("LinkLength"));
        float e1_link_s = e1_link_w * e1_link_h;
        float e2_link_s = e2_link_w * e2_link_h;
        float link = (Math.min(e1_link_l, e2_link_l)) / (Math.max(e1_link_l, e2_link_l))
                * ((e1_link_s + e2_link_s) / (e1_s + e2_s));
        sim = pos + img + text + link;
        return sim;
    }
    /**
     * 簇C和块b的相似性
     * @param list
     * @param e
     * @return
     */
    public float averageSimBlocks(List<Element> list, Element e) {
        float sim = 0f;
        for(Element ce : list) {
            sim += simBlocks(ce, e);
        }
        sim = sim / list.size();
        return sim;
    }
    /**
     * 聚类算法
     * 
     * @param list
     */
    public void clusteringBlocks(List<Element> list) {
        // 任取一视觉块形成初始簇
        List<Element> initialCluster = new ArrayList<Element>();
        initialCluster.add(list.get(0));
        clustersList.add(initialCluster);
        for (int i = 1, size = list.size(); i < size; i++) {
            // 找出相似度最大的簇
            float maxSim = 0;
            List<Element> CMaxList = null;
            Element b_e = list.get(i);
            for(List<Element> clist : clustersList) {
                float sim = averageSimBlocks(clist, b_e);
                if(sim > maxSim) {
                    maxSim = sim;
                    CMaxList = clist;
                }
            }
            // 判断是否大于阈值
            if(maxSim > T_Cluster) {
                CMaxList.add(b_e);
            } else {
                List<Element> newCluster = new ArrayList<Element>();
                clustersList.add(newCluster);
            }
        }
    }
}
