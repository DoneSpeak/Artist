package com.artist.model;

import java.util.ArrayList;
import java.util.Date;

public class Article {
  private int id;
  private String url;
  private String title;
  private String content;
  private Publisher publisher;
  private Category category;
  private Date published_time;
  private Date update_time;
//  附件列表
  private ArrayList<Attach> attaches;

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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Publisher getPublisher() {
    return publisher;
  }

  public void setPublisher(Publisher publisher) {
    this.publisher = publisher;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public Date getPublished_time() {
    return published_time;
  }

  public void setPublished_time(Date published_time) {
    this.published_time = published_time;
  }

  public Date getUpdate_time() {
    return update_time;
  }

  public void setUpdate_time(Date update_time) {
    this.update_time = update_time;
  }

  public ArrayList<Attach> getAttaches() {
    return attaches;
  }

  public void setAttaches(ArrayList<Attach> attaches) {
    this.attaches = attaches;
  }

  public Article clone(){
    Article article = new Article();
    article.setId(id);
    article.setUrl(url);
    article.setTitle(title);
    article.setContent(content);
    article.setPublisher(publisher.clone());
    article.setCategory(category.clone());
    article.setPublished_time(published_time);
    article.setUpdate_time(update_time);

    ArrayList<Attach> attachelist = new ArrayList<Attach>();
    for(Attach attach: attaches){
      attachelist.add(attach.clone());
    }
    article.setAttaches(attachelist);
    return article;
  }
  @Override
  public String toString() {
    return "Article{" +
            "id=" + id +
            ", url='" + url + '\'' +
            ", title='" + title + '\'' +
            ", content='" + content + '\'' +
            ", publisher=" + publisher +
            ", category=" + category +
            ", published_time=" + published_time +
            ", update_time=" + update_time +
            ", attaches=" + (attaches == null? "[]": attaches) +
            '}';
  }
}
