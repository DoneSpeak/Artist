package com.artist.model;

// 文档号，词频
// TODO 可以拓展为：文档号、词频、权重
public class Pair {
	public int docId;
	public int tf;
	
	public Pair(int docId, int tf){
		this.docId = docId;
		this.tf = tf;
	}
	
	@Override
	public int hashCode(){
		return docId;
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Pair)){
			return false;
		}
		Pair pair = (Pair)obj;
		if(pair.docId == docId && pair.tf == tf){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "{" +
				"docId=" + docId +
				", tf=" + tf +
				'}';
	}
}
