
package cn.upc.bbsinfor.extraction.util;

/**
 * 将整数转化成一,二 的形式
 * 
 * @author Belief
 */
public class IntegertoChina {
    private static char[] strVal = new char[] {
            '零', '一', '二', '三', '四', '五', '六', '七', '八', '九'
    };

    public static String convertInteger(int val) {
        StringBuffer res = new StringBuffer();
        int t_place = 0;
        int s_place = 0;
        int o_place = 0;
        if (val <= 999 && val > 99) {
            t_place = val / 100;
            s_place = val % 100 / 10;
            o_place = val % 10;
            if (s_place == 0 && o_place == 0) {
                res.append(strVal[t_place]).append("百");
            } else if (s_place == 0 && o_place != 0) {
                res.append(strVal[t_place]).append("百").append(strVal[s_place])
                        .append(strVal[o_place]);
            } else {
                res.append(strVal[t_place]).append("百").append(strVal[s_place]).append("十")
                        .append(strVal[o_place]);
            }
        } else if (val <= 99 && val > 9) {
            s_place = val / 10;
            o_place = val % 10;
            if (s_place == 1 && o_place == 0) {
                res.append("十");
            } else if (s_place == 1 && o_place != 0) {
                res.append("十").append(strVal[o_place]);
            } else if (s_place != 1 && o_place == 0) {
                res.append(strVal[s_place]).append("十");
            } else {
                res.append(strVal[s_place]).append("十").append(strVal[o_place]);
            }
        } else if (val <= 9 && val >= 0) {
            res.append(strVal[val]);
        } else {
            res.append(-1);
        }
        return res.toString();
    }
}
