package com.example.hp.mycloudmusic.ui.onLine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.merge.TabFragmentPagerAdapter;
import com.example.hp.mycloudmusic.custom.ArtistDetailScrollView;
import com.example.hp.mycloudmusic.fragment.instance.ADetailSongFragment;
import com.example.hp.mycloudmusic.ui.BaseActivity;
import com.example.hp.mycloudmusic.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public abstract class ArtistInfoActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ArtistDetailScrollView.MyOnScrollListener {
    public static final String TAG = "ArtistInfoActivity";
    public static final String UID = "UID";

    @Bind(R.id.artist_middle_avatar)
    ImageView iv_avatar;
    @Bind(R.id.artist_iv_front_avatar)
    ImageView iv_front;
    @Bind(R.id.artist_detail_bar_title)
    TextView tvTitle;
    @Bind(R.id.ll_detail)
    LinearLayout llDetail;
    @Bind(R.id.radio_group)
    RadioGroup radioGroup;
    @Bind(R.id.vp_activity_detail)
    ViewPager vpWorksList;
    @Bind(R.id.detail_scroll_view)
    ArtistDetailScrollView scrollView;

    private List<Fragment> fragmentList;
    private ADetailSongFragment aDetailSongFragment1;
    private ADetailSongFragment aDetailSongFragment2;
    private TabFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
    }

    private void updateData() {
//        List<Song> songList = new ArrayList<>();
//        for(int i=0;i<20;i++){
//            Song song = new Song();
//            song.setTitle("Song-"+i);
//            song.setAuthor("artist");
//            songList.add(song);
//        }
//        Song_info song_info = new Song_info();
//        song_info.setSong_list(songList);
//        aDetailSongFragment1.setData(song_info);
//        for(int i=0;i<10;i++){
//            Song song = new Song();
//            song.setTitle("Song-"+i);
//            song.setAuthor("artist");
//            songList.add(song);
//        }
//        Song_info song_info2 = new Song_info();
//        song_info2.setSong_list(songList);
//        aDetailSongFragment2.setData(song_info2);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        radioGroup.setOnCheckedChangeListener(this);
        tvTitle.setSelected(true);
        vpWorksList.addOnPageChangeListener(new OnDetailPageChangeListener());

        fragmentList = new ArrayList<>();
        aDetailSongFragment1 = ADetailSongFragment.newInstance(null);
        fragmentList.add(aDetailSongFragment1);
        aDetailSongFragment2 = ADetailSongFragment.newInstance(null);
        fragmentList.add(aDetailSongFragment2);
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);
        vpWorksList.setAdapter(adapter);
        vpWorksList.setCurrentItem(0);

        scrollView.setScrollListener(this);

        int height = DisplayUtil.getScreenHeight(ArtistInfoActivity.this)-DisplayUtil.dip2px(ArtistInfoActivity.this,65)-radioGroup.getHeight();
        LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) vpWorksList.getLayoutParams();
        layoutParams.height = 3000;
        vpWorksList.setLayoutParams(layoutParams);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_artist_info;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.radio_single:
                vpWorksList.setCurrentItem(0);
//                LinearLayout.LayoutParams layoutParams= (LinearLayout.LayoutParams) vpWorksList.getLayoutParams();
//                int height = aDetailSongFragment1.getHeight();
//                layoutParams.height = height;
//                vpWorksList.setLayoutParams(layoutParams);
//                Log.e(TAG, "onCheckedChanged: height = "+height);

                break;
            case R.id.radio_album:
                vpWorksList.setCurrentItem(1);
//                LinearLayout.LayoutParams layoutParams2= (LinearLayout.LayoutParams) vpWorksList.getLayoutParams();
//                int height2 = aDetailSongFragment2.getHeight();
//                layoutParams2.height = height2;
//                vpWorksList.setLayoutParams(layoutParams2);
//                Log.e(TAG, "onCheckedChanged: height = "+height2);
                break;
            case R.id.radio_mv:
                break;
        }

    }

    public static void toArtistInfoActivity(Context context,String uid){
        Intent intent = new Intent(context,ArtistInfoActivity.class);
        intent.putExtra(UID,uid);
        context.startActivity(intent);
    }

    public void setCheck(int i) {
        if(i==0) {
            radioGroup.check(R.id.radio_single);
        }
    }

    @Override
    public void llMoveTo(float margin) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) llDetail.getLayoutParams();
//        Log.e(TAG, "overDown: old margin = "+layoutParams.topMargin);
        layoutParams.topMargin = (int) margin;
//        Log.e(TAG, "overDown: new margin = "+layoutParams.topMargin);
        llDetail.setLayoutParams(layoutParams);
    }

    @Override
    public void imageScale(float margin) {
        changeImageSize(margin);
    }

    @Override
    public void setAlpha(float alpha) {
        iv_front.setAlpha(alpha);
    }

    private void changeImageSize(float margin) {
//        Log.e(TAG, "left="+iv_avatar.getLeft()+"\ntop="+iv_avatar.getTop()+"\nright="+iv_avatar.getRight()+"\nbottom="+iv_avatar.getBottom());
//        float finalY = margin + DisplayUtil.dip2px(this,65);
        float height = DisplayUtil.dip2px(ArtistInfoActivity.this,220);
        //Log.e(TAG, "changeImageSize: finalY="+finalY+"\nheight="+height);
        float mScale = margin/height;
        float width = getMobileWidth()*mScale;
        float dx = (width-getMobileWidth())/2;
//        Log.e(TAG, "-dx="+(-dx)+"\ntop="+0+"\nright="+(getMobileWidth()+dx)+"\nbottom="+finalY);
        iv_avatar.layout((int)(0-dx),0,(int)(getMobileWidth()+dx),(int)margin);
//        iv_avatar.requestLayout();
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_avatar.getLayoutParams();
//        layoutParams.width = (int) width;
//        layoutParams.height = (int) finalY;
//        iv_avatar.setLayoutParams(layoutParams);
//        Log.e(TAG, "left="+iv_avatar.getLeft()+"\ntop="+iv_avatar.getTop()+"\nright="+iv_avatar.getRight()+"\nbottom="+iv_avatar.getBottom());

    }

    private float getMobileWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        return width;
    }

    public class OnDetailPageChangeListener implements ViewPager.OnPageChangeListener {
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

    public ArtistDetailScrollView getScrollView(){
        return scrollView;
    }
    public static void get(){

    }
}
