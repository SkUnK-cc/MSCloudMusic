package com.example.hp.mycloudmusic.util;

import com.litesuits.orm.LiteOrm;

public class SingletonUtils {
    private static SingletonUtils singletonUtils;
    private LiteOrm liteOrm;

    public SingletonUtils() {
    }

    public static SingletonUtils getInstance(){
        if(singletonUtils == null){
            synchronized (SingletonUtils.class){
                if(singletonUtils==null){
                    singletonUtils = new SingletonUtils();
                }
            }
        }
        return singletonUtils;
    }
}
