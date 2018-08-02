package com.example.hp.mycloudmusic.adapter.recyclerview.search_fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonAdapter;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonViewHolder;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;

import java.util.List;

public class SearchAdapter extends CommonAdapter<AudioBean> {


    private OnItemClickListener mItemClickListener;

    public SearchAdapter(Context context,int layoutId){
        super(context,layoutId);
    }

    public SearchAdapter(Context context, int layoutId, List<AudioBean> datas) {
        super(context, layoutId, datas);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CommonViewHolder.get(mContext,null,parent,viewType,-1);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, final int position) {
        if(mItemClickListener != null){
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemClickListener != null && view != null && recyclerView != null){
                    int pos = recyclerView.getChildAdapterPosition(view);
                    if(mDatas.get(pos).getViewId() == R.layout.item_search_two){
                        mItemClickListener.onItemThreeClick(recyclerView,view,position);
                    }else if (mDatas.get(pos).getViewId() == R.layout.item_search_three){
                        mItemClickListener.onItemTwoClick(recyclerView,view,position);
                    }
                }
            }
        });
        convert(holder,mDatas.get(position));
    }

    @Override
    public void convert(CommonViewHolder holder, AudioBean audioBean) {
        Bitmap bitmap;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = 6;
        switch (audioBean.getViewId()){
            case R.layout.item_search_three:
                holder.setText(R.id.tv_title,audioBean.getTitle());
                holder.setText(R.id.tv_describe,audioBean.getArtist());
                bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.taylor_swift,op);
                BitmapDrawable bd = new BitmapDrawable(mContext.getResources(),bitmap);
                holder.setImageDrawable(R.id.iv_image,bd);
                break;
            case R.layout.item_search_two:
                holder.setText(R.id.tv_title,audioBean.getTitle());
                holder.setText(R.id.tv_describe,audioBean.getArtist());
                bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.taylor_swift,op);
                holder.setImageBitmap(R.id.iv_image,bitmap);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDatas.get(position).getViewId();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof NoScrollGridLayoutManager){
            final NoScrollGridLayoutManager gridManager = (NoScrollGridLayoutManager) layoutManager;
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int size ;
                    switch (getItemViewType(position)){
                        case R.layout.item_search_three:
                            size = 2;
                            break;
                        case R.layout.item_search_two:
                            size = 3;
                            break;
                        default:
                            size = 6;
                            break;
                    }
                    return size;
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(RecyclerView parent, View view, int position);
        void onTitleClick(RecyclerView parent,View view ,int position);
        void onItemThreeClick(RecyclerView parent,View view,int position);
        void onItemTwoClick(RecyclerView parent,View view,int position);
    }
}
