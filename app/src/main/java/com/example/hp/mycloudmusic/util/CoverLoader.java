package com.example.hp.mycloudmusic.util;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;

import com.example.hp.mycloudmusic.CMApplication;
import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * 单例
 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
 * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
 * 静态初始化器，有JVM来保证线程安全
 */
public class CoverLoader {
    public static final String KEY_NULL = "null";

    public static final String TYPE_BLUR = "blur";

    private LruCache<String,Bitmap> mCoverCache;

    public static CoverLoader getInstance(){
        return SingletonHolder.coverLoader;
    }

    static class SingletonHolder{

        static CoverLoader coverLoader = new CoverLoader();
    }

    private CoverLoader(){
        int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        int cacheSize = maxMemory/8;
        mCoverCache = new LruCache<String, Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                    return  bitmap.getAllocationByteCount()/1024;
                }else{
                    return bitmap.getByteCount()/1024;
                }
            }
        };
    }

    /**
     * 获取蒙层透明背景bitmap
     * @param music
     * @return
     */
    public Bitmap loadBlur(AbstractMusic music) {
        return loadCover(music,TYPE_BLUR);
    }


    private Bitmap loadCover(AbstractMusic music, String type) {
        Bitmap bitmap;
        String key = getKey(music,type);
        if(TextUtils.isEmpty(key)){
            //是local或者online的图片,否则key为null，不执行此段代码
            bitmap = mCoverCache.get(KEY_NULL.concat(type));
            if(bitmap != null){
                return bitmap;
            }
            bitmap = getDefaultCover(type);
            mCoverCache.put(KEY_NULL.concat(type),bitmap);
            return bitmap;
        }
        bitmap = mCoverCache.get(key);
        if(bitmap != null){
            return bitmap;
        }
        bitmap = loadCoverByType(music,type);       //根据type从媒体库或者文件获取bitmap
        if(bitmap!=null){
            mCoverCache.put(key,bitmap);
            return bitmap;
        }
        return loadCover(null,type);
    }

    private Bitmap loadCoverByType(AbstractMusic music, String type) {
        Bitmap bitmap;
        if(music.getType() == AudioBean.TYPE_LOCAL ){
            AudioBean audioBean = (AudioBean) music;
            bitmap = loadCoverFromMediaStore(audioBean.getAlbumId());
        }else{
//            bitmap = loadCoverFromFile(music.getCoverPath());
            bitmap = null;
        }
        switch (type){
            case TYPE_BLUR:
                return ImageUtils.blur(bitmap);
            default:
                return bitmap;
        }
    }

    /**
     * 从文件获取封面
     * @param coverPath
     * @return
     */
    private Bitmap loadCoverFromFile(String coverPath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(coverPath,options);
    }

    /**
     * 从媒体库获取封面（ContentResolver）
     * @param albumId
     * @return
     */
    private Bitmap loadCoverFromMediaStore(long albumId) {
        ContentResolver resolver = CMApplication.getApplication().getContentResolver();
        Uri uri = FileMusicUtils.getMediaStoreAlbumCoverUri(albumId);
        InputStream is = null;
        try {
            is = resolver.openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeStream(is,null,options);
    }

    private Bitmap getDefaultCover(String type) {
        switch (type){
            case TYPE_BLUR:
                return BitmapFactory.decodeResource(CMApplication.getApplication().getResources(), R.drawable.default_cover);
            default:
                return BitmapFactory.decodeResource(CMApplication.getApplication().getResources(), R.drawable.default_cover);
        }
    }

    private String getKey(AbstractMusic music, String type) {
        if (music == null) {
            return null;
        }
        if (music.getType() == AbstractMusic.TYPE_LOCAL) {
            AudioBean audioBean = (AudioBean) music;
            if (audioBean.getAlbumId() > 0) {
                return String.valueOf(audioBean.getAlbumId()).concat(AbstractMusic.TYPE_LOCAL);
            }
        } else {
            return null;
        }
        return null;
    }
//        else if(music.getType()==AbstractMusic.TYPE_ONLINE && !TextUtils.isEmpty(music.getCoverPath())){
//            return music.getCoverPath().concat(AudioBean.TYPE_ONLINE);
//            return null;
//        }
//        else{
//            return null;
//        }
}
