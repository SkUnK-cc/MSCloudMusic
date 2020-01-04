package com.example.hp.mycloudmusic.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter;
import com.example.hp.mycloudmusic.mvp.view.IBaseView;
import com.example.hp.mycloudmusic.service.PlayService;
import com.example.hp.mycloudmusic.util.DevUtil;

import butterknife.ButterKnife;

public abstract class BaseActivity<P extends BasePresenter> extends FragmentActivity implements IBaseView{
    public static final String TAG = "BaseActivity";
    protected P mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DevUtil.d(TAG, "onCreate: "+getClass().getName());
        doBeforeContentView();
        setContentView(getContentView());
        ButterKnife.bind(this);
        if(Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.parseColor("#9C27B0"));
            getWindow().setStatusBarColor(Color.TRANSPARENT);//设置状态栏的背景色
        }
        initPresenter();
        if(mPresenter!=null) {
            mPresenter.attach(this);
        }
        initView();
        initListener();
        initData();
    }

    protected void doBeforeContentView(){

    }

    //通过该方法由子类实例具体的presenter，父类无法实例具体的presenter
    protected abstract void initPresenter();

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
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mPresenter!=null){
            mPresenter.detach();
        }
        super.onDestroy();
    }
}
