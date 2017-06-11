package com.artist.utils.parser;

import com.artist.model.Article;
import com.artist.model.Category;
import com.artist.utils.indexer.IndexConstructor;
import com.artist.utils.indexer.IndexManager;
import com.artist.utils.indexer.IndexSearcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by DoneSpeak on 2017/6/10.
 */
//singleton 表示在spring容器中的单例，通过spring容器获得该bean时总是返回唯一的实例
//prototype表示每次获得bean都会生成一个新的对象
//request表示在一次http请求内有效（只适用于web应用）
//session表示在一个用户会话内有效（只适用于web应用）
//globalSession表示在全局会话内有效（只适用于web应用）
@Component
//@Scope("prototyp")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
//TODO 还不可以用，目前还有些spring相关的问题
public class CrawlerRunnable implements Runnable {
    @Autowired
    private ArticleListCrawler crawler;

    private IndexConstructor indexConstructor;

    private IndexSearcher indexSearcher;

    private IndexManager indexManager;

    private Category category;

    private boolean inited = false;

    public CrawlerRunnable(){}

    public void init(IndexSearcher indexSearcher, Category category){
        this.indexSearcher = indexSearcher;
        this.indexConstructor = indexSearcher.indexConstructor;
        this.indexManager = indexSearcher.indexManager;
        this.category = category;
        inited = true;
    }

    @Override
    public void run() {
        if(!inited){
            System.out.println("CrawlerRunnable need inited!");
            return;
        }
        ArrayList<Article> articles = null;
        try {
            articles = crawler.parseAndSave(category.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        synchronized (CrawlerRunnable.class) {
//          加锁，防止操作冲突
            indexConstructor.createPostingses(articles, true);
            indexManager.add2RegionMapPostings(indexConstructor.getRegionMapPostings());
            indexManager.add2DocLenghts(indexConstructor.getDocLenghts());
        }
    }
}
