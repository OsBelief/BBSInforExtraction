
package cn.upc.bbsinfor.extraction.util;

import cn.upc.bbsinfor.extraction.bean.UserSpeech;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private static Pattern userNamePattern = null;

    private static Pattern userLevelPattern = null;

    private static Pattern userSpeechPattern = null;
    static {
        String userNameRegex = "<li class=\"d_name\" data-field=.*?target=\"_blank\">(.*?)</a>";
        String userLevelRegex = "<div class=\"d_badge_title.*?\">(.*?)</div><div class=\"d_badge_lv\">(.*?)</div>";
        String userSpeechRegex = "class=\"d_post_content j_d_post_content \">(.*?)</div>";
        userNamePattern = Pattern.compile(userNameRegex, Pattern.DOTALL);
        userLevelPattern = Pattern.compile(userLevelRegex, Pattern.DOTALL);
        userSpeechPattern = Pattern.compile(userSpeechRegex, Pattern.DOTALL);
    }

    public static String getHttpContent(String url, String charset) {
        StringBuffer content = null;
        try {
            URL _url = new URL(url);
            URLConnection connection = _url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
            content = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public static List<UserSpeech> ExtractUserSpeach(String content) {
        List<UserSpeech> list = new ArrayList<UserSpeech>();
        List<String> usernameList = new ArrayList<String>();
        List<String> levelList = new ArrayList<String>();
        List<String> speechList = new ArrayList<String>();
        Matcher nameMatcher = userNamePattern.matcher(content);
        Matcher levelMatcher = userLevelPattern.matcher(content);
        Matcher speechMatcher = userSpeechPattern.matcher(content);
        while (nameMatcher.find()) {
            String s = nameMatcher.group(1);
            if (s.equals("") == false) {
                usernameList.add(s);
            }
        }
        while (levelMatcher.find()) {
            String s = levelMatcher.group(1);
            if (s.equals("") == false) {
                levelList.add(levelMatcher.group(1) + " " + levelMatcher.group(2));
            }
        }
        while (speechMatcher.find()) {
            String s = speechMatcher.group(1).replaceAll("<.*?>", "").trim();
            speechList.add(s);
        }
        int size = usernameList.size();
        if (size == levelList.size() && size == speechList.size()) {
            for (int i = 0; i < size; i++) {
                UserSpeech u = new UserSpeech();
                u.setUsername(usernameList.get(i));
                u.setLevel(levelList.get(i));
                u.setSpeaking(speechList.get(i));
                list.add(u);
            }
        } else {
            logger.info("抽取用户发言信息错误！");
            new Exception("抽取用户发言信息错误！");
        }
        return list;
    }
}
