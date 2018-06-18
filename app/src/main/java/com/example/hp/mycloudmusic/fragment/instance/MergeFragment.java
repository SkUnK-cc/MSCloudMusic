package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.injection.component.AppComponent;

public class MergeFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public MergeFragment() {

    }

    public static MergeFragment newInstance(String param1) {
        MergeFragment fragment = new MergeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

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
        return R.layout.merge_fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
