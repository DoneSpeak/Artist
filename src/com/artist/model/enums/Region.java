package com.artist.model.enums;

/**
 * Created by DoneSpeak on 2017/6/7.
 */
public enum Region {

    ARTICLETITLE,
    ARTICLECONTENT,
    ATTACHFILENAME,
    ATTACHCONTENT;

    public static Region toEnum(String region) throws Exception{
        return Enum.valueOf(Region.class,region);
    }

    public static int getIndex(String region) throws Exception{
//        必须转化为大小
        return Enum.valueOf(Region.class,region.toUpperCase()).ordinal();
    }

    public static int getIndex(Region region){
        return region.ordinal();
    }

    public static Region getEnum(int index) throws Exception {
        Region[] regions = Region.values();
        if(index < 0 || index > regions.length){
            throw new Exception("out of region rang");
        }
        return regions[index];
    }

    public static void main(String[] args) throws Exception {
        System.out.println(Region.ARTICLECONTENT.hashCode());
        System.out.println(Region.ATTACHCONTENT.hashCode());
        System.out.println(Region.ARTICLECONTENT.equals(Region.ARTICLECONTENT));
        System.out.println(Region.ARTICLECONTENT.equals(Region.ATTACHCONTENT));

        Enum en = Region.ARTICLECONTENT;
        System.out.println(en instanceof Region);
        System.out.println(en instanceof BelongTo);
    }

    public static boolean isArticleRegion(int region) throws Exception {
        return isArticleRegion(Region.getEnum(region));
    }

    public static boolean isAttachRegion(int region) throws Exception {
        return isAttachRegion(Region.getEnum(region));
    }

    public static boolean isArticleRegion(Region region){
        if(region == Region.ARTICLETITLE || region == Region.ARTICLECONTENT){
            return true;
        }
        return false;
    }

    public static boolean isAttachRegion(Region region){
        if(region == Region.ATTACHFILENAME || region == Region.ATTACHCONTENT){
            return true;
        }
        return false;
    }
}
