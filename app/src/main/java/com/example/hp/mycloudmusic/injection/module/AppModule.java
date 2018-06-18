package com.example.hp.mycloudmusic.injection.module;

import com.example.hp.mycloudmusic.CMApplication;
import com.litesuits.orm.LiteOrm;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * 看成一个普通的类，提供依赖
 */
@Module
public class AppModule {
    public static final String DB_NAME = "MyCloudMusic.db";
    private CMApplication application;

    public AppModule(CMApplication application){
        this.application = application;
    }

    @Provides
    @Singleton
    public LiteOrm provideLiteOrm(){
        LiteOrm liteOrm = LiteOrm.newSingleInstance(application,DB_NAME);
        return liteOrm;
    }
}
