package com.example.hp.mycloudmusic.util;

import java.util.Formatter;
import java.util.Locale;

public class PlayerFormatUtils {
    public static String formatTime(long millionSeconds){
        if(millionSeconds<=0 || millionSeconds>=24*60*60*1000){
            return "00:00";
        }
        long totalSecond = (int) (millionSeconds / 1000);
        long second = totalSecond % 60;
        long minute = (totalSecond / 60) % 60;
        long hour = totalSecond/3600 ;
        StringBuilder builder = new StringBuilder();
        Formatter formatter = new Formatter(builder, Locale.getDefault());
        if(hour>0){
            return formatter.format("%d:%02d:%02d",hour,minute,second).toString();
        }else{
            return formatter.format("%02d:%02d",minute,second).toString();
        }

    }
}
