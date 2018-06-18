package com.example.hp.mycloudmusic.injection.component;

import com.example.hp.mycloudmusic.fragment.instance.LocalFragment;
import com.example.hp.mycloudmusic.fragment.instance.MeFragment;
import com.example.hp.mycloudmusic.fragment.instance.MusicFragment;
import com.example.hp.mycloudmusic.fragment.instance.PlayMusicFragment;
import com.example.hp.mycloudmusic.fragment.instance.SearchFragment;
import com.example.hp.mycloudmusic.injection.ActivityScope;
import com.example.hp.mycloudmusic.ui.MainActivity;

import dagger.Component;

/**
 * 子component需要在注解中使用dependencies来连接父component
 */
@ActivityScope
@Component(dependencies = {AppComponent.class})
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(LocalFragment localFragment);
    void inject(MeFragment meFragment);
    void inject(MusicFragment musicFragment);
    void inject(PlayMusicFragment playMusicFragment);
    void inject(SearchFragment searchFragment);
}
