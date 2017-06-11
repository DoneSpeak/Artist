package com.artist.main;

import com.artist.utils.Shower;

import java.util.Date;

/**
 * Created by DoneSpeak on 2017/6/10.
 */
public class TestMain {
    public static void main(String[] args){
        String nullStr = null;
        Date one = new Date();
        Date two = new Date();
        Shower.printf("equals",one.equals(two));
        Shower.printf("=",one == two);
        Shower.printf("tolowercase","观荣 guanRong".toLowerCase());
        try{
            nullStr.length();
        }catch(Exception e){
            System.out.println(e.toString());
        }
    }
}
