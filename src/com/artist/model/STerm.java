package com.artist.model;

public class STerm {
  private Long id;
  private String term;
  private Long df;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public Long getDf() {
    return df;
  }

  public void setDf(Long df) {
    this.df = df;
  }

  @Override
  public int hashCode(){
    return (id + "#" + term + "#" + df).hashCode();
  }

  @Override
  public boolean equals(Object obj){
    if(!(obj instanceof STerm)){
      return false;
    }
    STerm term = (STerm)obj;
    if(id == term.id && term.term.equals(this.term) && df == term.df){
      return true;
    }
    return false;
  }
}
