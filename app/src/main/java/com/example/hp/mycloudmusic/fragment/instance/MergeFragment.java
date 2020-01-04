package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.merge.TabFragmentPagerAdapter;
import com.example.hp.mycloudmusic.fragment.presenter.MergePresenter;
import com.example.hp.mycloudmusic.fragment.view.IMergeView;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryMergeResp;
import com.example.hp.mycloudmusic.musicInfo.merge.QueryResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class MergeFragment extends BaseFragment<MergePresenter> implements IMergeView, RadioGroup.OnCheckedChangeListener {
    public static final String TAG = "MergeFragment";
    public static final String SEARCH_WORD = "Search_Word";

    @Bind(R.id.merge_pager)
    ViewPager viewPager;
    @Bind(R.id.iv_loading)
    ImageView ivLoading;
    @Bind(R.id.merge_radio_group)
    RadioGroup rgMerge;

    List<Fragment> fragmentList;
    QueryMergeResp queryMergeResp;
    private TabFragmentPagerAdapter adapter;

    MergeSongFragment mergeSongFragment;
    MergeArtistFragment mergeArtistFragment;
    MergeAlbumFragment mergeAlbumFragment;

    private String search_word;

    @Override
    protected int getContentView() {
        return R.layout.merge_fragment;
    }

    public MergeFragment() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new MergePresenter(liteOrm);
        mPresenter.attach(this);
        super.onViewCreated(view, savedInstanceState);
    }

    public static MergeFragment newInstance(String word) {
        MergeFragment fragment = new MergeFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_WORD, word);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initData() {
        updateData();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        Log.e(TAG, "initView: 执行");
        rgMerge.check(R.id.merge_single);
        viewPager.addOnPageChangeListener(new MyPagerChangeListener());

        fragmentList = new ArrayList<>();
        mergeSongFragment = MergeSongFragment.newInstance();
        mergeArtistFragment = MergeArtistFragment.newInstance();
        mergeAlbumFragment = MergeAlbumFragment.newInstance();
        fragmentList.add(mergeSongFragment);
        fragmentList.add(mergeArtistFragment);
        fragmentList.add(mergeAlbumFragment);
        adapter = new TabFragmentPagerAdapter(getChildFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(0);

        rgMerge.setOnCheckedChangeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 首次创建或点击搜索时更新关键字，获取数据
     */
    public void updateData() {
        search_word = getArguments().getString(SEARCH_WORD);
        Log.e(TAG, "initData: search_word:"+search_word);
//        mPresenter.getMergeData(search_word);
        mPresenter.getMergeData2(search_word)
        .subscribe(queryMergeResp1 -> {
            showMergeData(queryMergeResp1);
        });
    }

    /**
     * 请求得到数据后的回调函数
     * @param res
     */
    @Override
    public void showMergeData(QueryMergeResp res) {
        this.queryMergeResp = res;
        ivLoading.setVisibility(View.GONE);
        QueryResult queryResult = res.result;
        mergeSongFragment.setData(queryResult.getSong_info());
        mergeArtistFragment.setData(queryResult.getArtist_info());
        mergeAlbumFragment.setData(queryResult.getAlbum_info());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.merge_single:
                viewPager.setCurrentItem(0);
                break;
            case R.id.merge_artist:
                viewPager.setCurrentItem(1);
                break;
            case R.id.merge_album:
                viewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }
}
