package com.artist.main;

import com.artist.utils.parser.ArticleListCrawler;
import com.artist.utils.parser.ArticleParser;
import com.artist.utils.parser.PublisherParser;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by DoneSpeak on 2017/6/5.
 */
public class ArticleParserMain {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/applicationContext-main.xml");
        AutowireCapableBeanFactory wireFactory=context.getAutowireCapableBeanFactory();
        ArticleListCrawler parser = wireFactory.getBean(ArticleListCrawler.class);
        try {
            parser.parse2Database();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
