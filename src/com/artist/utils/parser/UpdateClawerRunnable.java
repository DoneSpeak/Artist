package com.artist.utils.parser;

import com.artist.model.Category;
import com.artist.utils.Shower;
import com.artist.utils.indexer.IndexSearcher;

import java.util.ArrayList;

import static java.lang.Thread.sleep;

/**
 * Created by DoneSpeak on 2017/6/10.
 */
//@Component
//    TODO 定时抓取网站最新数据 - 重点： 高效判断最新数据
public class UpdateClawerRunnable implements Runnable {

    private IndexSearcher indexSearcher;

    private ArrayList<Category> categories;

    private boolean continueous = false;

    public UpdateClawerRunnable(){
        categories = new ArrayList<Category>();
    }

    public void init(IndexSearcher indexSearcher, ArrayList<Category> categories, boolean continueous){
        this.indexSearcher = indexSearcher;
        this.categories = categories;
    }

    @Override
    public void run() {
        for (Category cat : categories) {
            Shower.printf("infotype", cat.getName());
            CrawlerRunnable crawlerRunnable = new CrawlerRunnable();
            crawlerRunnable.init(indexSearcher,cat);
            new Thread(crawlerRunnable).start();
        }
        if(continueous){
            try {
//            每小时运行一次
                sleep(1000 * 60 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
