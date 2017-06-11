package com.artist.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by DoneSpeak on 2017/6/5.
 */
public class URLTokenizer {
    private String url;
    private String protocol;
    private String file;
    private String host;
    private String path;
    private int post;
    private int defaultPost;
    private String query;

    private HashMap<String,String> params;


    public URLTokenizer(String urlStr) throws MalformedURLException {
        params = new HashMap<String,String>();
        if(!urlStr.startsWith("http")){
            urlStr = "http://" + url;
        }
        init(new URL(urlStr));
    }

    public URLTokenizer(URL url){
        params = new HashMap<String,String>();
        init(url);
    }

    public String getUrl() {
        return url;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getFile() {
        return file;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public int getPost() {
        return post;
    }

    public int getDefaultPost() {
        return defaultPost;
    }

    public String getQuery() {
        return query;
    }

    public void init(URL url){
        this.url = url.toString();
        this.protocol = url.getProtocol();
        this.file = url.getFile();
        this.host = url.getHost();
        this.post = url.getPort();
        this.defaultPost = url.getDefaultPort();
        this.path = url.getPath();
        this.query = url.getQuery();
        if(this.query != null && this.query.length() > 0){
            analizeParameters(this.query);
        }
    }

    public String getParam(String key){
        return params.get(key);
    }

    private void analizeParameters(String query){
        String[] queryItems = query.split("&");
        for(String item: queryItems){
            String[] pairs = item.split("=");
            String value = "";
            if(pairs.length > 1){
                value = pairs[1];
            }
            params.put(pairs[0],value);
        }
    }

    public static void main(String[] args){
        try {
            String urlStr = "http://www.artist.com//index.html?query=URL%E8%A7%A3%E6%9E%90&type=&klass=&duration=&publisher=";
            URLTokenizer tokenizer = new URLTokenizer(urlStr);
            System.out.println("query = " + tokenizer.getParam("query"));
            System.out.println("klass = " + tokenizer.getParam("klass"));
            System.out.println("notexist = " + tokenizer.getParam("notexist"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
