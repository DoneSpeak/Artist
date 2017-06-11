package com.artist.utils.parser;

import com.artist.dao.ArticleDao;
import com.artist.dao.AttachDao;
import com.artist.dao.CategoryDao;
import com.artist.dao.PublisherDao;
import com.artist.model.Article;
import com.artist.model.Attach;
import com.artist.model.Category;
import com.artist.model.Publisher;
import com.artist.utils.Shower;
import org.apache.http.client.utils.URIBuilder;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by DoneSpeak on 2017/6/4.
 * 【参考】
 * [使用 HttpClient 和 HtmlParser 实现简易爬虫](https://www.ibm.com/developerworks/cn/opensource/os-cn-crawler/index.html)
 */
@Component
public class ArticleListCrawler {

    @Autowired
    private ArticleDao articleDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private PublisherDao publisherDao;
    @Autowired
    private AttachDao attachDao;

    private ArticleParser articleParser;
    private HtmlDownloader htmlDownloader;
    private String urlArea =  "table border=\"0\" cellpadding=3 style=\"border-collapse: collapse\" width=\"98%\"";
    private String charset = "gb2312";

    public ArticleListCrawler(){
        htmlDownloader = new HtmlDownloader();
        articleParser = new ArticleParser();
    }

    public ArrayList<Article> parseAndSave(String infotype) throws Exception {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.szu.edu.cn")
                .setPath("/board/")
                .setParameter("infotype", infotype)
                .setCharset(Charset.forName("gb2312"))
                .build();
        return parseAndSave(uri, charset);
    }

    public ArrayList<Article> parseAndSave(URI uri,String charset) throws Exception {
        String htmlContent = htmlDownloader.getContent(uri,charset,"post");
        Parser parser = new Parser(htmlContent);
        ArrayList<Article>  articles = new ArrayList<Article>();
        parser.setEncoding(charset);
//            包含主要文章内容的 table
        NodeFilter tableFilter = new NodeFilter() {
            public boolean accept(Node node) {
                if (node.getText().startsWith(urlArea)) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        NodeList areaNodes = parser.extractAllNodesThatMatch(tableFilter);

        if(areaNodes.size() == 0){
            return new ArrayList<Article>();
        }
        NodeList trNodeList = areaNodes.elementAt(0).getChildren().extractAllNodesThatMatch(new TagNameFilter("tr"));

        for(int i = 2; i < trNodeList.size(); i ++){
            Node trNode = trNodeList.elementAt(i);
            NodeList tdNodes = trNode.getChildren().extractAllNodesThatMatch(new TagNameFilter("td"));
            String categoryName = extractCategory(tdNodes.elementAt(1));
            String publisherName = extractPublisher(tdNodes.elementAt(2));
            String articleUrl = "http://" + extractArticleUrl(tdNodes.elementAt(3),uri.getHost() + uri.getPath());
            Shower.printf("url-" + (i-1),articleUrl);
            Publisher publisher = new Publisher(publisherName);
            publisher.setId(publisherDao.getIdByName(publisher.getName()));

            Category category = new Category(categoryName);
            category.setId(categoryDao.getIdByName(category.getName()));

//            Shower.printf("publisher",publisher);
//            Shower.printf("category",category);

            Article article = articleParser.parse(articleUrl,publisher,category);
//            必须先插入article再插入attach, 因为 attach 的 article_id 依赖于已有的 article
//            int publisherId = publisher.getId();
//            int categoryId = category.getId();

            try {
//                article 此时还没有id, 需要再插入以后才有id
                articleDao.addRawOne(article);
            }catch(DuplicateKeyException e){
//                重复抓取 导致 id 重复
                e.printStackTrace();
                continue;
            }
            ArrayList<Attach> attaches = article.getAttaches();
            for(Attach attach: attaches){
//                设置所属文章的 id
                attach.setArticle_id(article.getId());
            }
            addAttachments2Database(attaches);

            articles.add(article);
        }
        return articles;
    }

    private String extractCategory(Node node){
        Node linkNode = node.getFirstChild();
        return linkNode.toPlainTextString().trim();
    }

    private String extractPublisher(Node node){
        return node.toPlainTextString().trim();
    }

    private String extractArticleUrl(Node node, String curPath){
        Node linkNode = node.getLastChild();
        TagNode tag = new TagNode();
        tag.setText(linkNode.toHtml());
        curPath = curPath.endsWith("/") ? curPath : curPath + "/";
        return curPath + tag.getAttribute("href");
    }

    private void addAttachments2Database(ArrayList<Attach> attaches) throws Exception {
        for(Attach attach: attaches){
            attachDao.addOne(attach);
        }
    }

    public void parse2Database() throws Exception {
        ArrayList<Category> categories = categoryDao.getAll();
        for(Category cat: categories){
            Shower.printf("infotype",cat.getName());
            parseAndSave(cat.getName());
        }
    }

    public static void main(String[] args){
        ApplicationContext context = new ClassPathXmlApplicationContext("config/applicationContext-main.xml");
        AutowireCapableBeanFactory wireFactory=context.getAutowireCapableBeanFactory();
        ArticleListCrawler parser = wireFactory.getBean(ArticleListCrawler.class);
        try {
            parser.parseAndSave("教务");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("发生错误！停止！");
        }
    }
}
