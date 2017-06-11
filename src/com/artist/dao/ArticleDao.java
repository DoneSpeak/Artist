package com.artist.dao;

import com.artist.model.Article;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by DoneSpeak on 2017/6/4.
 */
public interface ArticleDao extends Dao<Article>{
//    RawOne 插入的article暂时没有id，需要插入才能获取
    public int addRawOne(Article article) throws Exception;
    public ArrayList<Article> addAllByCategoryId(int categoryId) throws Exception;
    public ArrayList<Article> getAllWithAbstractInIds(@Param("category") int category, @Param("publisher") int publisher, @Param("duration") int duration, @Param("docIds") int[] docIds) throws Exception;
    public ArrayList<Article> getAllWithAbstractForAttachIds(@Param("category") int category, @Param("publisher") int publisher, @Param("duration") int duration, @Param("docIds") int[] docIds) throws Exception;
    public ArrayList<Article> getTopK(int k) throws Exception;
    public ArrayList<Article> getTopKWithAbstract(int k) throws Exception;
    public ArrayList<Article> getAllWithAbstract() throws Exception;
    public ArrayList<Article> getAllWithAbstractMatched(@Param("category") int category, @Param("publisher") int publisher, @Param("duration") int duration, @Param("isForArticleNotAttach") boolean isForArticleNotAttach) throws Exception;
    public ArrayList<Article> getTopKWithAbstractLength(@Param("k") int k, @Param("length") int length) throws Exception;

}
