package com.example.hp.mycloudmusic.adapter.recyclerview.search_fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonAdapter;
import com.example.hp.mycloudmusic.adapter.recyclerview.CommonViewHolder;
import com.example.hp.mycloudmusic.musicInfo.mv.FirstMv;
import com.example.hp.mycloudmusic.ui.mv.PlayMvActivity;

import java.util.List;

public class SearchAdapter extends CommonAdapter<FirstMv> {

    /**
     * three 每行三个
     * two   每行两个
     */

    private OnItemClickListener mItemClickListener;

    public SearchAdapter(Context context,int layoutId){
        super(context,layoutId);
    }

    public SearchAdapter(Context context, int layoutId, List<FirstMv> datas) {
        super(context, layoutId, datas);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mItemClickListener = onItemClickListener;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int itemLayout = viewType==0?R.layout.item_search_two:R.layout.item_search_three;
        return CommonViewHolder.get(mContext,null,parent,itemLayout,-1);
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, final int position) {
        if(mItemClickListener != null){
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mItemClickListener != null && view != null && recyclerView != null){
                    Intent intent = new Intent(mContext,PlayMvActivity.class);
                    intent.putExtra("mvid",mDatas.get(position).getId());
                    mContext.startActivity(intent);
                }
            }
        });
        convert(holder,mDatas.get(position));
    }

    @Override
    public void convert(CommonViewHolder holder, FirstMv firstMv) {
        Bitmap bitmap;
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inSampleSize = 6;
        int type = getItemViewType(mDatas.indexOf(firstMv));
        switch (type){
            case 0:
                holder.setText(R.id.tv_title,firstMv.getName());
                holder.setText(R.id.tv_describe,firstMv.getArtistName());
                Glide.with(mContext)
                        .load(firstMv.getCover())
                        .asBitmap()
                        .into((ImageView) holder.getView(R.id.iv_image));
//                bitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.taylor_swift,op);
//                BitmapDrawable bd = new BitmapDrawable(mContext.getResources(),bitmap);
//                holder.setImageDrawable(R.id.iv_image,bd);
                break;
            case 1:
                holder.setText(R.id.tv_title,firstMv.getName());
                holder.setText(R.id.tv_describe,firstMv.getArtistName());
                Glide.with(mContext)
                        .load(firstMv.getCover())
                        .asBitmap()
                        .into((ImageView) holder.getView(R.id.iv_image));
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position<=3){
            return 0;
        }else{
            return 1;
        }
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
                        case 0:
                            size = 3;
                            break;
                        case 1:
                            size = 2;
                            break;
                        default:
                            size = 0;
                            break;
                    }
                    Log.e("SearchAdapter", "getSpanSize: "+size);
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
