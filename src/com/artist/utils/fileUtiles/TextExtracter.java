package com.artist.utils.fileUtiles;

import jxl.read.biff.BiffException;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.xmlbeans.XmlException;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 提取文档中的文本内容
 * Created by DoneSpeak on 2017/6/9.
 */
public abstract class TextExtracter {

    /**
     * 提取文档中内容到字符串
     * @param file
     * @return
     */
    public abstract String extractText(File file) throws Exception;

    /**
     * 提取url指定的文档内容到字符串
     * @param url
     * @return
     */
    public abstract String extractText(URL url) throws Exception;

    /**
     * 提取文档的内容到指定文件
     * @param srcFile 源文件
     * @param distFile 目标文件
     */
    public abstract void extractText2File(File srcFile, File distFile) throws Exception;

    /**
     * 提取url执行的文档内容带指定文档
     * @param url
     * @param distFile 目标文件
     */
    public abstract void extractText2File(URL url, File distFile) throws IOException, BiffException;

//    利用 httpClient 获取 url 指向的输入流
    public InputStream getInputStreamFromURLWithClient(URL url) throws IOException{
        String urlStr = url.toString();

        CloseableHttpClient httpclient = HttpClients.createDefault();

        //设置请求和传输超时时间
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(5000)
                .setConnectTimeout(5000)
                .build();
        HttpGet httpget = new HttpGet(urlStr);
        httpget.setConfig(requestConfig);
        //执行 get 请求
        CloseableHttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        return entity.getContent();
    }

//    利用 URLConnection 获取 url 指向的输入流
    public InputStream getInputStreamFromURL(URL url) throws IOException {
        //返回一个 URLConnection 对象，它表示到 URL 所引用的远程对象的连接。
        URLConnection uc = url.openConnection();
        //打开的连接读取的输入流。
        return uc.getInputStream();
    }

//将一行字符串输出到一个文件中
    public void string2File(String str, File distFile) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(distFile)));
            writer.write(str);
            writer.flush();
        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
