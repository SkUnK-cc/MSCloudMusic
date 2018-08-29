package com.example.hp.mycloudmusic.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter;
import com.example.hp.mycloudmusic.mvp.view.IBaseView;
import com.example.hp.mycloudmusic.service.PlayService;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends BasePresenter> extends FragmentActivity implements IBaseView{
    public static final String TAG = "BaseActivity";
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate: "+getClass().getName());
        setContentView(getContentView());
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            //int option = View.SYSTEM_UI_FLAG_VISIBLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.parseColor("#9C27B0"));
            getWindow().setStatusBarColor(Color.TRANSPARENT);//设置状态栏的背景色
        }
        initView();
        initListener();
        initData();
    }

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

    @Override
    protected void onStart() {
        super.onStart();
    //        Log.e(TAG, "--------------------------------------------onStart-----" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "--------------------------------------------onResume-------");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.e(TAG, "--------------------------------------------onPause----");
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop: "+getClass().getName());
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "onDestroy: "+getClass().getName());
        super.onDestroy();
    }
}
