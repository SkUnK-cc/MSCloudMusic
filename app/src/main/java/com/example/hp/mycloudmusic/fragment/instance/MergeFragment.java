package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.fragment.presenter.MergePresenter;
import com.example.hp.mycloudmusic.fragment.view.IMergeView;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;

import butterknife.Bind;

public class MergeFragment extends BaseFragment<MergePresenter> implements IMergeView {
    public static final String TAG = "MergeFragment";
    public static final String SEARCH_WORD = "Search_Word";

    @Bind(R.id.merge_pager)
    ViewPager viewPager;
    @Bind(R.id.iv_loading)
    ImageView ivLoading;
    @Bind(R.id.merge_radio_group)
    RadioGroup rgMerge;

    QueryMergeResp queryMergeResp;

    private String search_word;

    public MergeFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new MergePresenter(liteOrm);
        mPresenter.attach(this);
        super.onViewCreated(view, savedInstanceState);
    }

    public static MergeFragment newInstance(String param1) {
        MergeFragment fragment = new MergeFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_WORD, param1);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerActivityComponent.builder()
//                .appComponent(appComponent)
//                .build()
//                .inject(this);
//    }

    @Override
    protected void initData() {
        updateData();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
    }

    @Override
    protected int getContentView() {
        return R.layout.merge_fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateData() {
        search_word = getArguments().getString(SEARCH_WORD);
        Log.e(TAG, "initData: search_word:"+search_word);
        mPresenter.getMergeData(search_word);
    }

    @Override
    public void showMergeData(QueryMergeResp res) {
        this.queryMergeResp = res;

    }
}
