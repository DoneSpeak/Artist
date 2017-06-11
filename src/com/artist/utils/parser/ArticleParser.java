package com.artist.utils.parser;

import com.artist.model.Article;
import com.artist.model.Attach;
import com.artist.model.Category;
import com.artist.model.Publisher;
import com.artist.utils.Shower;
import com.artist.utils.URLTokenizer;
import com.artist.utils.fileUtiles.TextExtracter;
import com.artist.utils.fileUtiles.TextExtracterFactory;
import org.apache.http.client.utils.URIBuilder;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.beans.StringBean;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by DoneSpeak on 2017/6/4.
 * 从网页中抽取文章内容
 * 【参考】
 * [Apache HttpClient 官方教程中文版](http://www.ctolib.com/topics-80581.html)
 * [使用 HttpClient 和 HtmlParser 实现简易爬虫](https://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/index.html)
 * [HtmlParser应用,使用Filter从爬取到的网页中获取需要的内容](http://www.javashuo.com/content/p-2389212.html)
 * [html解析类库htmlparser.net使用方法](http://www.voidcn.com/blog/yeah2000/article/p-535049.html)
 * [[Java] 使用htmlparser在爬虫时过滤网页](http://www.chongchonggou.com/g_420620804.html)
 * [java利用htmlparser获取html中想要的代码具体实现](http://www.jb51.net/article/46762.htm)
 */
public class ArticleParser {

    private HtmlDownloader htmlDownloader;
    private String charset = "gb2312";
    private String articleAreaStr = "table border=\"0\" cellspacing=\"0\" cellpadding=4 style=\"border-collapse: collapse\" width=\"85%\" height=\"100%";

    public ArticleParser(){
        htmlDownloader = new HtmlDownloader();
    }

//    下载 url 指向页面的文章
    public Article parse(String url, Publisher publisher, Category category) throws Exception {
        String htmlContent = null;
        Article article = new Article();
        URLTokenizer urlTokenizer = new URLTokenizer(url);
        htmlContent = htmlDownloader.getContent(url,charset,"get");
        Parser parser = new Parser(htmlContent);
        parser.setEncoding(charset);
//            包含主要文章内容的 table
        NodeFilter tableFilter = new NodeFilter() {
            public boolean accept(Node node) {
                if (node.getText().startsWith(articleAreaStr)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        NodeList articleNodeList = parser.extractAllNodesThatMatch(tableFilter);
        if(articleNodeList.size() < 0) {
            return null;
        }
        Node articleNode = articleNodeList.elementAt(0);
//                printf("articleNodeList",articleNodeList.toString());
//                提取所有的 tr 节点
        NodeList trNodeList = articleNode.getChildren().extractAllNodesThatMatch(new TagNameFilter("tr"));
        Node titleNode = trNodeList.elementAt(0);
        String title = extractTitle(titleNode);
        Node infoNode = trNodeList.elementAt(1);
        Date publishTime = extractPublishTime(infoNode);
//        String publisherName = extractPublisher(infoNode);
        Node contentNode = trNodeList.elementAt(2);
        String content = extractContent(contentNode);
        Node attachAndUpdateTimeNode = trNodeList.elementAt(3);
        Date updateTime = extractUpdateTime(attachAndUpdateTimeNode);
        if(updateTime == null){
//            利用发布时间作为更新时间，方便后面排序处理
            updateTime = publishTime;
        }

        int index = url.lastIndexOf("/");
        String curPath = "";
        if(index > 0){
            curPath = url.substring(0,index+1);
        }
        if(!curPath.endsWith("/")){
            curPath = curPath + "/";
        }

//        int articleId = Integer.parseInt(urlTokenizer.getParam("id"));

        ArrayList<Attach> attaches = extractAttachment(attachAndUpdateTimeNode,curPath,charset, -1);

//        Shower.printf("title",title);
//        Shower.printf("publishTime",publishTime.toString());
//        Shower.printf("publisher",publisher);
//        Shower.printf("updateTime",updateTime.toString());
//        Shower.printf("content",content);
//        Shower.printf("attach",attaches.toString());

//        article.setId(articleId);
        article.setUrl(url);
        article.setTitle(title);
        article.setPublished_time(publishTime);
        article.setUpdate_time(updateTime);
        article.setContent(content);
        article.setAttaches(attaches);
        article.setPublisher(publisher);
        article.setCategory(category);
//        这里的 attaches 均没有id，需要插入数据库后获得 id
        article.setAttaches(attaches);

        return article;
    }

    private String extractTitle(Node node){
        if(node == null){
            return "";
        }
        return removeChineseSpace(node.toPlainTextString().trim());
    }

    private String removeChineseSpace(String str){
        int i = 0;
        while(str.charAt(i) == '　'){
            i++;
        }
        return str.substring(i);
    }

    private String extractPublisher(Node node){
        if(node == null){
            return null;
        }
        String text = node.toPlainTextString().trim();
//        中文的空格
        return text.substring(0,text.indexOf("　"));
    }

    private Date extractPublishTime(Node node) throws ParseException {
        if(node == null){
            return null;
        }
        String dateRegix = "20[0-9]{2}-[0-9]{1,2}-[0-9]{1,2} [0-9]{1,2}:[0-9]{1,2}:[0-9]{1,2}";
        Pattern pattern = Pattern.compile(dateRegix);
        Matcher matcher = pattern.matcher(node.toPlainTextString().trim());
        if(!matcher.find()){
            return null;
        }
        String dateStr = matcher.group();
//        System.out.println(dateStr);
        return createDate(formatDateTimeString(dateStr));
    }

    private String extractContent(Node node){
        if(node == null){
            return "";
        }
        return node.toPlainTextString().trim();
    }

    private Date extractUpdateTime(Node node) throws ParseException {
        if(node == null){
            return null;
        }
        return extractPublishTime(node);
    }

    private String formatDateTimeString(String dateStr){
        String[] dateTime = dateStr.split(" +");
        String[] dateField = dateTime[0].split("-");
        String[] timeField = dateTime[1].split(":");
        String year = dateField[0];
        String month = dateField[1].length() < 2 ? "0" + dateField[1] : dateField[1];
        String day = dateField[2].length() < 2 ? "0" + dateField[2] : dateField[2];
        String hour = timeField[0].length() < 2 ? "0" + timeField[0] : timeField[0];
        String min = timeField[1].length() < 2 ? "0" + timeField[1] : timeField[1];
        String second = timeField[2].length() < 2 ? "0" + timeField[2] : timeField[2];

        return year + "-" + month + "-" + day + " " + hour + ":" + min + ":" + second;
    }

    private ArrayList<Attach> extractAttachment(Node node, String curPath, String charset, int articleId) throws ParserException {
        if(node == null){
            return new ArrayList<Attach>();
        }

        Parser parser = new Parser(node.toHtml());
        parser.setEncoding(charset);
        NodeList nodelist = parser.extractAllNodesThatMatch(new TagNameFilter("a"));
//        NodeList nodelist = parser.extractAllNodesThatMatch(new NodeClassFilter(LinkTag.class));
        ArrayList<Attach> attaches = new ArrayList<Attach>();

        curPath = curPath.endsWith("/") ? curPath : curPath + "/";
        for(int i = 0; i < nodelist.size(); i ++){
            Node attachNode = nodelist.elementAt(i);
            TagNode tagNode = new TagNode();//通过TagNode获得属性，只有将Node转换为TagNode才能获取某一个标签的属性
            tagNode.setText(attachNode.toHtml());
            String fileurl = curPath + tagNode.getAttribute("href");
            String filename = fileurl.substring(fileurl.lastIndexOf("/")+1);
            Attach attach = new Attach();
            attach.setFilename(filename);
            attach.setUrl(fileurl);
            attach.setArticle_id(articleId);
            attach.setContent(htmlDownloader.getFileContentFromUrl(fileurl));
            attaches.add(attach);
        }
        return attaches;
    }

    /**
     * 【参考】
     * [Java日期时间使用总结](http://www.jianshu.com/p/eca39ed7cbba)
     */
    private Date createDate(String dateStr) throws ParseException {
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return bartDateFormat.parse(dateStr);
    }

    public static void main(String[] args){
        ArticleParser parser = new ArticleParser();
        try {
            parser.parse("http://www.szu.edu.cn/board/view.asp?id=338153",new Publisher("publisher"),new Category("categery"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
