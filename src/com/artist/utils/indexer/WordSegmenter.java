package com.artist.utils.indexer;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.ListIterator;

/**
 * Created by DoneSpeak on 2017/6/5.
 * 分词器
 * 【参考】
 * https://github.com/hankcs/HanLP/
 */
@Component
public class WordSegmenter {

    public static Segment SEGMENT = HanLP.newSegment()
            .enableCustomDictionary(false)
            .enablePlaceRecognize(false)
            .enableOrganizationRecognize(false)
            .enableNumberQuantifierRecognize(true);

    private boolean useStopWords = true;

    public WordSegmenter(){}

    public WordSegmenter(boolean useStopWords){
        this.useStopWords = useStopWords;
    }

    public List<Term> segment(String text){
        return segment(text.toCharArray());
    }

    /**
     * 分词
     * 根据 useStopWords 的设置使用停用词
     * @param text 文本
     * @return 分词结果
     */
    public List<Term> segment(char[] text){
        List<Term> resultList = SEGMENT.seg(text);
        if(!useStopWords){
            return resultList;
        }
        ListIterator<Term> listIterator = resultList.listIterator();
        while (listIterator.hasNext()){
            if (!CoreStopWordDictionary.shouldInclude(listIterator.next())){
                listIterator.remove();
            }
        }
        return resultList;
    }




    public static void main(String[] args){

        String[] testCase = new String[]{
                "2016---2017第二学期研究生教学检查简报",
                "计算机与软件学院ESI学科建设简报",
                "第二学期研究生教学检查简报",
                "there must be you houses,yes it's",
                "MOOC课程第三次线下见面课的通知"
        };
        for (String sentence : testCase){
            WordSegmenter segmenter = new WordSegmenter(true);
            System.out.println("分词：" + segmenter.segment(sentence));
        }
    }
}
