package com.example.hp.mycloudmusic.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.mycloudmusic.util.GlideImageLoader;

public class BaseRecyclerHolder extends RecyclerView.ViewHolder {

    private Fragment fragment;
    private SparseArray<View> views;
    private Context context;

    private BaseRecyclerHolder(Context context, View itemView) {
        super(itemView);
        this.context = context;
        views = new SparseArray<>(8);
    }

    public static BaseRecyclerHolder getRecyclerHolder(Context context,View itemView){
        return new BaseRecyclerHolder(context,itemView);
    }

    public SparseArray<View> getViews(){return this.views;}

    public <T extends View> T getView(int viewId){
        View view = views.get(viewId);
        if(view == null){
            view = itemView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T) view;
    }

    public BaseRecyclerHolder setText(int viewId,String text){
        TextView tv = getView(viewId);
        if(text.contains("<em>") || text.contains("</em>")){
            text = text.replace("<em>","");
            text = text.replace("</em>","");
        }
        if(text == null){
            tv.setVisibility(View.GONE);
        }else{
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
        }
        return this;
    }

    public BaseRecyclerHolder setImageByUrl(int viewId,String url){
        ImageView iv  = getView(viewId);
        GlideImageLoader.loadImage(context,url,iv);
        return this;
    }
}
