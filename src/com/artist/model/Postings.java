package com.artist.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Postings {
//	{word:[{2:100},{3:9},{4:4}]}
	private HashMap<String, ArrayList<Pair>> postings;

	public Postings(){
		postings = new HashMap<String, ArrayList<Pair>>();
	}
	
	public ArrayList<Pair> getPairArray(String term){
		if(!postings.containsKey(term)){
			return null;
		}
		return postings.get(term);
	}

	public ArrayList<Integer> getDocIds(String term){
        ArrayList<Pair> pairs = getPairArray(term);
        ArrayList<Integer> docIds = new ArrayList<Integer>();
        for(Pair pair: pairs){
            docIds.add(pair.docId);
        }
        return docIds;
    }
	
	public Set<String> getTerms(){
		return postings.keySet();
	}

	public int getTermDocFre(String term){
        return postings.get(term).size();
    }

	public boolean containsTerm(String term){
		return postings.containsKey(term);
	}
	
	public void addPairToTerm(String term, Pair pair){
		postings.get(term).add(pair);
	}

	public void add(String term, Pair pair){
		if(postings.containsKey(term)){
			postings.get(term).add(pair);
		}else{
			ArrayList<Pair> pairs = new ArrayList<Pair>();
			pairs.add(pair);
			postings.put(term, pairs);
		}
	}
	
	public void add(Postings posts){
		Set<String> terms = posts.getTerms();
		for(String t: terms){
			ArrayList<Pair> pairs = posts.getPairArray(t);
//			判断单词是否出现过，如果没有出现过，则直接添加，否则进行拓展
			if(postings.containsKey(t)){
//				包含词项 - 拓展
				for(Pair p: pairs){
					addPairToTerm(t,p);	
				}
			}else{
				postings.put(t, pairs);
			}	
		}		
	}

//	{word:[{2:100},{3:9},{4:4}]}
	public void show(){
		Set<String> terms = postings.keySet();
		for(String term: terms){
			System.out.print(term + ":[");
			ArrayList<Pair> pairs = postings.get(term);
			for(Pair pair: pairs){
				System.out.print(pair.toString() + ",");
			}
			System.out.println("]");
		}
	}
}
