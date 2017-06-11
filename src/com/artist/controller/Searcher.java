package com.artist.controller;

import com.artist.dao.ArticleDao;
import com.artist.dao.CategoryDao;
import com.artist.dao.PublisherDao;
import com.artist.model.*;
import com.artist.model.enums.Region;
import com.artist.utils.ArrayUtil;
import com.artist.utils.Shower;
import com.artist.utils.indexer.IndexConstructor;
import com.artist.utils.indexer.IndexManager;
import com.artist.utils.indexer.IndexSearcher;
import com.artist.utils.indexer.IndexTool;
import com.artist.utils.parser.ArticleListCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Created by DoneSpeak on 2017/6/7.
 * 【参考】
 * [最全面的 Spring 学习笔记](http://www.banzg.com/archives/945.html)
 * [当spring 容器初始化完成后执行某个方法](http://blog.csdn.net/ye1992/article/details/52022450)
 */
@Component
//  在默认情况下，Spring应用上下文中所有bean都是作为以单例（singleton）的形式创建的。
//  也就是说，不管给定的一个bean被注入到其他bean多少次，每次所注入的都是同一个实例。
public class Searcher {
//    成员：倒排索引构建器、管理器

    private IndexSearcher indexSearcher;

    private IndexManager indexManager;

    private IndexConstructor indexConstructor;

    @Autowired
    private ArticleListCrawler crawler;

    @Autowired
    private PublisherDao publisherDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private ArticleDao articleDao;

    public boolean debug = true;

    public Searcher(){
        indexSearcher = new IndexSearcher();
        indexManager = indexSearcher.indexManager;
        indexConstructor = indexSearcher.indexConstructor;
        crawler = new ArticleListCrawler();
    }

    @PostConstruct
    public void initAll(){
//        [!] 加载
        System.out.println("初始化倒排索引");
        try {
            IndexTool.initSeg();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        //*/ 从数据库获取数据
        start(true,false);
        /*/ 用爬虫从网上获取数据
        start(false,true);
        //*/
        System.out.println("初始化结束");
    }

    public void start(boolean fromDataBase, boolean fromWeb){
//        利用已有的数据初始化 OR 初始抓取数据库数据初始化
        if(fromWeb) {
            ArrayList<Category> categories = null;
            try {
                categories = categoryDao.getAll();
//                new Thread().start();
//                 TODO 可定时更新
                for (Category cat : categories) {
                    //*/ 利用多线程抓取数据，分析数据
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Article> articles = null;
                            try {
                                articles = crawler.parseAndSave(cat.getName());
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                            postinging(articles);
                        }
                    }).start();

                    /*/
                    Shower.printf("infotype", cat.getName());
                    ArrayList<Article> articles = null;
                    try {
                        articles = crawler.parseAndSave(cat.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    indexConstructor.createPostingses(articles, true);
                    indexManager.add2RegionMapPostings(indexConstructor.getRegionMapPostings());
                    indexManager.add2DocLenghts(indexConstructor.getDocLenghts());
                    indexManager.add2DocNum(indexConstructor.getDocNum());
                    break;
                    //*/
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(fromDataBase){
            ArrayList<Category> categories = null;
            try {
                //*/ 利用多线程抓取数据，分析数据
                categories = categoryDao.getAll();
//                new Thread().start();
//                 TODO 可定时更新
                for (Category cat : categories) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ArrayList<Article> articles = null;
                            try {
                                articles = articleDao.addAllByCategoryId(cat.getId());
                            } catch (Exception e) {
                                e.printStackTrace();
                                return;
                            }
                            postinging(articles);
                        }
                    }).start();
                }

                /*/ 直接一次性抓取所有数据进行分析
                ArrayList<Article> articles = articleDao.getAll();
                indexConstructor.createPostingses(articles, true);
                indexManager.add2RegionMapPostings(indexConstructor.getRegionMapPostings());
                indexManager.add2DocLenghts(indexConstructor.getDocLenghts());
                indexManager.add2DocNum(indexConstructor.getDocNum());
                indexConstructor.clear();
                //*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("这是不想干活了哈！");
        }
//        定时抓取数据添加到倒排索引并更新词项值
    }

    public synchronized void postinging(ArrayList<Article> articles){
        //  【!important】 加锁，防止操作冲突
//        TODO 优化，将构造器独立到同步之外，使得处理效率更快，这需要对spring框架更加熟悉
        indexConstructor.createPostingses(articles, true);
        indexManager.add2RegionMapPostings(indexConstructor.getRegionMapPostings());
        indexManager.add2DocLenghts(indexConstructor.getDocLenghts());

        if(debug) {
            Shower.showRegionMapPostings(indexManager.getRegionMapPostings());
            Shower.showDocLengths(indexManager.getRegionDocLengths());
        }
        Shower.printf("docNum", indexManager.getDocNum(Region.ARTICLECONTENT));
    }

    public ArrayList<Article> search(String query, int category, int publisher, int duration, Region region) throws Exception {
        ArrayList<Article> articles = null;
        boolean isForArticleNotAttach = true;
        if(Region.isAttachRegion(region)){
            isForArticleNotAttach = false;
        }
        if(query == null || query.trim().length() == 0){
//          空请求，返回满足条件的所有
            articles = articleDao.getAllWithAbstractMatched(category,publisher,duration,isForArticleNotAttach);
            Shower.printf("articles",articles.size());
//            Shower.showArticles(articles);
            Shower.printf("search","完成");
            return articles;
        }
// 计算得到 有权重顺序的 docIds 数组
        int[] docIds = indexSearcher.search(region, query, 20);

        if(docIds.length <= 0){
            return new ArrayList<Article>();
        }
//        for(int id :docIds){
//            System.out.print(id + ",");
//        }
//        System.out.println();
        if(Region.isArticleRegion(region)){
            articles = articleDao.getAllWithAbstractInIds(category, publisher, duration, docIds);
            articles = ArrayUtil.orderArticleBy(articles,docIds);
        }else if(Region.isAttachRegion(region)){
            articles = articleDao.getAllWithAbstractForAttachIds(category, publisher, duration, docIds);
            articles = ArrayUtil.orderArticleForAttachBy(articles,docIds);
        }else{
            Shower.printf("search","完成");
            return new ArrayList<Article>();
        }
        Shower.printf("articles",articles.size());
        Shower.showArticles(articles);
        Shower.printf("search","完成");
        return articles;
    }
}
