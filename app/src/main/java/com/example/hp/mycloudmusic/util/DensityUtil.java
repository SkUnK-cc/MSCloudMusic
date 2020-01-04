package com.example.hp.mycloudmusic.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;

public class DensityUtil {
    public static final String TAG = "DensityUtil";

    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    public static void setCustomDensity(Activity activity, @NonNull final Application application, int widthDp){
        final DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();

        if(sNoncompatDensity == 0){
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if(newConfig != null && newConfig.fontScale > 0){
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }
                @Override
                public void onLowMemory() {
                }
            });
        }

        float width = appDisplayMetrics.widthPixels;

        float targetDensity = width / widthDp;
        float targetScaledDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        int targetDensityDpi = (int) (160 * targetDensity);

        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;


        DisplayMetrics activieyDisplayMetrics = activity.getResources().getDisplayMetrics();
        activieyDisplayMetrics.density = width / widthDp;
        activieyDisplayMetrics.scaledDensity = targetScaledDensity;
        activieyDisplayMetrics.densityDpi = (int) (activieyDisplayMetrics.density*160);
    }
    public static void autoFit(Activity activity,boolean isPxEqualsDp){
        float width = 750;//todo 手动设置为设计图的宽,适配将根据宽为基准,也可以设置高,但是推荐设置宽,如果不需要px=dp则不设置也行
        int dpi = 375;//todo 手动设置设计图的dpi,不知道可以设计图的宽除2测试一下
        float nativeWidth = 0;//真实屏幕的宽,不需要手动改

        nativeWidth = activity.getWindowManager().getDefaultDisplay().getWidth();
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        displayMetrics.density = nativeWidth / 375;
        displayMetrics.densityDpi = (int) (displayMetrics.density*160);
    }
}
