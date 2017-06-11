package com.artist.utils;

import com.artist.model.Article;
import com.artist.model.Attach;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DoneSpeak on 2017/6/8.
 */
public class ArrayUtil {
    public static ArrayList<Article> orderArticleBy(ArrayList<Article> articles, int[] orders){
        ArrayList<Article> newList = new ArrayList<Article>();
        HashMap<Integer,Article> idMapArticle = new HashMap<Integer, Article>();
        for(Article article: articles){
            idMapArticle.put(article.getId(),article);
        }
        for(int id: orders){
            if(!idMapArticle.containsKey(id)){
//                由于sql 的筛选功能，所以未必所有的文章都会被选出来、
               continue;
            }
            newList.add(idMapArticle.get(id));
        }
        return newList;
    }

    public static ArrayList<Attach> orderAttachBy(ArrayList<Attach> attaches, int[] orders){
        ArrayList<Attach> newList = new ArrayList<Attach>();
        HashMap<Integer,Attach> idMapAttach = new HashMap<Integer, Attach>();
        for(Attach attach: attaches){
            idMapAttach.put(attach.getId(),attach);
        }
        for(int id: orders){
            newList.add(idMapAttach.get(id));
        }
        return newList;
    }

    public static ArrayList<Article> orderArticleForAttachBy(ArrayList<Article> articles, int[] orders){
        ArrayList<Article> newList = new ArrayList<Article>();
        HashMap<Integer,Article> idMapArticle = new HashMap<Integer, Article>();
        for(Article article: articles){
            ArrayList<Attach> attaches = article.getAttaches();
//           必然有至少一个attach
            if(attaches.size() == 1){
                idMapArticle.put(attaches.get(0).getId(), article);
            }else if(attaches.size() > 1){
                for(int i = 0;i < attaches.size(); i ++) {
//                创建文章副本
                    idMapArticle.put(attaches.get(i).getId(), copyArticleWithAttach(article,i));
                }
            }else{
                Shower.printf("error","天哪!不可能之错误！");
            }
        }
        for(int id: orders){
            if(!idMapArticle.containsKey(id)){
//                由于sql 的筛选功能，所以未必所有的文章都会被选出来、
                continue;
            }
            newList.add(idMapArticle.get(id));
        }
        return newList;
    }

    private static Article copyArticleWithAttach(Article article, int attachIndex){
        Article arti = new Article();
        arti.setId(article.getId());
        arti.setUrl(article.getUrl());
        arti.setTitle(article.getTitle());
        arti.setContent(article.getContent());
        arti.setPublisher(article.getPublisher().clone());
        arti.setCategory(article.getCategory().clone());
        arti.setPublished_time(article.getPublished_time());
        arti.setUpdate_time(article.getUpdate_time());

//        仅设置一个id
        ArrayList<Attach> attachelist = new ArrayList<Attach>();
        attachelist.add(article.getAttaches().get(attachIndex));
        arti.setAttaches(attachelist);
        return arti;
    }
}
