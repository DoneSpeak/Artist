package com.artist.utils.parser;

import com.artist.dao.PublisherDao;
import com.artist.model.Publisher;
import com.artist.utils.Shower;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by DoneSpeak on 2017/6/2.
 * 爬取发布单位信息
 */
@Component
public class PublisherParser {

    @Autowired
    private PublisherDao publisherDao;

    String url = "http://www.szu.edu.cn/board/userlist.asp";
    String charset = "gb2312";

    public ArrayList<Publisher> parse() throws ParserException {
        ArrayList<Publisher> publishers = new ArrayList<Publisher>();
        Parser parser = new Parser(url);
        parser.setEncoding(charset);
        NodeList optionNodeList = parser.extractAllNodesThatMatch(new TagNameFilter("option"));
        for(int i = 0; i < optionNodeList.size(); i ++){
            Node optionNode = optionNodeList.elementAt(i);
            TagNode tag = new TagNode();
            tag.setText(optionNode.toHtml());
            String value = tag.getAttribute("value");
            int index = value.indexOf("#");
            if(index >= 0){
                publishers.add(new Publisher(value.substring(0,index)));
            }
        }

        return publishers;
    }

    public void parse2Database(){
        ArrayList<Publisher> publishers = null;
        try {
            publishers = parse();
            publisherDao.addSome(publishers);
//            Shower.showArray(publishers);
        } catch (ParserException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
