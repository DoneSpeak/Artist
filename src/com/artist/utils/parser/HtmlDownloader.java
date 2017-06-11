package com.artist.utils.parser;

import com.artist.utils.Shower;
import com.artist.utils.fileUtiles.FileUtil;
import com.artist.utils.fileUtiles.TextExtracterFactory;
import com.artist.utils.fileUtiles.TextType;
import com.artist.utils.fileUtiles.TextExtracter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by DoneSpeak on 2017/6/2.
 * 下载完整的HTML文件
 * 【参考】
 * [基于apache —HttpClient的小爬虫获取网页内容](http://www.dongcoder.com/detail-449399.html)
 */
public class HtmlDownloader {

    public boolean debug = false;
    private String defaultSaveDir = ".src\\com\\artist\\download";
    private String saveDir = defaultSaveDir;

    public void setSaveDir(String saveDir){
        this.saveDir = saveDir;
    }

    public String getSaveDir(){
        return saveDir;
    }

    public void useDefaultSaveDir(){
        this.saveDir = saveDir;
    }

//    参考：[基于apache —HttpClient的小爬虫获取网页内容](http://www.dongcoder.com/detail-449399.html)
    public String downloadPost(URI uri, String htmlCharset,String method) throws Exception {
        String filepath = null;
        CloseableHttpResponse response = null;
        try {
            if(debug) {
                System.out.println(uri + "\n");
            }

            response = createResponse(uri,null,method);

            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                System.err.println("parse failed: "+ response.getStatusLine());
                return null;
            }
//            //返回获取实体
            HttpEntity entity=response.getEntity();
//          获取保存文件完整路径
            filepath = saveDir + File.separator + getFileNameFromUrl(uri.getHost() + uri.getPath(),entity.getContentType().getValue());
//            保存到文件
            write2File(entity,filepath);

        } finally{
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filepath;
    }

//    获取 uri 指向连接的 html 字符串
    public String getContent(URI uri, String htmlCharset, String method) throws Exception {
        CloseableHttpResponse response = null;
        String html = null;
        try {
            if(debug) {
                System.out.println(uri + "\n");
            }
            response = getResponse(uri,method);
//              请求不成功
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                String error = "parse failed: "+ response.getStatusLine();
                System.err.println(error);
                throw new Exception(error);
            }
//            返回获取实体
            HttpEntity entity=response.getEntity();
            html = EntityUtils.toString(entity,htmlCharset);
            if(debug) {
                //输出网页
                System.out.println(html);
            }
        } finally{
            try {
                if(response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return html;
    }

    private CloseableHttpResponse createResponse(URI uri, String urlStr, String method) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .build();
        if(method.toLowerCase().equals("get")){
            HttpGet httpget = null;
            if(urlStr != null){
                httpget = new HttpGet(urlStr);
            }else if(uri != null){
                httpget = new HttpGet(uri);
            }else{
                throw new Exception("wrong object type in httpget");
            }
            httpget.setConfig(requestConfig);
            //执行 get 请求
            response = httpclient.execute(httpget);
        }else if(method.toLowerCase().equals("post")){
            HttpPost httppost = null;
            if(urlStr != null){
                httppost = new HttpPost(urlStr);
            }else if(uri != null){
                httppost = new HttpPost(uri);
            }else{
                throw new Exception("wrong object type in httppost");
            }
            httppost.setConfig(requestConfig);
            //执行 post 请求
            response = httpclient.execute(httppost);
        }else{
            String error = "can't deal with method type ：" + method;
            System.out.println(error);
            throw new Exception(error);
        }
        return response;
    }

    public CloseableHttpResponse getResponse(String urlStr, String method) throws Exception {
        return createResponse(null,urlStr,method);
    }

    public CloseableHttpResponse getResponse(URI uri, String method) throws Exception {
        return createResponse(uri,null,method);
    }

    //    获取 url 指向连接的 html 字符串
    public String getContent(String url, String htmlCharset, String method) throws Exception {
        CloseableHttpResponse response = null;
        String html = null;
        try {
            response = getResponse(url,method);
//              请求不成功
            if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                String error = "parse failed: "+ response.getStatusLine();
                System.err.println(error);
                throw new Exception(error);
            }
//            返回获取实体
            HttpEntity entity=response.getEntity();
            html = EntityUtils.toString(entity,htmlCharset);

            if(debug) {
                //输出网页
                System.out.println(html);
            }
        } finally{
            try {
                if(response != null){
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return html;
    }

    public String getCharsetFromContentType(String contentType){
        String utf8 = "UTF-8";
        if(contentType == null){
            return utf8;
        }
        if(contentType.contains("/")){
            if(contentType.toLowerCase().contains("charset")){
                String str = contentType.replaceAll("\\s+","");
                return str.substring(str.indexOf("=") + 1);
            }else{
                return utf8;
            }
        }
        if(contentType.toLowerCase().contains("charset")){
            String str = contentType.replaceAll("\\s+","");
            return str.substring(str.indexOf("=") + 1);
        }
        return contentType;
    }

//    将httpEntity 内容写到文件中
    public void write2File(HttpEntity httpEntity, String filepath) throws Exception {
        OutputStream os = null;
        try {
            File file = new File(filepath);
            if (file.isDirectory()) {
                throw new Exception("filepath：" + filepath + "is a directory.");
            }
            os = new FileOutputStream(file);
            httpEntity.writeTo(os);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("save to file failed：" + e.getMessage());
        } finally {
            if(os != null){
                os.close();
            }
        }
    }

    public String getFileNameFromUrl(String url, String contentType){
        String filename = url;
        if(url.startsWith("http://")){
            filename = url.substring(7);
        }
//        if(filename.indexOf("?") > 0){
//            filename = filename.substring(0,filename.indexOf("?"));
//        }
//        Shower.printf("contentType",contentType);
        filename = filename.replaceAll("[\\?/:*|<>\"]", "_");
//        if(contentType == null){
//        }else if(contentType.indexOf(".")!=-1){
//            filename = filename+".html";
//        }else
//        {
//            filename = filename + contentType.substring(contentType.lastIndexOf("/")+1);
//        }
        return filename;
    }

    public String getNormalFileNameFromUrl(String url, String contentType){
        String filename = url;
        int index = filename.lastIndexOf("/");
        if(index > 0){
            return filename.substring(index+1);
        }
        return filename;
    }

    public String getRandomString(){
        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String datastr=dateformat1.format(new Date());
        return datastr;
//        return UUID.randomUUID().toString();
    }

    public String getFileContentFromUrl(String url){
        FileUtil.appendHTTP2URL(url);
        TextType textType = FileUtil.getOfficeType(url);
        TextExtracter extracter = TextExtracterFactory.getExtracter(textType);
        if(extracter == null){
            return "";
        }
        try {
            return extracter.extractText(new URL(url));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void testDownloadFile(){
        HtmlDownloader downer = new HtmlDownloader();
        try {
//http://www.szu.edu.cn/board/uploadfiles/201751013%E6%8E%A7%E5%88%B6%E5%B7%A5%E7%A8%8B%E4%B8%93%E4%B8%9A%E7%A0%94%E7%A9%B6%E7%94%9F%E7%A1%95%E5%A3%AB%E5%AD%A6%E4%BD%8D%E8%AE%BA%E6%96%87%E7%AD%94%E8%BE%A9%E5%85%AC%E5%91%8A-20170513.docx
            URI uri = new URIBuilder()
                    .setScheme("http")
                    .setHost("www.szu.edu.cn")
                    .setPath("/board/uploadfiles/201751013控制工程专业研究生硕士学位论文答辩公告-20170513.docx")
//                    .setParameter("infotype", "教务")
//                    .setCharset(Charset.forName("gb2312"))
                    .build();
            Shower.printf("url",uri.toString());
            downer.setSaveDir(".\\src\\com\\artist\\download");
            String filepath = downer.downloadPost(uri,"gb2312","get");
            if(filepath != null){
                System.out.println("保存成功：" + filepath);
            }else{
                System.out.println("保存失败");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testDownloadHTML(){
        HtmlDownloader downer = new HtmlDownloader();
        try {
//https://www.google.com/#q=mybatis
            URI uri = new URIBuilder()
                    .setScheme("https")
                    .setHost("www.google.com")
                    .setPath("/#q=mybatis")
//                    .setParameter("infotype", "教务")
//                    .setCharset(Charset.forName("gb2312"))
                    .build();
            Shower.printf("url",uri.toString());
            downer.setSaveDir(".\\src\\com\\artist\\download");
            String filepath = downer.downloadPost(uri,"gb2312","get");
            if(filepath != null){
                System.out.println("保存成功：" + filepath);
            }else{
                System.out.println("保存失败");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public static void main(String[] args){
        testDownloadHTML();
    }
}
