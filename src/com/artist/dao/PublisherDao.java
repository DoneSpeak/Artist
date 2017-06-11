package com.artist.dao;

import com.artist.model.Publisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * Created by DoneSpeak on 2017/6/5.
 */
@Component
public interface PublisherDao extends Dao<Publisher> {
    public Publisher getByName(String name) throws Exception;
    public int getIdByName(String name) throws Exception;
    public int addSome(ArrayList<Publisher> names) throws Exception;
}
