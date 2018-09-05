package com.example.hp.mycloudmusic.custom;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.listview.PopupAdapter;
import com.example.hp.mycloudmusic.adapter.listview.PopupItem;
import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.merge.Artist;
import com.example.hp.mycloudmusic.service.PlayService;
import com.example.hp.mycloudmusic.ui.onLine.ArtistDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class PopupWindowManager {
    private PopupWindow popupWindow;
    private View contentView;
    private Activity mActivity;
    private boolean hasMv;
    private boolean hasDelete;
    private boolean hasDownload;
    private int width;
    private int height;
    private PopupWindowListener listener;
    private AbstractMusic music;

    static int []resIds = null;
    static String[] titles = null;
    List<PopupItem> list = new ArrayList<>();

    PopupWindowListener defListener = new PopupWindowListener() {
        @Override
        public void onItemClick(int imageId, AbstractMusic music) {
            switch (imageId){
                case R.drawable.ic_icon_add_playback:
                    PlayService service = BaseAppHelper.get().getPlayService();
                    if(service!=null){
                        service.addNextPlayMusic(music);
                    }
                    break;
                case R.drawable.ic_icon_add_list:
                    break;
                case R.drawable.ic_icon_artist2:
                    Artist create = music.obtainArtist();
                    ArtistDetailActivity.toArtistDetailActivity(mActivity,create);
                    break;
                default:
                    break;
            }
            popupWindow.dismiss();
        }
    };

    public PopupWindowManager(){

    }
    public PopupWindowManager(Builder builder){
        this.mActivity = builder.activity;
        this.hasMv = builder.hasMv;
        this.hasDelete = builder.hasDelete;
        this.hasDownload = builder.hasDownload;
        this.width = builder.width;
        this.height = builder.height;
        this.listener = builder.listener;
        this.music = builder.music;

        this.contentView = LayoutInflater.from(mActivity).inflate(builder.contentviewid,null);
        initView(contentView);

        //initPopupWindow(builder);
    }

    private void initView(View contentView) {
        ListView listView = contentView.findViewById(R.id.pop_song_listview);
        initList();
        PopupAdapter adapter = new PopupAdapter(mActivity,R.layout.song_more_pop_item,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopupItem item = list.get(position);
                if(listener != null) {
                    listener.onItemClick(item.imageId, music);
                }else{
                    defListener.onItemClick(item.imageId,music);
                }
            }
        });
    }

    private void initList() {
        if(resIds == null || titles == null) {
            Log.e("popup window ", "init resIds and titles!!!");
            TypedArray ar = mActivity.getResources().obtainTypedArray(R.array.popup_images);
            int len = ar.length();
            resIds = new int[len];
            for (int i = 0; i < len; i++) {
                resIds[i] = ar.getResourceId(i, 0);
            }
            ar.recycle();
            titles = mActivity.getResources().getStringArray(R.array.popup_titles);
        }
        for (int i = 0; i < resIds.length; i++) {
            PopupItem item = checkItem(i);
            if(item == null)continue;
            list.add(item);
        }
    }

    private PopupItem checkItem(int i){
        PopupItem item = new PopupItem(resIds[i],titles[i]);
        switch (resIds[i]){
            case R.drawable.ic_icon_artist2:
                item.title = item.title.concat(music.getArtist());
                break;
            case R.drawable.ic_icon_video:
                if(hasMv == false)return null;
                break;
            case R.drawable.ic_icon_delete:
                if(hasDelete == false)return null;
                break;
            case R.drawable.ic_icon_album:
                if(music.getAlbumTitle() == null || music.getAlbumTitle().equals("")) {
                    return null;
                }
                item.title = item.title.concat(music.getAlbumTitle());
                break;
            case R.drawable.ic_icon_download:
                if(hasDownload == false)return null;
                break;
        }
        return item;
    }

    public PopupWindowManager showAtLocation(View anchor,int gravity,int x,int y){
        initPopupWindow();
        popOutShadow();
        popupWindow.showAtLocation(anchor,gravity,x,y);
        return this;
    }

    private void popOutShadow() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.7f;
        mActivity.getWindow().setAttributes(lp);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
                lp.alpha = 1f;
                mActivity.getWindow().setAttributes(lp);
            }
        });
    }

    public void initPopupWindow(){
        popupWindow = new PopupWindow(contentView,width,height);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
    }

    public static class Builder{
        private Activity activity;
        private int contentviewid;
        private int width;
        private int height;
        private boolean hasMv;
        private boolean hasDelete;
        private boolean hasDownload;
        private PopupWindowListener listener;
        private AbstractMusic music;
        public Builder(Activity activity,int contentviewid,int width,int height){
            this.activity = activity;
            this.contentviewid = contentviewid;
            this.width = width;
            this.height = height;
        }
        public Builder hasMv(boolean hasMv){
            this.hasMv = hasMv;
            return this;
        }
        public Builder hasDelete(boolean hasDelete){
            this.hasDelete = hasDelete;
            return this;
        }
        public Builder setListener(PopupWindowListener listener){
            this.listener = listener;
            return this;
        }
        public Builder setMusic(AbstractMusic music){
            this.music = music;
            return this;
        }
        public Builder hasDownload(boolean hasDownload){
            this.hasDownload = hasDownload;
            return this;
        }
        public PopupWindowManager build() {
            return new PopupWindowManager(this);
        }
    }

    public interface PopupWindowListener{
        void onItemClick(int imageId, AbstractMusic music);
    }
}
