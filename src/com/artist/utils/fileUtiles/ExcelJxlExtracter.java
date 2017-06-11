package com.artist.utils.fileUtiles;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.*;
import java.net.URL;

/**
 * Created by DoneSpeak on 2017/6/9.
 * 【参考】
 * [Java程序员从笨鸟到菜鸟之（一百零三）java操作office和pdf文件（一）java读取word，excel和pdf文档内容](http://blog.csdn.net/csh624366188/article/details/8161122)
 * [jxl](https://sourceforge.net/projects/jexcelapi/files/jexcelapi/2.6.12/)
 * [Java Excel API - A Java API to read, write, and modify Excel spreadsheets](http://jexcelapi.sourceforge.net/)
 * [jxl下载文件中的说明doc和tutorial.html]
 * 【说明】
 * 只能处理 【97-2003 workbook】, 无法处理2007 版本: "Excel 97-2003 workbook"
 */
public class ExcelJxlExtracter extends TextExtracter {
    @Override
    public String extractText(File file) throws Exception {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return output(file);
        }finally {
            if(fis != null){
                try {
                    fis.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public String extractText(URL url) throws Exception {
        InputStream in = null;
        try {
            in = getInputStreamFromURLWithClient(url);
            return output(in);
        }finally {
            if(in != null){
                try {
                    in.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void extractText2File(File srcFile, File distFile) throws IOException, BiffException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(srcFile);
            output(fis, distFile);
        }finally {
            if(fis != null){
                try {
                    fis.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public void extractText2File(URL url, File distFile) throws IOException, BiffException {
        InputStream in = null;
        try {
            in = getInputStreamFromURLWithClient(url);
            output(in, distFile);
        }finally {
            if(in != null){
                try {
                    in.close();
                }catch (IOException ex){
                    ex.printStackTrace();
                }
            }
        }
    }

    public String output(File file) throws Exception {
        StringBuffer sb = new StringBuffer();
        Workbook rwb = Workbook.getWorkbook(file);

        //获取 sheet表 集合
        Sheet[] sheet = rwb.getSheets();
        for (int i = 0; i < sheet.length; i++) {
            Sheet rs = rwb.getSheet(i);
            for (int j = 0; j < rs.getRows(); j++) {
//                获取一行的所有党员格
                Cell[] cells = rs.getRow(j);
//                获取第一行，第二列的值
//                Cell c10 = rs.getCell(1, 0);
                for(int k=0;k<cells.length;k++) {
//                    遍历每个单元格
                    sb.append(cells[k].getContents());
                }
            }
        }
        return sb.toString();
    }

    public String output(InputStream in) throws Exception {
        StringBuffer sb = new StringBuffer();
        Workbook rwb = Workbook.getWorkbook(in);

        //获取 sheet表 集合
        Sheet[] sheet = rwb.getSheets();
        for (int i = 0; i < sheet.length; i++) {
            Sheet rs = rwb.getSheet(i);
            for (int j = 0; j < rs.getRows(); j++) {
//                获取一行的所有党员格
                Cell[] cells = rs.getRow(j);
//                获取第一行，第二列的值
//                Cell c10 = rs.getCell(1, 0);
                for(int k=0;k<cells.length;k++) {
//                    遍历每个单元格
                    sb.append(cells[k].getContents());
                }
            }
        }
        return sb.toString();
    }

    public void output(InputStream in,File distFile) throws IOException, BiffException {
        BufferedWriter writer = null;
        try {
            Workbook rwb = Workbook.getWorkbook(in);
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(distFile)));

            //获取 sheet表 集合
            Sheet[] sheet = rwb.getSheets();
            for (int i = 0; i < sheet.length; i++) {
                Sheet rs = rwb.getSheet(i);
                for (int j = 0; j < rs.getRows(); j++) {
//                获取一行的所有单元格
                    Cell[] cells = rs.getRow(j);
//                获取第一行，第二列的值
//                Cell c10 = rs.getCell(1, 0);
                    StringBuffer sb = new StringBuffer();
                    for (int k = 0; k < cells.length; k++) {
//                    遍历每个单元格
                        sb.append(cells[k].getContents());
                    }
                    writer.write(sb.toString());
                }
            }
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


    public static void main(String[] args) throws Exception {
        String distExcelFilePath = ".\\src\\com\\artist\\asserts\\exceljxl.txt";
        String srcExcelFilePath2007 = ".\\src\\com\\artist\\asserts\\excel2007.xls";
        String srcExcelFilePath = ".\\src\\com\\artist\\asserts\\excel972003.xlt";
        String testUrl = "http://www1.szu.edu.cn/board/uploadfiles/2017674715%E9%97%A82%E5%AD%A6%E5%88%86%E6%A0%A1%E5%A4%96mooc%E8%AF%BE%E7%A8%8B%E7%BA%BF%E4%B8%8B%E8%80%83%E8%AF%95%E5%AE%89%E6%8E%92%E8%A1%A8.xlsx";
        TextExtracter extracter = TextExtracterFactory.getExcelJxlExtracter();
//        只能读取 97-3003版本的excel
//        String str = extracter.extractText(new File(srcExcelFilePath));
//        无法处理2007以上版本 jxl.read.biff.BiffException: Unable to recognize OLE stream
        String str = extracter.extractText(new File(srcExcelFilePath2007));
//        未测试
//        String str = extracter.extractText(new URL(testUrl));
        System.out.println(str);

//        未测试
//        extracter.extractText2File(new URL(testUrl),new File(distExcelFilePath));
//        只能读取 97-3003版本的excel
        extracter.extractText2File(new File(srcExcelFilePath),new File(distExcelFilePath));
    }
}
