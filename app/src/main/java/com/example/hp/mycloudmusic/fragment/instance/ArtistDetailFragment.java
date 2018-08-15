package com.example.hp.mycloudmusic.fragment.instance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.merge.TabFragmentPagerAdapter;
import com.example.hp.mycloudmusic.custom.SimpleViewPagerIndicator;
import com.example.hp.mycloudmusic.custom.StickNavLayout;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist;
import com.example.hp.mycloudmusic.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class ArtistDetailFragment extends BaseFragment implements StickNavLayout.MyStickyListener, SimpleViewPagerIndicator.IndicatorClickListener, View.OnClickListener {
    public static final String ARTIST = "ARTIST";
    public static final String[] titles = new String[]{"单曲","专辑","MV","歌手信息"};

    @Bind(R.id.id_stickynavlayout)
    StickNavLayout mStickNavLayout;
    @Bind(R.id.id_stickynavlayout_avatar)
    ImageView iv_avatar;
    @Bind(R.id.id_stickynavlayout_indicator)
    SimpleViewPagerIndicator mIndicator;
    @Bind(R.id.id_stickynavlayout_viewpager)
    ViewPager mViewPager;
    @Bind(R.id.search_back)
    ImageView ivBack;

    private TabFragmentPagerAdapter mAdapter;

    private Artist artist;

    private List<Fragment> mFragments = new ArrayList<>();

    public static ArtistDetailFragment newInstance(Artist artist){
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARTIST,artist);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected int getContentView() {
        return R.layout.activity_artist_detail;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(this);

        artist = getArguments().getParcelable(ARTIST);
        mIndicator.setIndicatorClickListener(this);
        mIndicator.setTitles(titles);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mIndicator.scroll(position,positionOffset);
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        for(int i=0;i<titles.length;i++){
//            mFragments.add(ADetailSongFragment.newInstance(artist));
            mFragments.add(ADSongListFragment.Companion.newInstance(artist));
        }
        mAdapter = new TabFragmentPagerAdapter(getActivity().getSupportFragmentManager(),mFragments);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setCurrentItem(0);

        mStickNavLayout.setScrollListener(this);

        int height = DisplayUtil.getScreenHeight(getContext())-DisplayUtil.dip2px(getContext(),65)-DisplayUtil.dip2px(getContext(),40);
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) mViewPager.getLayoutParams();
        layoutParams.height = height;
        mViewPager.setLayoutParams(layoutParams);
    }


    private float getMobileWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    @Override
    public void imageScale(float bottom) {
        float height = DisplayUtil.dip2px(getContext(),220);
        float mScale = bottom/height;
        float width = getMobileWidth()*mScale;
        float dx = (width-getMobileWidth())/2;
        iv_avatar.layout((int)(0-dx),0,(int)(getMobileWidth()+dx),(int)bottom);
    }

    @Override
    public void setAlpha(float alpha) {

    }

    @Override
    public void onClickItem(int i) {
        mViewPager.setCurrentItem(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_back:
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.hide(this);
                transaction.commit();
                break;
            default:
                break;
        }
    }
}
