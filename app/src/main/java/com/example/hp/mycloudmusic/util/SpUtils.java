package com.example.hp.mycloudmusic.util;

import android.content.Context;
import android.preference.PreferenceManager;

import com.example.hp.mycloudmusic.CMApplication;

public class SpUtils {

    public static final String FILTER_SIZE = "filter_size";
    public static final String FILTER_TIME = "filter_time";

    public static String getLocalFilterSize() {
        return getStringSharePreference(CMApplication.getApplication(),FILTER_SIZE,"0");
    }

    public static String getLocalFilterTime() {
        return getStringSharePreference(CMApplication.getApplication(),FILTER_TIME,"0");
    }

    /**--------------------------------------------------------------------------------------------------*/
    private static String getStringSharePreference(Context context, String key,String def) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key,def);
    }
}
