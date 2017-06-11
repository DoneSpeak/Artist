package com.artist.utils.fileUtiles;


import java.io.*;
import java.net.URL;

import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * Created by DoneSpeak on 2017/6/9.
 * 【参考】
 *  [api](http://poi.apache.org/apidocs/index.html)
 *  [api-WordExtractor](http://poi.apache.org/apidocs/org/apache/poi/hwpf/extractor/WordExtractor.html)
 *  !!![Text Extraction](http://poi.apache.org/text-extraction.html)
 *  [ava通过poi读取word，excel，ppt文件中文本（word部分）](http://blog.csdn.net/RunningTerry/article/details/47086659)
 *  【JAR】(https://www.apache.org/dyn/closer.lua/poi/release/bin/poi-bin-3.16-20170419.zip)
 *  1. poi-3.16.jar
 *  2. poi-scratchpad-3.16.jar
 *  3. poi-ooxml-3.16.jar
 *  4. xmlbeans-2.6.0.jar (http://www.java2s.com/Code/Jar/x/Downloadxmlbeans260jar.htm)
 */
public class DocExtracter extends TextExtracter {

    private TextType textType = TextType.DOC;

    @Override
    public String extractText(File file) throws IOException {
        textType = FileUtil.getOfficeType(file.getName());
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(file);
            return output(fis);
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String extractText(URL url) throws IOException {
        textType = FileUtil.getOfficeType(url.getPath());
        InputStream in = null;
        try{
            in = getInputStreamFromURLWithClient(url);
            return output(in);
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void extractText2File(File srcFile, File distFile) throws IOException {
        textType = FileUtil.getOfficeType(srcFile.getName());
        FileInputStream fis = null;
        try{
            fis = new FileInputStream(srcFile);
            output(fis, distFile);
        }finally {
            if(fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void extractText2File(URL url, File distFile) throws IOException {
        textType = FileUtil.getOfficeType(url.getPath());
        InputStream in = null;
        try{
            in = getInputStreamFromURLWithClient(url);
            output(in, distFile);
        }finally {
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String output(InputStream in) throws IOException {
        WordExtractor extractor = null;
        XWPFWordExtractor wxpExtractor = null;
        StringBuffer sb = new StringBuffer();
        try {
            if(textType == TextType.DOCX){
//                2007+ 版本 .docx
                wxpExtractor = new XWPFWordExtractor(new XWPFDocument(in));
                sb.append(wxpExtractor.getText());
            }else{
//                97-2003 版本 .doc
                extractor = new WordExtractor(in);
                String[] paragraphText = extractor.getParagraphText();
                for (String paragraph : paragraphText) {
                    sb.append(paragraph);
                }
            }
            return sb.toString();
        }finally {
            if(extractor != null){
                try {
                    extractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(wxpExtractor != null){
                try {
                    wxpExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void output(InputStream in, File distFile) throws IOException {
        WordExtractor extractor = null;
        XWPFWordExtractor wxpExtractor = null;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(distFile)));

            if(textType == TextType.DOCX){
//                2007+ 版本 .docx
                wxpExtractor = new XWPFWordExtractor(new XWPFDocument(in));
                writer.write(wxpExtractor.getText());
            }else{
//                97-2003 版本 .doc
                extractor = new WordExtractor(in);
                String[] paragraphText = extractor.getParagraphText();
                for (String paragraph : paragraphText) {
                    writer.write(paragraph);
                }
                writer.flush();
//              版本太高则抛出  OfficeXmlFileException
            }

        }finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(extractor != null){
                try {
                    extractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(wxpExtractor != null){
                try {
                    wxpExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        先提取出所有的文本，再一次性写入
//        String text = output(in);
//        string2File(text,disFile);
    }

    public static void main(String[] args) throws Exception {
        String distFilePath = ".\\src\\com\\artist\\asserts\\doc.txt";
        String srcFilePath = ".\\src\\com\\artist\\asserts\\20176943附件2：未来产业项目绩效评价报告.doc";
        String srcFilePath2010 = ".\\src\\com\\artist\\asserts\\word2010.docx";
        String testUrl = "http://www1.szu.edu.cn/board/uploadfiles/20176943%E9%99%84%E4%BB%B62%EF%BC%9A%E6%9C%AA%E6%9D%A5%E4%BA%A7%E4%B8%9A%E9%A1%B9%E7%9B%AE%E7%BB%A9%E6%95%88%E8%AF%84%E4%BB%B7%E6%8A%A5%E5%91%8A.doc";
        TextExtracter extracter = TextExtracterFactory.getExtracter(TextType.DOC);
//        //        获取字符串没有问题
        String str = extracter.extractText(new File(srcFilePath2010));
//        String str = extracter.extractText(new URL(testUrl));
        System.out.println(str);

//        获取字符串输出到文件中没有问题
//        extracter.extractText2File(new URL(testUrl),new File(distFilePath));
//        extracter.extractText2File(new File(srcFilePath),new File(distFilePath));
    }
}
