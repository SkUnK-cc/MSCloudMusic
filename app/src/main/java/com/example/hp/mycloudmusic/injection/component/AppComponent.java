package com.example.hp.mycloudmusic.injection.component;

import com.example.hp.mycloudmusic.injection.module.AppModule;
import com.litesuits.orm.LiteOrm;

import javax.inject.Singleton;

import dagger.Component;

/**
 * 父component要显示的写出需要暴露可提供给子component的依赖
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    LiteOrm getLiteOrm();
}
