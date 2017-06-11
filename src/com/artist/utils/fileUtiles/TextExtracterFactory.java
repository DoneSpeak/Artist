package com.artist.utils.fileUtiles;

/**
 * Created by DoneSpeak on 2017/6/9.
 */
public class TextExtracterFactory {
//    public static final int TXTEXTRACTER = 0;
//    public static final int DOCEXTRACTER = 1;
//    public static final int EXCELPOIEXTRACTER = 2;
//    public static final int EXCELJXLEXTRACTER = 3;
//    public static final int PDFEXTRACTER = 4;
//    public static final int PPTEXTRACTER = 5;

    public static TextExtracter getExtracter(TextType textType){
        TextExtracter textExtracter = null;
        switch(textType){
            case TXT:{
                textExtracter = new TxtExtracter();
                break;
            }
            case DOCX:
            case DOC:{
                textExtracter = new DocExtracter();
                break;
            }
            case XLS:
            case XLSX:
            case XLT:{
                textExtracter = new ExcelPoiExtracter();
                break;
            }
            case PDF:{
                textExtracter = new PdfExtracter();
                break;
            }
            case PPT:
            case PPTX: {
                textExtracter = new PPTExtracter();
                break;
            }
            default:{
                textExtracter = null;
            }
        }
        return textExtracter;
    }

    public static TextExtracter getExcelJxlExtracter(){
        return new ExcelJxlExtracter();
    }
}
