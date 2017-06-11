package com.artist.model;

// 文档号，对应的得分
public class Score {
	public int docId;
	public float score;
	
	public Score(){
		
	}
	
	public Score(int docId, float score){
		this.docId = docId;
		this.score = score;
	}
	
	@Override
	public int hashCode(){
		return (docId + score + "").hashCode();
	}
	
	@Override
	public boolean equals(Object obj){
		if(!(obj instanceof Score)){
			return false;
		}
		Score score = (Score)obj;
		if(score.docId == docId && score.score == score.score){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Score{" +
				"docId=" + docId +
				", score=" + score +
				'}';
	}
}
