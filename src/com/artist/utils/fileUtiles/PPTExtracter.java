package com.artist.utils.fileUtiles;

import jxl.read.biff.BiffException;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

import java.io.*;
import java.net.URL;

/**
 * Created by DoneSpeak on 2017/6/9.
 * 【参考】
 * [ Java通过poi读取word，excel，ppt文件中文本（excel，ppt部分）](http://blog.csdn.net/RunningTerry/article/details/47105767)
 */
public class PPTExtracter extends TextExtracter {

    private TextType textType = TextType.DOC;

    @Override
    public String extractText(File file) throws Exception {
        textType = FileUtil.getOfficeType(file.getName());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return output(fis);
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
        textType = FileUtil.getOfficeType(url.getPath());
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
    public void extractText2File(File srcFile, File distFile) throws Exception {
        textType = FileUtil.getOfficeType(srcFile.getName());
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
        textType = FileUtil.getOfficeType(url.getPath());
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

    public String output(InputStream in) throws IOException {
        XSLFPowerPointExtractor xslfExtractor = null;
        PowerPointExtractor pptExtractor = null;
        try {
            if (textType == TextType.PPTX) {
//            获取 2007 +
                XMLSlideShow slide = new XMLSlideShow(in);
                xslfExtractor = new XSLFPowerPointExtractor(slide);
                return xslfExtractor.getText();
            } else {
//            获取 97-2003
                pptExtractor = new PowerPointExtractor(in);
                return pptExtractor.getText();
            }
        }finally {
            if(xslfExtractor != null){
                try {
                    xslfExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(pptExtractor != null){
                try {
                    pptExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void output(InputStream in, File distFile) throws IOException {
        XSLFPowerPointExtractor xslfExtractor = null;
        PowerPointExtractor pptExtractor = null;
        try {
            if (textType == TextType.PPTX) {
//            获取 2007 +
                XMLSlideShow slide = new XMLSlideShow(in);
                xslfExtractor = new XSLFPowerPointExtractor(slide);
                string2File(xslfExtractor.getText(),distFile);
            } else {
//            获取 97-2003
                pptExtractor = new PowerPointExtractor(in);
                string2File(pptExtractor.getText(),distFile);
            }
        }finally {
            if(xslfExtractor != null){
                try {
                    xslfExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(pptExtractor != null){
                try {
                    pptExtractor.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String distFilePath = ".\\src\\com\\artist\\asserts\\ppt.txt";
        String srcFilePath = ".\\src\\com\\artist\\asserts\\testPPt.pptx";
        String srcFilePath972003 = ".\\src\\com\\artist\\asserts\\ppt972003.ppt";
        String testUrl = "";
        TextExtracter extracter = TextExtracterFactory.getExtracter(TextType.PPT);
//        获取字符串没有问题
//        String str = extracter.extractText(new File(srcFilePath));
//        未测试
//        String str = extracter.extractText(new URL(testUrl));
//        System.out.println(str);

//        获取字符串输出到文件中没有问题
//        extracter.extractText2File(new URL(testUrl),new File(distFilePath));
        extracter.extractText2File(new File(srcFilePath),new File(distFilePath));
    }


}
