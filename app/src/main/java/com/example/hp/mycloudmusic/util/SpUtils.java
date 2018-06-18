package com.example.hp.mycloudmusic.util;

import android.content.Context;
import android.preference.PreferenceManager;

public class SpUtils {

    public static final String FILTER_SIZE = "filter_size";
    public static final String FILTER_TIME = "filter_time";

    public static String getLocalFilterSize(Context context) {
        return getStringSharePreference(context,FILTER_SIZE,"0");
    }

    public static String getLocalFilterTime(Context context) {
        return getStringSharePreference(context,FILTER_TIME,"0");
    }

    /**--------------------------------------------------------------------------------------------------*/
    private static String getStringSharePreference(Context context, String key,String def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key,def);
    }
}
