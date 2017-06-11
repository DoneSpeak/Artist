package com.artist.utils.fileUtiles;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by DoneSpeak on 2017/6/9.
 * 【参考】
 * [java读取pdf总结](ttp://blog.csdn.net/meifage/article/details/6963985)
 * [Java程序员从笨鸟到菜鸟之（一百零三）java操作office和pdf文件（一）java读取word，excel和pdf文档内容](http://blog.csdn.net/csh624366188/article/details/8161122)
 */
public class PdfExtracter extends TextExtracter {
    public boolean sortByPosition = false;
    public int startPage = 0;
    public int endPage = Integer.MAX_VALUE;

    private PDDocument document = null;
    private Writer writer = null;

    @Override
    public String extractText(File file) throws IOException {
        try {
            OutputStream output = new ByteArrayOutputStream();
            output(file, output);
            return output.toString();
        }finally {
            close();
        }
    }

    @Override
    public String extractText(URL url) throws IOException {
        try {
            OutputStream output = new ByteArrayOutputStream();
            output(url, output);
            return output.toString();
        }finally {
            close();
        }
    }

    @Override
    public void extractText2File(File srcFile, File distFile) throws IOException {
        try {
            OutputStream output = new FileOutputStream(distFile);
            output(srcFile, output);
        }finally {
            close();
        }
    }

    @Override
    public void extractText2File(URL url, File distFile) throws IOException {
        try {
            OutputStream output = new FileOutputStream(distFile);
            output(url, output);
        }finally {
            close();
        }
    }

    /**
     * 创建一个PDFTextStripper对象，相应的通用设置均可以在这里设置
     * @return
     * @throws IOException
     */
    private PDFTextStripper createPDFTextStripper() throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setSortByPosition( sortByPosition );
        stripper.setStartPage( startPage );
        stripper.setEndPage(endPage);
        //   stripper.setEndPage(document.getNumberOfPages())
        return stripper;
    }

    /**
     * 将file的文本内容输出到outputStream中
     * @param file
     * @param output
     * @throws IOException
     */
    private void output(File file, OutputStream output) throws IOException {

        document = PDDocument.load(file);

        PDFTextStripper stripper = createPDFTextStripper();
        writer = new OutputStreamWriter(output);
        stripper.writeText(document, writer);
//      记得flush，否则数据不完整
        writer.flush();
    }

    /**
     * 将url指向的pdf的文本内容输出到outputStream中
     * @param url
     * @param output
     * @throws IOException
     */
    private void output(URL url, OutputStream output) throws IOException {
        InputStream in = null;
        try{
            in = getInputStreamFromURLWithClient(url);

            document = PDDocument.load(in);

            PDFTextStripper stripper = createPDFTextStripper();
            writer = new OutputStreamWriter(output);
            stripper.writeText(document, writer);
//      记得flush，否则数据不完整
            writer.flush();
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

    private void close(){
        if(writer != null){
//               关闭写出流
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(document != null){
//                关闭PDF Document
            try {
                document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String srcFilePath = ".\\src\\com\\artist\\asserts\\testPdf.pdf";
        String distFilePath = ".\\src\\com\\artist\\asserts\\testPdf.txt";
        String urlStr = "http://www1.szu.edu.cn/board/uploadfiles/20176913%E9%99%84%E4%BB%B63%EF%BC%9A%E6%B7%B1%E5%9C%B3%E5%B8%82%E8%B4%A2%E6%94%BF%E5%A7%94%E5%91%98%E4%BC%9A%E5%85%B3%E4%BA%8E2016%E5%B9%B4%E6%B7%B1%E5%9C%B3%E5%B8%82%E6%94%BF%E5%BA%9C%E9%9B%86%E4%B8%AD%E9%87%87%E8%B4%AD%E7%9B%AE%E5%BD%95%E7%AD%89%E4%BA%8B%E9%A1%B9%E7%9A%84%E9%80%9A%E7%9F%A5.pdf";

        PdfExtracter pdfExtracter = new PdfExtracter();
//        输出到字符串，测试没有问题
        String str = pdfExtracter.extractText(new URL(urlStr));
//        String str = pdfExtracter.extractText(new File(srcFilePath));
        System.out.println(str);

//        输出到文件，测试没有问题
//        pdfExtracter.extractText2File(new URL(urlStr),new File(distFilePath));
//        pdfExtracter.extractText2File(new File(srcFilePath),new File(distFilePath));
    }

}
