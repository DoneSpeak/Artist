package com.artist.model;


public class Attach {

  private int id;
  private String url;
  private String filename;
  private int article_id;

  private String content;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }


  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }


  public String getFilename() {
    return filename;
  }

  public void setFilename(String title) {
    this.filename = title;
  }


  public int getArticle_id() {
    return article_id;
  }

  public void setArticle_id(int article_id) {
    this.article_id = article_id;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Attach clone(){
      Attach attach = new Attach();
      attach.setId(id);
      attach.setUrl(url);
      attach.setFilename(filename);
      attach.setArticle_id(article_id);
      attach.setContent(content);
      return attach;
  }

  @Override
  public String toString() {
    return "Attach{" +
            "id=" + id +
            ", url='" + url + '\'' +
            ", filename='" + filename + '\'' +
            ", article_id=" + article_id +
            '}';
  }
}
