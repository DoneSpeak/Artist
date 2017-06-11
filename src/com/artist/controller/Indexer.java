package com.artist.controller;

import com.artist.dao.ArticleDao;
import com.artist.dao.CategoryDao;
import com.artist.dao.PublisherDao;
import com.artist.model.Article;
import com.artist.model.Category;
import com.artist.model.Publisher;
import com.artist.model.enums.Region;
import com.artist.utils.Shower;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by DoneSpeak on 2017/6/5.
 */
@Controller
@RequestMapping("/")
public class Indexer {

    @Autowired
    private PublisherDao publisherDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private Searcher searcher;

    @RequestMapping("/")
    public String index(Model model){
        ArrayList<Publisher> publishers = null;
        ArrayList<Category> categories = null;
        ArrayList<Article> articles = null;
        try {
            publishers = publisherDao.getAll();
            categories = categoryDao.getAll();
            articles = articleDao.getTopKWithAbstractLength(20,140);
//            articles = new ArrayList<Article>();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error",e);
            return "error";
        }
        model.addAttribute("publishers",publishers);
        model.addAttribute("categories",categories);
        model.addAttribute("articles",articles);

//        System.out.println("显示文章列表");
//        try {
//            Shower.showArticles(articles);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        return "index_article";
    }

    @RequestMapping("/search")
    public String search(String query, int categoryId, int publisherId, int duration, int region, Model model){
        Region forRegion = null;
        int targetDuration = duration == -2 ? 30: duration;
        try {
//            域 必须完全匹配，否则按照默认设置处理 - 文章全文
            forRegion = Region.getEnum(region);
        } catch (Exception e) {
//            return "error";
            forRegion = Region.ARTICLECONTENT;
            region = Region.getIndex(Region.ARTICLECONTENT);
        }
        ArrayList<Publisher> publishers = null;
        ArrayList<Category> categories = null;
        ArrayList<Article> articles = null;
        try {
            publishers = publisherDao.getAll();
            categories = categoryDao.getAll();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error",e);
            return "error";
        }
        model.addAttribute("publishers",publishers);
        model.addAttribute("categories",categories);
//        -2 或者 -1 均表示全部类别，其中 -1 为用户主动选择，-2为默认
       if(categoryId != -2 && categoryId != -1 && !containsCategory(categories, categoryId)){
//           均不符合则使用默认选项 - 全部
//            model.addAttribute("articles",new ArrayList<Article>());
//            return "index_article";
           categoryId = -2;
        }

        if(publisherId != -2 && publisherId != -1 && !containsPublisher(publishers, publisherId)){
//            model.addAttribute("articles",new ArrayList<Article>());
//            return "index_article";
            publisherId = -2;
        }

        try {
//            默认检索前20条 - 空请求为符合条件的所有
            articles = searcher.search(query, categoryId, publisherId, targetDuration, forRegion);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error",e);
            return "error";
        }

        model.addAttribute("query",query);
        model.addAttribute("publisherId",publisherId);
        model.addAttribute("categoryId",categoryId);
        model.addAttribute("duration",duration);
        model.addAttribute("region",region);

        model.addAttribute("articles",articles);
//        【！】再jsp文件中，如果读取到一个null的属性，页面也有可能卡住
        if(Region.isAttachRegion(forRegion)){
//            检索附件，显示附件页面，否则为文章页面
            return "index_attach";
        }
        return "index_article";
    }

    private boolean containsPublisher(ArrayList<Publisher> publishers, int id){
        for(Publisher publisher: publishers){
            if(publisher.getId() == id){
                return true;
            }
        }
        return false;
    }

    private Publisher getPublisher(ArrayList<Publisher> publishers, int id){
        for(Publisher publisher: publishers){
            if(publisher.getId() == id){
                return publisher;
            }
        }
        return new Publisher(id,null);
    }

    private boolean containsCategory(ArrayList<Category> categories, int id){
        for(Category category: categories){
            if(category.getId() == id){
                return true;
            }
        }
        return false;
    }

    private Category getCategory(ArrayList<Category> categories, int id){
        for(Category category: categories){
            if(category.getId() == id){
                return category;
            }
        }
        return new Category(id,null);
    }

}
