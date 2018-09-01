package com.example.hp.mycloudmusic.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LocalMusicAdapter extends RecyclerView.Adapter<LocalMusicAdapter.MyHolder>{

    private ClickListener mItemListener;
    private List<AudioBean> mList = new ArrayList<>();

    public LocalMusicAdapter(){

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.local_music_recycler_item,parent,false);
        final MyHolder holder = new MyHolder(view);
        if(mItemListener != null){
            holder.audioItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.onLocalItemClick(holder.getAdapterPosition());
                }
            });
            holder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemListener.clickMore(holder.getAdapterPosition());
                }
            });

        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        AudioBean audioBean = mList.get(position);
        holder.title.setText(audioBean.getTitle());
        holder.artist.setText(audioBean.getArtist());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setData(List<AudioBean> list) {
        mList = list;
        notifyDataSetChanged();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        View audioItem;
        @Bind(R.id.local_music_title)
        TextView title;
        @Bind(R.id.local_music_artist)
        TextView artist;
        @Bind(R.id.local_music_more)
        ImageView more;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            audioItem = itemView;
        }
    }

    public void setOnItemClickListener(ClickListener listener){
        mItemListener = listener;
    }

    public interface ClickListener{
        void onClick(View view);
        void onLocalItemClick(int position);
        void clickMore(int position);
    }
}
