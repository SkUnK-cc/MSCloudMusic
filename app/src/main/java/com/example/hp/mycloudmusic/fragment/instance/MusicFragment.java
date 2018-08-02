package com.example.hp.mycloudmusic.fragment.instance;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.hp.mycloudmusic.R;

import butterknife.Bind;

public class MusicFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = "MusicFragment";

    @Bind(R.id.to_local_music)
    RelativeLayout localmusic;
    @Bind(R.id.the_music_playing)
    ImageView iv_playing;

    @Override
    protected int getContentView() {
        return R.layout.mymusic_fragment;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        localmusic.setOnClickListener(this);
    }

    @Override
    protected void initView() {
        iv_playing.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        activityListener.onClick(v);
    }

}
