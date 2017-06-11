package com.artist.dao;

import com.artist.model.Category;

/**
 * Created by DoneSpeak on 2017/6/5.
 */
public interface CategoryDao extends Dao<Category>{
    public Category getByName(String name) throws Exception;
    public int getIdByName(String name) throws Exception;
}
