package com.artist.model;

import org.springframework.stereotype.Component;

@Component
public class Category {
  private int id;
  private String name;

  public Category(){}

  public Category(int id, String name){
    this.id = id;
    this.name = name;
  }

  public Category(String name){
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Category clone(){
    Category category = new Category();
    category.setId(id);
    category.setName(name);
    return category;
  }

  @Override
  public String toString() {
    return "{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
  }
}
