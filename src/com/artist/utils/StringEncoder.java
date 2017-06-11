package com.artist.utils;

import com.artist.utils.fileUtiles.FileUtil;

import java.io.UnsupportedEncodingException;

/**
 * 转换字符串的编码
 * 【参考】
 * [Java 正确的做字符串编码转换](https://my.oschina.net/mojiewhy/blog/177473)
 * [ JAVA字符编码系列三：Java应用中的编码问题](http://blog.csdn.net/qinysong/article/details/1179513)
 * []()
 *
 */
public class StringEncoder {
    /** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
    public static final String US_ASCII = "US-ASCII";

    /** ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 */
    public static final String ISO_8859_1 = "ISO-8859-1";

    /** 8 位 UCS 转换格式 */
    public static final String UTF_8 = "UTF-8";

    /** 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 */
    public static final String UTF_16BE = "UTF-16BE";

    /** 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 */
    public static final String UTF_16LE = "UTF-16LE";

    /** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
    public static final String UTF_16 = "UTF-16";

    /**汉字国标码 - 双字节编码，而英文字母和iso8859-1一致（兼容iso8859-1编码） - 只能表示简体字 */
    public static final String GB2312 = "GB2312";

    /** 中文超大字符集 - 能够用来同时表示繁体字和简体字 - 兼容gb2312编码的 */
    public static final String GBK = "GBK";

    /**
     * 将字符编码转换成US-ASCII码
     */
    public String toASCII(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, US_ASCII);
    }
    /**
     * 将字符编码转换成ISO-8859-1码
     */
    public String toISO_8859_1(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, ISO_8859_1);
    }
    /**
     * 将字符编码转换成UTF-8码
     */
    public String toUTF_8(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, UTF_8);
    }
    /**
     * 将字符编码转换成UTF-16BE码
     */
    public String toUTF_16BE(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, UTF_16BE);
    }
    /**
     * 将字符编码转换成UTF-16LE码 - Unicode - java 内存默认编码方式
     */
    public String toUTF_16LE(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, UTF_16LE);
    }
    /**
     * 将字符编码转换成UTF-16码
     */
    public String toUTF_16(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, UTF_16);
    }
    /**
     * 将字符编码转换成GBK码
     */
    public String toGBK(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, GBK);
    }

    /**
     * 将字符编码转换成gb2312码
     */
    public String toGB2312(String str) throws UnsupportedEncodingException{
        return this.changeCharset(str, GB2312);
    }

    /**
     * 字符串编码转换的实现方法
     * @param str  待转换编码的字符串
     * @param charset 目标编码
     * @return
     * @throws UnsupportedEncodingException
     */
    public String changeCharset(String str, String charset) throws UnsupportedEncodingException {
        if (str != null) {
            //用新的字符编码生成字符串
            return new String(str.getBytes(charset), charset);
        }
        return null;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        StringEncoder test = new StringEncoder();
//        默认是Unicode - utf-16 LE
        String str = "This is a 中文的 String!";
        String gb2312 = test.changeCharset(str,"gb2312");
        System.out.println("gb2312:" + gb2312);
        String utf8 = test.changeCharset(gb2312, StringEncoder.UTF_8);
        System.out.println("utf8: " + utf8);
        System.out.println();
        FileUtil fileUtil = new FileUtil();
        try {
            fileUtil.string2File(gb2312,".\\src\\com\\artist\\download\\gb2312.txt");
            fileUtil.string2File(utf8,".\\src\\com\\artist\\download\\utf8.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}