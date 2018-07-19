package com.example.hp.mycloudmusic.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter;
import com.example.hp.mycloudmusic.mvp.view.IBaseView;
import com.example.hp.mycloudmusic.service.PlayService;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IBaseView{

    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            //int option = View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.parseColor("#9C27B0"));
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initView();
        initListener();
        initData();
    }

//    protected abstract void setupActivityComponent(AppComponent appComponent);

    public PlayService getPlayService(){
        PlayService playService = BaseAppHelper.get().getPlayService();
        if(playService == null){
            throw new NullPointerException(" play service is null!");
        }
        return playService;
    }


    protected abstract void initData();

    protected abstract void initListener();

    protected abstract void initView();

    protected abstract int getContentView();

//    public LiteOrm getLiteOrm(){
//        return mLiteOrm;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
