package com.artist.utils.parser;

import java.util.LinkedList;

/**
 * Created by DoneSpeak on 2017/6/4.
 * 数据结构队列
 *  实现了一个简单的队列，在 LinkDb.java 中使用了此类。
 */
public class Queue<T> {

    private LinkedList<T> queue=new LinkedList<T>();

    public void push(T t){
        queue.addLast(t);
    }

    public T pop(){
        return queue.removeFirst();
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }

    public boolean contians(T t){
        return queue.contains(t);
    }

    public void clear(){
        queue.clear();
    }

    public int size(){
        return queue.size();
    }
}