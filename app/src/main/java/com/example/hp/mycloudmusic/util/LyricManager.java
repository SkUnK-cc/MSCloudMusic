package com.example.hp.mycloudmusic.util;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.example.hp.mycloudmusic.fragment.callback.OnProgressChangedListener;
import com.example.hp.mycloudmusic.musicInfo.lyric.LineInfo;
import com.example.hp.mycloudmusic.musicInfo.lyric.LyricInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class LyricManager {
    private static final String TAG = "LyricManager";

    private int normalColor = Color.parseColor("#FFFFFF");
    private int selectedColor = Color.parseColor("#07FA81");

    private OnProgressChangedListener onProgressChangedListener;

    private LyricInfo lyricInfo;
    private int flag_position = 0;
    private boolean flag_refresh = false;

    public LyricManager(){

    }

    public void setFileStream(final InputStream is) {
        if( is != null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    decode(is);
                }
            }).start();
        }else{
            lyricInfo = null;
        }
    }

    /**
     * 文件解析，然后设置当前位置
     * @param is
     */
    private void decode(InputStream is) {
        Log.e(TAG, "decode ： 开始解析文件");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line ;
            lyricInfo = new LyricInfo();
            while((line=br.readLine())!=null){
                analyzeLyric(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCurrentTimeMillis(0);
    }

    public void setCurrentTimeMillis(int currentTimeMillis) {
        List<LineInfo> lines = lyricInfo==null ? null : lyricInfo.getLines();
        if(lines != null){
            int position = 0;
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
            //以一个int的position来定位
            for(int i=0;i<lines.size();i++){
                if(currentTimeMillis > lines.get(i).getStart()){
                    Log.e(TAG, "setCurrentTimeMillis: currentTimeMillis="+currentTimeMillis+" line="+lines.get(i).getStart());
                    position = i;
                }else{
                    break;
                }
            }
            if(position == flag_position && !flag_refresh){
                return;
            }
            flag_position = position;
            for(int i=0;i<lines.size();i++){
                if(i != position){
                    ForegroundColorSpan span = new ForegroundColorSpan(normalColor);
                    String line = lines.get(i).getContent();
                    SpannableString spannableString = new SpannableString(line+"\n");
                    spannableString.setSpan(span,0,spannableString.length(),SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
                    stringBuilder.append(spannableString);
                }else{
                    ForegroundColorSpan span = new ForegroundColorSpan(selectedColor);
                    String line = lines.get(i).getContent();
                    SpannableString spannableString = new SpannableString(line+"\n");
                    spannableString.setSpan(span,0,spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    stringBuilder.append(spannableString);
                }
            }
            Message message = Message.obtain();
            message.what = 0x159;
            DataHolder holder = new DataHolder();
            holder.builder = stringBuilder;
            holder.position = flag_position;
            holder.refresh = flag_refresh;
            holder.lines = lines;
            message.obj = holder;
            handler.sendMessage(message);
            if(flag_refresh){
                flag_refresh = false;
            }
        }
    }

    /**
     * handler运行在主线程
     */
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0x159:
                    Object obj = msg.obj;
                    if(obj != null && obj instanceof DataHolder){
                        DataHolder holder = (DataHolder) obj;
                        if(onProgressChangedListener!=null){
                            Log.e(TAG, "handleMessage: 调用回调函数更新进度handler所在线程"+Thread.currentThread().getName());
                            onProgressChangedListener.onProgressChanged(holder.builder,holder.position,holder.refresh);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    class DataHolder{
        SpannableStringBuilder builder;
        List<LineInfo> lines;
        int position;
        boolean refresh;
    }

    /**
     * 逐行解析歌词文件
     * [ti:约定]-------标题
     [ar:周惠]------演唱者
     [al:周蕙－精选]-------专辑
     [00:26.00]远处的钟声回荡在雨里--------每句内容由一个时间点和内容组成
     分：秒.毫秒
     同时应该注意到
     [02:23.00][00:49.00]一路从泥泞走到了美景---------在每个内容可能出现多个时间点
     * */
    private void analyzeLyric(String line) {
//        Log.e(TAG, "analyzeLyric: 逐行解析:"+line);
        //先匹配特殊行，避免后面的匹配到特殊行
        int index = line.lastIndexOf("]");
        LineInfo lineInfo = new LineInfo();

        if(line!=null && line.startsWith("[offset:")){
            String offset = line.substring(8,index).trim();
            lyricInfo.setOffset(Long.parseLong(offset));
            return;
        }
        if(line!=null && line.startsWith("[ti:")){
            lyricInfo.setTitle(line.substring(4,index).trim());
            return;
        }
        if(line!=null && line.startsWith("[ar:")){
            lyricInfo.setArtist(line.substring(4,index));
            return;
        }
        if(line!=null && line.startsWith("[al:")){
            lyricInfo.setAlbum(line.substring(4,index));
            return;
        }
        if(line!=null && line.startsWith("[by:")){
            return;
        }
        if(line!=null && index==9 && line.trim().length()>10){
            String content = line.substring(10,line.length());
            lineInfo.setContent(content);
            lineInfo.setStart(analyzeStartTimeMillis(line.substring(0,10)));
            lyricInfo.getLines().add(lineInfo);
            return;
        }
    }

    /**    [00:33.12]
     * 从字符串获取时间值，以毫秒为单位
     * @param string
     * @return
     */
    private long analyzeStartTimeMillis(String string) {
        long minute = Long.parseLong(string.substring(1,3));
        long second = Long.parseLong(string.substring(4,6));
        long millisecond = Long.parseLong(string.substring(7,9));
        return minute*60000+second*1000+millisecond;
    }
}
