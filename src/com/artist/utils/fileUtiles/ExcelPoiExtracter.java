package com.artist.utils.fileUtiles;

import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.extractor.OldExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.extractor.XSSFExcelExtractor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;

/**
 * Created by DoneSpeak on 2017/6/9.
 * 【参考】
 *  !!![Text Extraction](http://poi.apache.org/text-extraction.html)
 * [JExcelApi JavaDoc](http://jexcelapi.sourceforge.net/resources/javadocs/index.html)
 * [JExcelApi 2.6.10 JavaDoc](http://jexcelapi.sourceforge.net/resources/javadocs/2_6_10/docs/index.html)
 * [操作excel的jexcelapi-2.6.12](https://sourceforge.net/projects/jexcelapi/files/jexcelapi/2.6.12/)
 * [xmlbeans-2.6.0.jar](http://www.java2s.com/Code/Jar/x/Downloadxmlbeans260jar.htm)
 * [dom4j-1.6.1-hudson-1.jar](http://www.java2s.com/Code/Jar/d/Downloaddom4j161hudson1jar.htm)
 * [ Java通过poi读取word，excel，ppt文件中文本（excel，ppt部分）](http://blog.csdn.net/RunningTerry/article/details/47105767)
 * 【JAR】
 * 1. jxl.jar
 */
public class ExcelPoiExtracter extends TextExtracter {
    /**
     *
     * @param file
     * @return
     * @throws IOException
     * 2007+的excel需要使用 XSSF 而不是 HSSF
     */
    private TextType textType = TextType.XLS;
    @Override
    public String extractText(File file) throws Exception {
        textType = FileUtil.getOfficeType(file.getName());
        FileInputStream fis = null;
        try{
           fis = new FileInputStream(file);
           return output(fis);
        }catch (OldExcelFormatException e){
            e.printStackTrace();
            fis = new FileInputStream(file);
            return extractWithOldExcelExtractor(fis);
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
            String text = output(in);
            return text;
        }catch (OldExcelFormatException e){
            e.printStackTrace();
            in = getInputStreamFromURLWithClient( new URL(url.toString()));
            return extractWithOldExcelExtractor(in);
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
    public void extractText2File(File srcFile, File distFile) throws Exception {
        textType = FileUtil.getOfficeType(srcFile.getName());
        String text = extractText(srcFile);
        string2File(text,distFile);
    }

    @Override
    public void extractText2File(URL url, File distFile) throws IOException {
        textType = FileUtil.getOfficeType(url.getPath());
        String text = extractText(url);
        string2File(text,distFile);
    }

    public String output(InputStream in) throws IOException{
//        [JAR] commons-collections4-4.1.jar
//        [JAR] poi-ooxml-schemas-3.16.jar
        XSSFExcelExtractor xssfExtractor = null;
        ExcelExtractor excelExtractor = null;
        try {
            if (textType == TextType.XLSX) {
//            >= 2007 版本
                xssfExtractor = new XSSFExcelExtractor(new XSSFWorkbook(in));
                return xssfExtractor.getText();
            } else {
//            97-2003版本
                HSSFWorkbook wb = new HSSFWorkbook(in);
                excelExtractor = new ExcelExtractor(wb);
                return excelExtractor.getText();
            }
        }finally {
            if(xssfExtractor != null){
                try {
                    xssfExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(excelExtractor != null){
                try {
                    excelExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String extractWithOldExcelExtractor(InputStream in) throws IOException {
        OldExcelExtractor extractor = new OldExcelExtractor(in);
        return extractor.getText();
    }

    public static void main(String[] args) throws Exception {
        String distFilePath = ".\\src\\com\\artist\\asserts\\excelPoi.txt";
        String srcFilePath972003 = ".\\src\\com\\artist\\asserts\\excel972003.xls";
        String srcFilePath = ".\\src\\com\\artist\\asserts\\2017674715门2学分校外mooc课程线下考试安排表.xlsx";
        String testUrl = "http://www1.szu.edu.cn/board/uploadfiles/2017674715%E9%97%A82%E5%AD%A6%E5%88%86%E6%A0%A1%E5%A4%96mooc%E8%AF%BE%E7%A8%8B%E7%BA%BF%E4%B8%8B%E8%80%83%E8%AF%95%E5%AE%89%E6%8E%92%E8%A1%A8.xlsx";
        String tstUrl2 = "http://www.szu.edu.cn/board/uploadfiles/201753114%E6%AF%95%E4%B8%9A%E7%94%9F%E6%AC%A0%E8%B4%B9%E5%90%8D%E5%8D%95.xls";
        TextExtracter extracter = TextExtracterFactory.getExtracter(TextType.XLS);
//        获取字符串没有问题
        String str = "";
//        String str = extracter.extractText(new File(srcFilePath));
//        str = extracter.extractText(new File(srcFilePath972003));
        try {
            str  = extracter.extractText(new URL(tstUrl2));
        }catch(Exception e){
            e.printStackTrace();
            extracter = TextExtracterFactory.getExcelJxlExtracter();
            str = extracter.extractText(new URL(tstUrl2));
        }
        System.out.println(str);

//        获取字符串输出到文件中没有问题
//        extracter.extractText2File(new URL(testUrl),new File(distFilePath));
//        extracter.extractText2File(new File(srcFilePath),new File(distFilePath));
    }
}
