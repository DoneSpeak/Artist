package com.artist.utils.indexer;

import com.artist.model.Pair;
import com.artist.model.Postings;
import com.artist.model.Score;
import com.artist.model.enums.Region;
import com.artist.utils.Shower;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class IndexSearcher {

    public IndexConstructor indexConstructor;
    public IndexManager indexManager;

    public IndexSearcher(){
        indexConstructor = new IndexConstructor();
        indexManager = new IndexManager();
    }

//    得到最符合请求的 k 个结果
    public int[] search(Region region, String query, int k){
        Score[] scores = CosineScore(region, query, k);
        for(int i = 0; i < scores.length; i ++){
            System.out.println("#score-" + i + ": " + scores[i]);
        }
        int[] docIds = new int[scores.length];
        for(int i = 0; i < scores.length; i ++){
            docIds[i] = scores[i].docId;
        }
        return docIds;
    }

    private Score[] CosineScore(Region region, String query, int k) {
        HashMap<String, Integer> queryTerms = IndexTool.termTokenize(query);
        Set<String> terms = queryTerms.keySet();
        Shower.printf("query",query);
        for(String term: terms){
            System.out.print(term + ":" + queryTerms.get(term));
        }
        System.out.println();
        return CosineScore(region, queryTerms,k);
    }

    //	求 cosine score
    private Score[] CosineScore(Region region, HashMap<String, Integer> queryTerms, int k){
        HashMap<Integer, Float> scores = new HashMap<Integer, Float>();
//        {docId:tf}
        HashMap<Integer, Integer> docLenghts = indexManager.getDocLenghts(region);
//		处理请求中的词项
        Postings postiongs = indexManager.getPostiongs(region);

        if(postiongs == null){
            return new Score[0];
        }
//		文档集的大小
        int N = indexManager.getDocNum(region);
        Set<String> qTerms = queryTerms.keySet();
        for(String term: qTerms){
//			遍历请求中的所有词项

//			获得该词的倒排索引记录表
            ArrayList<Pair> pairs = postiongs.getPairArray(term);
            if(pairs == null || pairs.isEmpty()){
                continue;
            }
//			词项在请求中的词频
            int tfq = queryTerms.get(term);
//			query 词项 term 在词典中的文档频数
            int df = postiongs.getTermDocFre(term);
            float idfq = (float) Math.log10(N * 1.0 / df);
            float wtq = tfq * idfq;

//			求得该词对于所有的文档的权重 - 文档与请求的相关度计算
            for(Pair p: pairs){
//               term 在文档中的权重使用了文档的出现的频率 是否需要使用TF-IDF作为权重
//               TF-IDF = tf * idf = tf * log(N/df), 对于不同的文档,同一个词项 tf-idf 的区别在于tf,
//               但是对于不同的词，即使在同一篇文章中，算的的权重也会是不同的，因为 df 可能不同。

//              使用TF-IDF作为term在文档中的权重，也可以直接使用tf作为权重
                float wftd = p.tf * idfq;

                float score = wftd * wtq;
                if(scores.containsKey(p.docId)){
                    score += scores.get(p.docId);
                    scores.replace(p.docId,score);
                }else{
                    scores.put(p.docId, score);
                }
            }
        }
//		[start] score/lenght 获取score前k的结果
        Set<Integer> docIds = scores.keySet();
        for(Integer docId: docIds){
            scores.replace(docId, scores.get(docId)/docLenghts.get(docId));
        }
//		选出排名前k个
        Score[] topKScore = topK(scores, k);

        return topKScore;
    }

    public static Score[] topK(HashMap<Integer, Float> scores, int k){
        if(k < 0 || k > scores.size()){
            k = scores.size();
        }else if(k == 0){
            return null;
        }
        Score[] topKScore = new Score[k];
        Set<Integer> docIds = scores.keySet();

        int lastOneIndex = -1;
        int j = lastOneIndex;
        for(int i: docIds){
            for(j = lastOneIndex; j >= 0; j --){
//				System.out.println(L[j].Act + "<" + TermValue.get(i).Act + " = " + (L[j].Act < TermValue.get(i).Act));
                if(topKScore[j].score > scores.get(i)){
//					找到第一个大于 TermValue.get(i).Act 的值得
                    break;
                }
            }
            if(j + 1 < k){
//				j + 1 为要插入的位置
                for(int p = k - 1; p > j + 1; p --){
                    topKScore[p] = topKScore[p - 1];
                }
                topKScore[j+1] = new Score(i,scores.get(i));
            }
            lastOneIndex ++;
            lastOneIndex = lastOneIndex >= k - 1 ? k - 1: lastOneIndex;
        }
        return topKScore;
    }

    public static void testTopK(){

        HashMap<Integer, Float> scores = new HashMap<Integer, Float>();
        for(int i = 0; i < 100; i ++){
            scores.put(i,(float) i);
        }
        for(int i = 1000; i > 800; i --){
            scores.put(i,(float) i);
        }
        for(int i = 300; i < 500; i ++){
            scores.put(i,(float) i);
        }
        IndexSearcher searcher = new IndexSearcher();
        Score[] result = searcher.topK(scores,10);
        for(Score score: result){
            System.out.println(score);
        }
    }

    public static void main(String[] args){
        testTopK();
    }

}
