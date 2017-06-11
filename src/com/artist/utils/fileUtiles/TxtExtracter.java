package com.artist.utils.fileUtiles;


import java.io.*;
import java.net.URL;

/**
 * Created by DoneSpeak on 2017/6/9.
 */
public class TxtExtracter extends TextExtracter{

    @Override
    public String extractText(File file) throws IOException {
        InputStream in = new FileInputStream(file);
        return output(in);
    }

    @Override
    public String extractText(URL url) throws IOException {
        InputStream in = getInputStreamFromURLWithClient(url);
        return output(in);
    }

    @Override
    public void extractText2File(File srcFile, File distFile) throws IOException {
        InputStream in = new FileInputStream(srcFile);
        output(in, distFile);
    }

    @Override
    public void extractText2File(URL url, File distFile) throws IOException {
        InputStream in = getInputStreamFromURLWithClient(url);
        output(in, distFile);
    }

    private String output(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while((line = reader.readLine()) != null){
            sb.append(line);
        }
        return sb.toString();
    }

    private void output(InputStream in, File distFile) throws IOException {
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            ;
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(distFile)));
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
            }
            writer.flush();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        String distFilePath = ".\\src\\com\\artist\\asserts\\txt.txt";
        String srcFilePath = ".\\src\\com\\artist\\asserts\\testPdf.txt";
        String testUrl = "";
        TextExtracter extracter = TextExtracterFactory.getExtracter(TextType.TXT);
        String str = extracter.extractText(new File(srcFilePath));
        extracter.extractText2File(new File(srcFilePath),new File(distFilePath));
        System.out.println(str);
    }
}
