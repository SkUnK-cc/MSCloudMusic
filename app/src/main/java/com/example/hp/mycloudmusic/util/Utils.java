package com.example.hp.mycloudmusic.util;

import android.app.Application;

public class Utils {
    public static Application sApplication;

    public static void Utils(final Application app){
        Utils.sApplication = app;
    }

    public static Application getApp(){
        if(sApplication!=null)
            return sApplication;
        throw new NullPointerException("sApplication is null!");
    }
}
