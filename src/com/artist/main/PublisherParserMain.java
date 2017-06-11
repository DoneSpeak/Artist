package com.artist.main;

import com.artist.model.Publisher;
import com.artist.utils.Shower;
import com.artist.utils.parser.PublisherParser;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;

/**
 * Created by DoneSpeak on 2017/6/5.
 * 【参考】
 * [Spring的自动装配方法](https://www.tianmaying.com/tutorial/spring-auto-wiring)
 */
public class PublisherParserMain {
    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext("config/applicationContext-main.xml");
        AutowireCapableBeanFactory wireFactory=context.getAutowireCapableBeanFactory();
        PublisherParser parser = wireFactory.getBean(PublisherParser.class);
        parser.parse2Database();
    }
}
