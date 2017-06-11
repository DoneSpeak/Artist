package com.artist.utils.indexer;

import com.artist.model.Pair;
import com.artist.model.Postings;
import com.artist.model.enums.Region;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
public class IndexManager {
    //  以{region:{word:[{2:100},{3:9},{4:4}]}}的方式记录词典，后面的数组表示出现该词项的{文档编号:词频}
	private HashMap<Region, Postings> regionMapPostings;
	//    {region{id: tf}} 记录文档的编号和对应文档的词项数
	private HashMap<Region,HashMap<Integer, Integer>> regionDocLengths;

	public IndexManager(){
        regionMapPostings = new HashMap<Region, Postings>();
		regionDocLengths = new HashMap<Region,HashMap<Integer, Integer>>();
    }

    public HashMap<Region, Postings> getRegionMapPostings() {
        return regionMapPostings;
    }

    public HashMap<Region,HashMap<Integer, Integer>> getRegionDocLengths() {
        return regionDocLengths;
    }

    public HashMap<Integer, Integer> getDocLenghts(Region region) {
	    if(regionDocLengths.containsKey(region)){
            return regionDocLengths.get(region);
        }
        return null;
    }

    public int getDocNum(Region region) {
        if(regionDocLengths.containsKey(region)){
            return regionDocLengths.get(region).size();
        }
        return 0;
    }

    public Postings getPostiongs(Region region){
	    if(regionMapPostings.containsKey(region)){
	        return regionMapPostings.get(region);
        }
	    return null;
    }

    public ArrayList<Pair> getPairArray(Region region, String term){
        return regionMapPostings.get(region).getPairArray(term);
    }
	
//	获取含有 terms 中单词的文档编号集合
	public HashSet<Integer> getDocsForTerms(Region region, Set<String> terms){
        HashSet<Integer> resultDocs = new HashSet<Integer>();
        Postings postings = regionMapPostings.get(region);
        for(String termStr : terms){
            resultDocs.addAll(postings.getDocIds(termStr));
        }
		return resultDocs;
	}
	
//	获取含有query中单词的文档编号集合 - 需要进行分词
	public HashSet<Integer> getDocsForQuery(Region region, String query){
		return getDocsForTerms(region, IndexTool.wordTokenizor(query));
	}

    //   添加一个 regionMapPostings
    public void add2RegionMapPostings(HashMap<Region, Postings> regionMapPostings){
//        {region:{word:[{2:100},{3:9},{4:4}]}} ==> region和word可以相同，需要分开是否重复处理
        Set<Region> regionSet = regionMapPostings.keySet();
        for(Region region: regionSet){
            Postings newP = regionMapPostings.get(region);
            if(this.regionMapPostings.containsKey(region)){
//                已包含，在原有的postings中添加
                Postings postings = this.regionMapPostings.get(region);
                postings.add(newP);
            }else{
//                未包含，压入新的postings
                this.regionMapPostings.put(region,newP);
            }
        }
    }

    public void add2DocLenghts(HashMap<Region, HashMap<Integer, Integer>> regionDocLenghts){
        Set<Region> regionSet = regionDocLenghts.keySet();
        for(Region region: regionSet){
            if(this.regionDocLengths.containsKey(region)){
//        id: tf ==> 直接添加即可，不会出现重复
                this.regionDocLengths.get(region).putAll(regionDocLenghts.get(region));
            }else {
//              未包含 该 region
                this.regionDocLengths.put(region, regionDocLenghts.get(region));
            }
        }
    }

    public void addPostings(String region, Postings postings) throws Exception {
        addPostings(Region.toEnum(region), postings);
    }

    public void addPostings(Region region, Postings postings){
        Postings innerPostings = regionMapPostings.get(region);
        innerPostings.add(postings);
    }
	
	
}
