package com.artist.model;

public class Publisher {
  private int id;
  private String name;

  public Publisher(){}

  public Publisher(int id, String name){
    this.id = id;
    this.name = name;
  }

  public Publisher(String name){
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


  public Publisher clone(){
    Publisher publisher = new Publisher();
    publisher.setId(id);
    publisher.setName(name);
    return publisher;
  }

  @Override
  public String toString() {
    return "{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
  }
}
