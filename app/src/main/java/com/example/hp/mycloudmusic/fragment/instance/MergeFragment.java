package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.fragment.presenter.MergePresenter;
import com.example.hp.mycloudmusic.fragment.view.IMergeView;
import com.example.hp.mycloudmusic.injection.component.AppComponent;
import com.example.hp.mycloudmusic.injection.component.DaggerActivityComponent;

import butterknife.Bind;

public class MergeFragment extends BaseFragment<MergePresenter> implements IMergeView {
    public static final String SEARCH_WORD = "Search_Word";

    @Bind(R.id.merge_recycler)
    RecyclerView recyclerView;
    @Bind(R.id.iv_loading)
    ImageView ivLoading;
    @Bind(R.id.merge_radio_group)
    RadioGroup rgMerge;

    private String search_word;

    public MergeFragment() {

    }

    public static MergeFragment newInstance(String param1) {
        MergeFragment fragment = new MergeFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_WORD, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder()
                .appComponent(appComponent)
                .build()
                .inject(this);
    }

    @Override
    protected void initData() {
        search_word = getArguments().getString(SEARCH_WORD);
        mPresenter.getMergeData(search_word);
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
}
