package com.artist.utils.parser;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by DoneSpeak on 2017/6/2.
 */
public class UrlManager {
    private HashSet<String> oldUrls;
    private Queue<String> newUrls;

    public UrlManager(){
        oldUrls = new HashSet<String>();
        newUrls = new Queue<String>();
    }

//    添加一条url
    public void addNewUrl(String url){
        if(url == null || url.trim().equals("")){
            return;
        }
        url = url.trim();
//      TODO 可以考虑只检测 oldUrls，在取的时候再考虑 url 是否出现在oldUrls 中
//       判断是否已经访问过或者添加过，防止重复出现
        if(!oldUrls.contains(url) && !newUrls.contians(url)){
            newUrls.push(url);
        }
    }

//    添加一个url集合
    public void addNewUrls(Collection<String> urls){
        if(urls == null || urls.isEmpty()){
            return;
        }
        for(String url: urls){
            addNewUrl(url);
        }
    }

//    获取一条新的url
    public String getNewUrl(){
        if(newUrls.isEmpty()){
            return null;
        }
        String url = newUrls.pop();
        oldUrls.add(url);
        return url;
    }

//    判断是否还有新的url
    public boolean hasMoreNewUrls(){
        return !newUrls.isEmpty();
    }

//    获取新url的数量
    public int newUrlNum(){
        return newUrls.size();
    }

//    清空新url
    public void clearNewUrls(){
        newUrls.clear();
    }

    //    清空旧url
    public void clearOldUrls(){
        oldUrls.clear();
    }
}
