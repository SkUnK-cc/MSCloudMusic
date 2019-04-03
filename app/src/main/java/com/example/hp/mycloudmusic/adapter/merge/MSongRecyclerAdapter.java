package com.example.hp.mycloudmusic.adapter.merge;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.musicInfo.merge.Song;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MSongRecyclerAdapter extends RecyclerView.Adapter<MSongRecyclerAdapter.MSViewHolder> {

    private Context mContext;
    private List<Song> list;        //该list应该提前初始化

    private OnMergeSongClickListener listener;

    public MSongRecyclerAdapter(Context context){
        this.mContext = context;
        list = new ArrayList<>();
    }

    @NonNull
    @Override
    public MSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.merge_song_item,parent,false);
        MSViewHolder viewHolder = new MSViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MSViewHolder holder, int position) {
        final Song song = list.get(position);
        holder.title.setText(song.getTitle());
        holder.artist.setText(song.getArtist());
        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickItem(position);
            }
        });
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu(song,v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MSViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout info;
        TextView title;
        TextView artist;
        ImageView more;
        public MSViewHolder(View itemView) {
            super(itemView);
            info = itemView.findViewById(R.id.merge_song_info);
            title = itemView.findViewById(R.id.merge_song_title);
            artist = itemView.findViewById(R.id.merge_song_artist);
            more = itemView.findViewById(R.id.merge_song_more);
        }
    }

    private void showPopMenu(final Song song, View anchor){
        PopupMenu popupMenu = new PopupMenu(mContext,anchor);
        popupMenu.getMenuInflater().inflate(R.menu.merge_song_pop_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(listener != null){
                    listener.onClickMenuItem(item.getItemId(),song);
                }
                return false;
            }
        });
        makePopForceShowIcon(popupMenu);
        popupMenu.show();
    }

    @SuppressLint("RestrictedApi")
    private void makePopForceShowIcon(PopupMenu popupMenu) {
        Field mFieldPopup = null;
        try {
            mFieldPopup = popupMenu.getClass().getDeclaredField("mPopup");
            mFieldPopup.setAccessible(true);
            MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popupMenu);
            mPopup.setForceShowIcon(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    public interface OnMergeSongClickListener{
        void onClickMenuItem(int itemId,Song song);
        void onClickItem(int position);
    }

    public void setOnClickListener(OnMergeSongClickListener listener){
        this.listener = listener;
    }

    public List<Song> getData(){
        return list;
    }

    public void updateData(List<Song> newList){
        if(newList==null){
            list.clear();
        }else {
            this.list = newList;
        }
    }
}
