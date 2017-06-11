package com.artist.utils;

import com.artist.model.Article;
import com.artist.model.Postings;
import com.artist.model.enums.Region;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by DoneSpeak on 2017/6/5.
 */
public class Shower {
    public static void printf(String field, Object content){
        System.out.println("#" + field + ": " + content.toString());
    }

    public static <T> void showArray(List<T> list){
        for(int i = 0; i < list.size(); i ++){
            System.out.println(i + " : " + list.get(i));
        }
    }

    public static void showArticles(List<Article> list){
        for(int i = 0; i < list.size(); i ++){
            Article article = list.get(i);
            if(article == null){
                Shower.printf("article","null");
                continue;
            }
//            System.out.println(article.toString());
//            Shower.printf("publisher",article.getPublisher());
//            Shower.printf("category",article.getCategory());
            Shower.printf("attaches",article.getAttaches());
        }
    }

    public static void showRegionMapPostings(HashMap<Region, Postings> regionMapPostings){
        Set<Region> regions = regionMapPostings.keySet();
        for(Region region: regions){
            System.out.println(region.toString() + ": ");
            Postings postings = regionMapPostings.get(region);
            postings.show();
            System.out.println();
        }
    }

//    {region{id: tf}} 记录文档的编号和对应文档的词项数
    public static void showDocLengths(HashMap<Region,HashMap<Integer, Integer>> regionDocLenghts){
        Set<Region> regions = regionDocLenghts.keySet();
        for(Region region: regions){
            System.out.println(region.toString() + ": ");
            HashMap<Integer, Integer> docTF = regionDocLenghts.get(region);
            Set<Integer> docs = docTF.keySet();
            for(int id: docs){
                System.out.print("{" + id + "," + docTF.get(id) + "}, ");
            }
            System.out.println();
        }
    }
}
