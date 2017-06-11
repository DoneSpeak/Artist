package com.artist.model.enums;

/**
 * Created by DoneSpeak on 2017/6/7.
 */
public enum BelongTo {
    ARTICLE,
    ATTACH;

    public static BelongTo toEnum(String belongTo) throws Exception{
        return Enum.valueOf(BelongTo.class,belongTo);
    }
    public static int getIndex(String belongTo) throws Exception{
//        如果字符串不是相应的值，则会抛出异常
        return Enum.valueOf(BelongTo.class,belongTo).ordinal();
    }

    public static int getIndex(BelongTo belongTo){
        return belongTo.ordinal();
    }

    public static BelongTo getEnum(int index){
        BelongTo[] belongs = BelongTo.values();
        return belongs[index];
    }
}
