package com.example.hp.mycloudmusic.fragment.instance;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.injection.component.AppComponent;
import com.example.hp.mycloudmusic.injection.component.DaggerActivityComponent;

public class MeFragment extends BaseFragment {


    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected int getContentView() {
        return R.layout.me_fragment;
    }
}
