package com.leibro.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by leibro on 2017/1/9.
 */
public class BlogAbstractor {
    public static String abstractContent(String content) {
        String html = MarkdownToHtml.markdownToHtml(content);
        Matcher matcher = Pattern.compile("<p>((\\w|\\W)*?)</p>").matcher(html);
        StringBuilder stringBuilder = new StringBuilder();
        while(matcher.find()) {
            stringBuilder.append(matcher.group(0));
        }
        String matchResult = Pattern.compile("</?[^>]+>").matcher(stringBuilder.toString()).replaceAll("");
        String abstractP;
        if(matchResult.length() > 300) {
            abstractP = matchResult.substring(0, 299) + "...";
        } else {
            abstractP = matchResult + "...";
        }
        return abstractP;
    }

    public static String abstractImgUrl(String content) {
        try {
            Matcher matcher = Pattern.compile("!\\[[^\\]]*]\\([^\\)]*\\.(png|jpeg|jpg|gif|bmp)\\)").matcher(content);
            matcher.find();
            String markdownImg = matcher.group(0);
            Matcher matcher1 = Pattern.compile("(?<=[(]).*(?=[)])").matcher(markdownImg);
            matcher1.find();
            return matcher1.group(0);
        } catch (Exception e) {
        }
        return null;

    }
}
