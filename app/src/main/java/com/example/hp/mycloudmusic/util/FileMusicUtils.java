package com.example.hp.mycloudmusic.util;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;

import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileMusicUtils {
    public static Uri getMediaStoreAlbumCoverUri(long albumId) {
        Uri uri = Uri.parse("content://media/external/audio/albumart");
        return ContentUris.withAppendedId(uri,albumId);
    }

    /**
     * 获取歌词路径<br>
     * 先从已下载文件夹中查找，如果不存在，则从歌曲文件所在文件夹查找。
     * @return 如果存在返回路径，否则返回null
     */
    public static String getLrcFilePath(AudioBean music) {
        if(music == null){
            return null;
        }
        //从下载文件夹中查找
        String lrcFilePath = getLrcDir()+getLrcFileName(music.getTitle(),music.getArtist());
        if(!exists(lrcFilePath)){
            //从歌曲文件所在文件夹查找
            lrcFilePath = music.getPath().replace(".mp3",".lrc");
            if(!exists(lrcFilePath)){
                lrcFilePath = null;
            }
        }
        return lrcFilePath;
    }

    public static File createLrcFile(AbstractMusic music){
        if(music == null){
            return null;
        }
        String lrcFile = getLrcDir()+getLrcFileName(music.getTitle(),music.getArtist());
        File file = new File(lrcFile);
        try {
            if(!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 判断文件是否存在
     * @param lrcFilePath
     * @return
     */
    private static boolean exists(String lrcFilePath) {
        File file = new File(lrcFilePath);
        return file.exists();
    }

    /**
     * 通过名称和人名组成文件名+后缀
     * @param artist
     * @param title
     * @return
     */
    public static String getLrcFileName(String title, String artist) {
        return getFileName(title,artist)+".lrc";
    }

    @NotNull
    public static String getLocalMusicName(@Nullable String title, @Nullable String artist) {
        return getFileName(title,artist)+".mp3";
    }

    private static String getFileName(String title, String artist) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if(TextUtils.isEmpty(artist)){
            artist = "未知";
        }
        if(TextUtils.isEmpty(title)){
            title = "未知";
        }
        return title+"-"+artist;
    }

    /**
     * 过滤特殊字符
     * @param fileName
     * @return
     */
    private static String stringFilter(String fileName) {
        if(fileName == null){
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(fileName);
        return matcher.replaceAll("").trim();
    }
    public static String getLrcDir() {
        String dir = getAppDir()+"/Lyric/";
        return mkdir(dir);
    }

    public static String getLocalMusicDir(){
        String dir = getAppDir()+"/LocalMusic/";
        return mkdir(dir);
    }

    private static String mkdir(String dir) {
        File file = new File(dir);
        if(!file.exists()){
            file.mkdirs();
        }
        return dir;
    }

    private static String getAppDir(){
        return Environment.getExternalStorageDirectory()+"/MyCloudMusic";
    }

    public static void saveLrcFile(String filePath, String result) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath,false));
            bw.write(result);
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
