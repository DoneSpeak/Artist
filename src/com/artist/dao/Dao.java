package com.artist.dao;

import java.util.ArrayList;

/**
 * Created by DoneSpeak on 2017/5/17.
 */
public interface Dao<T> {
    //    获取单个对象
    public T getOne(int id) throws Exception;
    //    获取所有对象
    public ArrayList<T> getAll() throws Exception;
    //   插入一个对象，插入出错时，抛出错误，返回值为插入对象的id
    public int addOne(T obj) throws Exception;
    //   更新一个对象，id = obj.id
    public int updateOne(T obj) throws Exception;
    //   根据id删除一个对象，可以重载出其他方法
    public int deleteOne(int id) throws Exception;
}
