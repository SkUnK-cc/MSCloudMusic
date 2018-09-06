package com.example.hp.mycloudmusic.fragment.instance;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.mycloudmusic.CMApplication;
import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.fragment.callback.ClickListener;
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter;
import com.example.hp.mycloudmusic.mvp.view.IBaseView;
import com.example.hp.mycloudmusic.service.PlayService;
import com.litesuits.orm.LiteOrm;

import butterknife.ButterKnife;

/**
 * 由于存储在FragmentManager中，fragment生命周期只执行一次
 * @param <P>
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment implements IBaseView {

    protected LiteOrm liteOrm;

    private static final String TAG = "BaseFragment";

    protected P mPresenter;
    protected ClickListener activityListener;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(),container,false);
        ButterKnife.bind(this,view);
        liteOrm = CMApplication.provideLiteOrm();
        Log.e(TAG, getClass().getName()+" : onCreateView");
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    /**
     * 应在onCreateView执行之后对控件进行初始化,onCreateView在onCreate之后执行
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view. This gives subclasses a chance
     * to initialize themselves once they know their view hierarchy has been completely created.
     * The fragment's view hierarchy is not however attached to its parent at this point.
     */
    //viewpager自动销毁，执行生命周期到onDestroyView，之后回到onCreateView -> onViewCreate
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e(TAG, getClass().getName()+" : onViewCreated");
//        setupActivityComponent(CMApplication.getAppComponent());
        if(mPresenter != null){
            mPresenter.attach(this);
        }
        initView();
        initListener();
        initData();
    }

    //    protected abstract void setupActivityComponent(AppComponent appComponent);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e(TAG, getClass().getName()+" : onActivityCreate");
    }

    public PlayService getPlayService(){
        PlayService playService = BaseAppHelper.get().getPlayService();
        if(playService == null){
            throw new NullPointerException("play service is null!");
        }
        return playService;
    }

    protected void addOrShowFragmentOnActivity(int layoutId, Fragment fragment, int enterAnim){
        if(fragment == null){
            return;
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim,0);
        if(!fragment.isAdded()){
            transaction.add(layoutId,fragment);
        }else if(fragment.isHidden()){
            transaction.show(fragment);
        }
        transaction.commit();
    }
    protected void hideFragmentOnActivity(Fragment fragment, int exitAnim){
        if(fragment == null){
            return;
        }
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(0, exitAnim);
        transaction.hide(fragment).commit();
    }

    protected void addOrShowFragmentOnFragment(int layoutId, Fragment fragment, int enterAnim){
        if(fragment == null){
            return;
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(enterAnim,0);
        if(!fragment.isAdded()){
            transaction.add(layoutId,fragment);
        }else if(fragment.isHidden()){
            transaction.show(fragment);
        }
        transaction.commit();
    }
    protected void hideFragmentOnFragment(Fragment fragment, int exitAnim){
        if(fragment == null){
            return;
        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.setCustomAnimations(0, exitAnim);
        transaction.hide(fragment).commit();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e(TAG, getClass().getName()+" : onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, getClass().getName() + ": onCreate");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, getClass().getName()+" : onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, getClass().getName()+" : onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e(TAG, getClass().getName()+":onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e(TAG, getClass().getName()+" : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e(TAG, getClass().getName()+" : onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null) {
            mPresenter.detach();
        }
        ButterKnife.unbind(this);
        Log.e(TAG, getClass().getName()+" : onDestroy");
    }

    public void startActivity(Class<?> cls){
        startActivity(cls,null);
    }

    private void startActivity(Class<?> cls,Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(getActivity(),cls);
        if(bundle != null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void setClickListener(ClickListener listener){
        this.activityListener = listener;
    }

    protected abstract void initData();

    protected abstract void initListener();

    protected abstract void initView();

    protected abstract int getContentView();
}
