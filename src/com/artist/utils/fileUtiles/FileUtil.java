package com.artist.utils.fileUtiles;

import java.io.*;

/**
 * Created by DoneSpeak on 2017/6/4.
 */
public class FileUtil {

    public void string2File(String str, String filepath, String charset) throws Exception {
        File file = new File(filepath);
        if(file.isDirectory()){
            throw new Exception("filepath:" + filepath + " is a directory.");
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filepath),charset));
            writer.write(str);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("write into file failed!");
        } finally {
            if(writer != null){
                writer.close();
            }
        }

    }

    public void string2File(String str, String filepath) throws Exception {
        File file = new File(filepath);
        if(file.isDirectory()){
            throw new Exception("filepath:" + filepath + " is a directory.");
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("write into file failed!");
        } finally {
            if(writer != null){
                writer.close();
            }
        }

    }


    //    URL 必须以 http:// 等协议开头，否则会报错
    public static String appendHTTP2URL(String urlStr){
        if(!urlStr.toLowerCase().startsWith("http")){
            urlStr = "http://" + urlStr;
        }
        return urlStr;
    }



    public static TextType getOfficeType(String path){
        int index = path.lastIndexOf(".");
        String extention = path.substring(index + 1).toLowerCase();
        if(extention.equals("docx")){
            return TextType.DOCX;
        }else if(extention.equals("doc")){
            return TextType.DOC;
        }else if(extention.equals("ppts")){
            return TextType.PPTX;
        }else if(extention.equals("ppt")){
            return TextType.PPT;
        }else if(extention.equals("xls")){
            return TextType.XLS;
        }else if(extention.equals("xlt")){
            return TextType.XLT;
        }else if(extention.equals("xlsx")){
            return TextType.XLSX;
        }
        return TextType.NONE;
    }
}
