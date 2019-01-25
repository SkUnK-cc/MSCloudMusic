package com.example.hp.mycloudmusic.util;

import android.util.Log;

import com.example.hp.mycloudmusic.BuildConfig;

public class DevUtil {
    private static final boolean isDebug = BuildConfig.DEBUG;

    private DevUtil(){
    }

    public static void d(String tag,String msg){
        if(isDebug){
            Log.d(msg, " - tag:"+tag);
        }
    }
    public static void v(String tag,String msg){
        if(isDebug){
            Log.v(msg, " - tag:"+tag);
        }
    }
    public static void w(String tag,String msg){
        if(isDebug){
            Log.w(msg, " - tag:"+tag);
        }
    }
    public static void e(String tag,String msg){
        if(isDebug){
            Log.e(msg, " - tag:"+tag);
        }
    }

    public static boolean isDebug(){
        return isDebug;
    }
}
