package com.artist.utils.fileUtiles;

import org.apache.poi.POIOLE2TextExtractor;
import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.hdgf.extractor.VisioTextExtractor;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.xmlbeans.XmlException;

import java.io.*;

/**
 * Created by DoneSpeak on 2017/6/9.
 */
public class Ignore_Test {

    public void test(String inputFile) throws IOException, OpenXML4JException, XmlException {
        FileInputStream fis = new FileInputStream(inputFile);
        POIFSFileSystem fileSystem = new POIFSFileSystem(fis);
// Firstly, get an extractor for the Workbook
//        【！】需要确保有xmlbeans-2.6.0.jar (http://www.java2s.com/Code/Jar/x/Downloadxmlbeans260jar.htm)
        POIOLE2TextExtractor oleTextExtractor = ExtractorFactory.createExtractor(fileSystem);
// Then a List of extractors for any embedded Excel, Word, PowerPoint
// or Visio objects embedded into it.
        POITextExtractor[] embeddedExtractors = ExtractorFactory.getEmbededDocsTextExtractors(oleTextExtractor);
        for (POITextExtractor textExtractor : embeddedExtractors) {
            // If the embedded object was an Excel spreadsheet.
            if (textExtractor instanceof ExcelExtractor) {
                ExcelExtractor excelExtractor = (ExcelExtractor) textExtractor;
                System.out.println(excelExtractor.getText());
            }
            // A Word Document
            else if (textExtractor instanceof WordExtractor) {
                WordExtractor wordExtractor = (WordExtractor) textExtractor;
                String[] paragraphText = wordExtractor.getParagraphText();
                for (String paragraph : paragraphText) {
                    System.out.println(paragraph);
                }
                // Display the document's header and footer text
//                System.out.println("Footer text: " + wordExtractor.getFooterText());
//                System.out.println("Header text: " + wordExtractor.getHeaderText());
            }
            // PowerPoint Presentation.
            else if (textExtractor instanceof PowerPointExtractor) {
                PowerPointExtractor powerPointExtractor =
                        (PowerPointExtractor) textExtractor;
                System.out.println("Text: " + powerPointExtractor.getText());
                System.out.println("Notes: " + powerPointExtractor.getNotes());
            }
            // Visio Drawing
            else if (textExtractor instanceof VisioTextExtractor) {
                VisioTextExtractor visioTextExtractor =
                        (VisioTextExtractor) textExtractor;
                System.out.println("Text: " + visioTextExtractor.getText());
            }
        }
    }

    private void string2File(String str, File distFile) throws IOException {
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

    public static void main(String[] args) throws IOException, OpenXML4JException, XmlException {
        String srcFilePath = "";
        String srcExcelFilePath = ".\\src\\com\\artist\\asserts\\2017674715门2学分校外mooc课程线下考试安排表.xlsx";
        String testUrl = "http://www1.szu.edu.cn/board/uploadfiles/20176943%E9%99%84%E4%BB%B62%EF%BC%9A%E6%9C%AA%E6%9D%A5%E4%BA%A7%E4%B8%9A%E9%A1%B9%E7%9B%AE%E7%BB%A9%E6%95%88%E8%AF%84%E4%BB%B7%E6%8A%A5%E5%91%8A.doc";
        Ignore_Test extracter = new Ignore_Test();
//        String str = extracter.extractText(new URL(testUrl));
        extracter.test(srcExcelFilePath);
//        System.out.println(str);
    }
}
