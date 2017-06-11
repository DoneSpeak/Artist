package com.artist.test;

/**
 * Created by DoneSpeak on 2017/6/3.
 */
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException{

        CloseableHttpResponse response = null;
        try {
            URI uri = new URIBuilder()
                .setScheme("http")
                .setHost("www.szu.edu.cn")
                .setPath("/board/")
                .setParameter("infotype", "教务")
                .setCharset(Charset.forName("gb2312"))
                .build();
            System.out.println(uri + "\n");
            HttpPost httppost = new HttpPost(uri);
            CloseableHttpClient httpclient = HttpClients.createDefault();

            //设置请求和传输超时时间
            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .build();

            httppost.setConfig(requestConfig);
            //执行 post 请求
            response = httpclient.execute(httppost);
//          HttpClient 根据发送的 HTTP 消息属性来选择最合适的转码
//            StringEntity entityhead = new StringEntity("important message", ContentType.create("html/text", Charset.forName("gb2312")));
//            entityhead.setChunked(true);
//            httppost.setEntity(entityhead);

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                //返回获取实体
                HttpEntity entity=response.getEntity();
                System.out.println(entity.getContentType().getValue());
//                System.out.println(entity.getContent());
//                System.out.println(entity.getContentEncoding().getValue());
                System.out.println(entity.getContentType().getValue());
                //获取网页内容，指定编码
                String web= EntityUtils.toString(entity,"gb2312");
                //输出网页
                System.out.println(web);
            }else{
                System.err.println("parse failed: "+ response.getStatusLine());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally{
            response.close();
        }


    }
}