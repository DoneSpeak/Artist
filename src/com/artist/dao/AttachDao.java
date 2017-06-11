package com.artist.dao;

import com.artist.model.Attach;

import java.util.ArrayList;

/**
 * Created by DoneSpeak on 2017/6/5.
 */
public interface AttachDao extends Dao<Attach> {
    public int addSome(ArrayList<Attach> attaches) throws Exception;
}
