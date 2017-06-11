package com.artist.utils.parser;

import org.htmlparser.beans.StringBean;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DoneSpeak on 2017/6/4.
 */
public class HtmlHelper {

    public String delHTMLTag(String url){
        StringBean sb = new StringBean();
        sb.setLinks(false);//设置结果中去点链接
        sb.setURL(url);//设置你所需要滤掉网页标签的页面 url
        String cleanStr = sb.getStrings();

        System.out.println(cleanStr);//打印结果

        return cleanStr;
    }

    /**
     * 【参考】
     * [JAVA:清除HTML标签](https://gist.github.com/binjoo/5655961)
      */
    public static String delHTMLTagInStr(String htmlStr){
        String regEx_script="<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义script的正则表达式
        String regEx_style="<style[^>]*?>[\\s\\S]*?<\\/style>"; //定义style的正则表达式
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_script=Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        htmlStr=m_script.replaceAll(""); //过滤script标签

        Pattern p_style=Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
        Matcher m_style=p_style.matcher(htmlStr);
        htmlStr=m_style.replaceAll(""); //过滤style标签

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        htmlStr=m_html.replaceAll(""); //过滤html标签

        return htmlStr.trim(); //返回文本字符串
    }
}
