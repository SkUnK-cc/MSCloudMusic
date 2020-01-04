package com.example.hp.mycloudmusic.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.hp.mycloudmusic.CMApplication;

public class SpUtils {

    public static final String FILTER_SIZE = "filter_size";
    public static final String FILTER_TIME = "filter_time";
    public static final String REMAIND_BUFFER_FLOW = "remaind_buffer_flow";

    // 扫描音乐时，过滤文件小于 FILTER_SIZE 的文件
    public static String getLocalFilterSize() {
        return getStringSharePreference(CMApplication.getApplication(),FILTER_SIZE,"0");
    }

    // 扫描音乐时，过滤音乐时长小于 FILTER_TIME 的文件
    public static String getLocalFilterTime() {
        return getStringSharePreference(CMApplication.getApplication(),FILTER_TIME,"0");
    }

    // 获取今天剩余的音乐缓存流量(默认10MB)
    public static long getTodayBufferRemaindFlow(){
        return getLongSharePreference(CMApplication.getApplication(),REMAIND_BUFFER_FLOW,10*1024*1024);
    }
    public static void setTodayBufferRemaindFlow(long value){
        setLongSharePreference(CMApplication.getApplication(),REMAIND_BUFFER_FLOW,value);
    }

    /**--------------------------------------------------------------------------------------------------*/
    private static String getStringSharePreference(Context context, String key,String def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key,def);
    }
    private static long getLongSharePreference(Context context, String key, long def){
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(key,def);
    }
    private static void setLongSharePreference(Context context, String key, long value){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(key,value).apply();
    }
}
