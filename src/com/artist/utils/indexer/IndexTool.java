package com.artist.utils.indexer;

import com.artist.model.enums.Region;
import com.hankcs.hanlp.seg.common.Term;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by DoneSpeak on 2017/6/7.
 */
public class IndexTool {

    //		TODO 查找下如何使用停用词 - 创建需要花费的时间
//    public static Segment segment = HanLP.newSegment()
//            .enableCustomDictionary(false)
//            .enablePlaceRecognize(false)
//            .enableOrganizationRecognize(false)
//            .enableNumberQuantifierRecognize(true);

//    是否使用停用词
    private static boolean useStopWord = false;
    //    包含了禁用停用词 - WordSegmenter 默认使用
    public static WordSegmenter segment = new WordSegmenter(useStopWord);

    public static void initSeg(){
        segment.segment("");
    }

    public static String createDocLengthsKey(Region region, int id){
        return Region.getIndex(region) + "#" + id;
    }

    //	{term:tf}
    public static HashMap<String, Integer> termTokenize(String content){
        HashMap<String, Integer> termTF = new HashMap<String, Integer>();
//		分词
        List<Term> terms = segment.segment(content);

        for(Term term : terms){
//            将单词转化为小写
            String word = term.word.toLowerCase();
            if(termTF.containsKey(word)){
                int val = termTF.get(word);
                termTF.replace(word, val++);
            }else{
                termTF.put(word, 1);
            }
        }
        return termTF;
    }

    public static Set<String> wordTokenizor(String content){
        HashMap<String, Integer> termTF = termTokenize(content);
        return termTF.keySet();
    }
}
