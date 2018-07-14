package com.example.hp.mycloudmusic;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.example.hp.mycloudmusic.injection.component.AppComponent;
import com.example.hp.mycloudmusic.injection.component.DaggerAppComponent;
import com.example.hp.mycloudmusic.injection.module.AppModule;
import com.litesuits.orm.LiteOrm;

/**
 * note:
 * 继承Application时应在AndroidManifest文件中修改application的name
 */
public class CMApplication extends Application {

    public static final String TAG = "Application";
    public static final String DB_NAME = "MyCloudMusic.db";
    public static Context mContext;
    public static Application mApplication;
    private static AppComponent mAppComponent;
    private static LiteOrm liteOrm;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        mContext = this;
        mApplication = this;
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static Context getAppContext(){
        return mContext;
    }

    public static Application getApplication(){
        return mApplication;
    }

    public static AppComponent getAppComponent(){
        return mAppComponent;
    }

    public static LiteOrm provideLiteOrm(){
        if(liteOrm == null){
            synchronized(CMApplication.class){
                if(liteOrm == null){
                    liteOrm = LiteOrm.newSingleInstance(mApplication,DB_NAME);
                }
            }
        }
        return liteOrm;
    }
}
