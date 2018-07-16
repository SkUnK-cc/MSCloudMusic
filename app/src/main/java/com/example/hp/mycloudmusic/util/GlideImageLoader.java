package com.example.hp.mycloudmusic.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class GlideImageLoader {
    public static void loadImage(Context context, String url, ImageView imageView){
        Glide.with(context)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
    public static void loadImage(Fragment fragment,String url,ImageView imageView){
        Glide.with(fragment)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(imageView);
    }
}
