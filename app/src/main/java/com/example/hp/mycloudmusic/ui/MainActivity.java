package com.example.hp.mycloudmusic.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.base.BaseAppHelper;
import com.example.hp.mycloudmusic.fragment.callback.ClickListener;
import com.example.hp.mycloudmusic.fragment.factory.FragmentFactory;
import com.example.hp.mycloudmusic.fragment.instance.LocalFragment;
import com.example.hp.mycloudmusic.fragment.instance.MeFragment;
import com.example.hp.mycloudmusic.fragment.instance.MusicFragment;
import com.example.hp.mycloudmusic.fragment.instance.PlayMusicFragment;
import com.example.hp.mycloudmusic.fragment.instance.SearchFragment;
import com.example.hp.mycloudmusic.musicInfo.AudioBean;
import com.example.hp.mycloudmusic.mvp.presenter.MainPresenter;
import com.example.hp.mycloudmusic.mvp.view.IMainView;
import com.example.hp.mycloudmusic.service.PlayService;
import com.example.hp.mycloudmusic.service.listener.OnPlayerEventListener;

import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity<MainPresenter> implements IMainView, RadioGroup.OnCheckedChangeListener ,ClickListener, View.OnClickListener {

    @Bind(R.id.framelayout)
    FrameLayout frameLayout;
    @Bind(R.id.bottom_radiogroup)
    RadioGroup radioGroup;
    @Bind(R.id.bottom_radio_search)
    RadioButton radioSearch;
    @Bind(R.id.bottom_radio_music)
    RadioButton radiomusic;
    @Bind(R.id.bottom_radio_me)
    RadioButton radiome;

    private int fragmentPosition;
    public static final int FRAGMENT_SEARCH = 0;
    public static final int FRAGMENT_MUSIC = 1;
    public static final int FRAGMENT_ME = 2;
    public static final int FRAGMENT_LOCAL = 3;
    public static final int FRAGMENT_PLAYING = 4;

    private int playingType=-1;
    private static final int LOCAL_PLAYING = 0;
    private static final int ONLINE_PLAYING = 1;

    private int playingPosition=-1;

    private Bundle bundle;
    private SearchFragment searchFragment;
    private MusicFragment musicFragment;
    private MeFragment meFragment;
    private Fragment currentFragment;
    private LocalFragment localFragment;
    private PlayMusicFragment playMusicFragment;
    private static final String POSITION = "position";
    private String TAG = "MainActivity";
    private boolean isShowPlayFragment = false;
    private static final int WAIT_PLAYFRAGMENT_ADD = 11;
    private boolean isBind = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            bundle = savedInstanceState;
        }
        Log.e(TAG, "onCreate.." );
        permissionCheck();
        checkPlayService();
    }

//    @Override
//    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerActivityComponent.builder()
//                .appComponent(appComponent)
//                .build()
//                .inject(this);
//    }

    public void checkPlayService() {
        if(BaseAppHelper.get().getPlayService() == null){
            Intent intent = new Intent(this, PlayService.class);
            Log.e(TAG, "checkPlayService: to start service");
            isBind = bindService(intent,playConnection, Context.BIND_AUTO_CREATE);
        }
    }
    private ServiceConnection playConnection = new ServiceConnection() {
        /**
         * 在serviceconnected中为service添加监听，
         * 防止service未创建
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService playService = ((PlayService.PlayBinder) service).getPlayService();
            BaseAppHelper.get().setPlayService(playService);
            initPlayServiceListener();
            Log.e(TAG, "onServiceConnected: ");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected");
        }
    };

    private void initPlayServiceListener() {
        if(getPlayService() == null){
            Log.e(TAG, "initPlayServiceListener: service is null!!!");
            return;
        }
        Log.e(TAG, "initPlayServiceListener: service is not null");
        getPlayService().setOnPlayerEventListener(new OnPlayerEventListener(){

            @Override
            public void onUpdateProgress(int currentPosition) {
                playMusicFragment.onUpdateProgress(currentPosition);
            }

            @Override
            public void onPlayerStart() {

            }

            @Override
            public void onBufferingUpdate(int percent) {

            }

            @Override
            public void onChange(AudioBean music) {
                Log.e(TAG, "onchange: 调用onchange方法0");
                Log.e(TAG, "onChange: "+music==null?"music=null":"music!=null" );
                Log.e(TAG, "onChange: "+playMusicFragment==null?"fragment=null":"fragment!=null" );
                if(playMusicFragment.isAdded()){
                    Log.e(TAG, "onChange: fragment is add");
                }else{
                    Log.e(TAG, "onChange: fragment is  not  add");
                }
                if(music != null && playMusicFragment!=null && playMusicFragment.isAdded()){
                    playMusicFragment.onchange(music);
                }else{
                    Message message = Message.obtain();
                    message.what = WAIT_PLAYFRAGMENT_ADD;
                    message.obj = music;
                    handler.sendMessageDelayed(message,300);
                }
            }
        });
    }
    Handler handler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case WAIT_PLAYFRAGMENT_ADD:
                    if(playMusicFragment.isAdded()){
                        Log.e(TAG, "调用framgnet方法onchange");
                        if(msg.obj!=null && msg.obj instanceof AudioBean){
                            AudioBean music = (AudioBean) msg.obj;
                            playMusicFragment.onchange(music);
                        }
                    }else{
                        Message message = Message.obtain();
                        message.obj = msg.obj;
                        message.what = WAIT_PLAYFRAGMENT_ADD;
                        handler.sendMessageDelayed(message,300);
                    }
                    break;
            }
        }
    };

    private void permissionCheck() {
        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]
                    {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
    }


    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {
        radioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initView() {
//        if(!checkServiceAlive()){
//
//        }
        initFragment();
    }


    private void initFragment() {
        if(bundle != null) {
            Log.d(TAG, "bundle != null");
            searchFragment = FragmentFactory.getInstance(this).getmSearchFragment();
            musicFragment = FragmentFactory.getInstance(this).getmMusicFragment();
            meFragment = FragmentFactory.getInstance(this).getmMeFragment();
            localFragment = FragmentFactory.getInstance(this).getmLocalFragment();
            int index = bundle.getInt(POSITION);
            if(index== FRAGMENT_PLAYING){
                index = FRAGMENT_MUSIC;
            }
            showFragment(index);
        }else{
            Log.d(TAG, "bundle == null");
            showFragment(FRAGMENT_MUSIC);
        }
    }

    private void showFragment(int index) {
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        Log.e(TAG, "showFragment: index = "+index );
        fragmentPosition = index;
        switch(index){
            case FRAGMENT_SEARCH:
                if(searchFragment == null){
                    searchFragment = FragmentFactory.getInstance(this).getmSearchFragment();
                }
                addOrShowFragment(transaction,searchFragment);
                radioSearch.setChecked(true);
                break;
            case FRAGMENT_MUSIC:
                if(musicFragment == null){
                    Log.d(TAG, "musicFragment == null");
                    musicFragment = FragmentFactory.getInstance(this).getmMusicFragment();
                    musicFragment.setClickListener(this);
                    Log.d(TAG, "musicFragment is created");
                }
                addOrShowFragment(transaction,musicFragment);
                radiomusic.setChecked(true);
                break;
            case FRAGMENT_ME:
                if(meFragment == null){
                    meFragment = FragmentFactory.getInstance(this).getmMeFragment();
                }
                addOrShowFragment(transaction,meFragment);
                radiome.setChecked(true);
                break;
            case FRAGMENT_LOCAL:
                if(localFragment == null){
                    localFragment = FragmentFactory.getInstance(this).getmLocalFragment();
                    localFragment.setClickListener(this);
                }
                addOrShowFragment(transaction,localFragment);
                radiomusic.setChecked(true);
                break;
        }
    }

    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment) {
        if(currentFragment == fragment){
            Log.d(TAG, "currentFragment == frament");
            return ;
        }


        if (currentFragment != null){
            transaction.hide(currentFragment);
        }
        if(!fragment.isAdded()){
            Log.d(TAG, "!fragment.isAdded()");
            transaction.add(R.id.framelayout,fragment).commit();
        }else{
            Log.d(TAG, "fragment.isAdded()");
            transaction.show(fragment).commit();
        }

        currentFragment = fragment;
    }

    @Override
    protected int getContentView() { return R.layout.activity_main; }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.bottom_radio_search:
                if(fragmentPosition == FRAGMENT_SEARCH){
                    return;
                }
                showFragment(FRAGMENT_SEARCH);
                break;
            case R.id.bottom_radio_music:
                if(fragmentPosition == FRAGMENT_MUSIC){
                    return;
                }
                showFragment(FRAGMENT_MUSIC);
                break;
            case R.id.bottom_radio_me:
                if(fragmentPosition == FRAGMENT_ME){
                    return;
                }
                showFragment(FRAGMENT_ME);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION,fragmentPosition);
    }

    /**
     * 该方法Activity不会调用pause或stop
     * activity依然存在，并没有被其他Activity覆盖
     */
    private void hidePlayingFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(0,R.anim.fragment_slide_out);
        transaction.hide(playMusicFragment);
        transaction.commitAllowingStateLoss();
    }

    private void showPlayingFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_from_right,0);
        if(playMusicFragment == null){
            playMusicFragment = FragmentFactory.getInstance(this).getmPlayMusicFragment();
            //transaction.replace(android.R.id.content,playMusicFragment);
            transaction.add(android.R.id.content,playMusicFragment);
        }else{
            transaction.show(playMusicFragment);
        }
        isShowPlayFragment = true;
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if(playMusicFragment != null && isShowPlayFragment){
            isShowPlayFragment = false;
            hidePlayingFragment();
            return;
        }
        super.onBackPressed();      //该方法会回退
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(isBind) {
            unbindService(playConnection);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.to_local_music:
                showFragment(FRAGMENT_LOCAL);
                break;
            case R.id.local_music_back:
                showFragment(FRAGMENT_MUSIC);
                break;
            case R.id.the_music_playing:
                showPlayingFragment();
                break;
            default:
                break;
        }
    }

    @Override
    public void onLocalItemClick(int position) {
        showPlayingFragment();
        if(checkPlayingChange(LOCAL_PLAYING,position)) {
            List<AudioBean> musicList = BaseAppHelper.get().getMusicList();
            BaseAppHelper.get().getPlayService().play(musicList, position);
        }
    }

    private boolean checkPlayingChange(int type, int position) {
        if(playingType == type && playingPosition==position){
            return false;
        }else{
            playingType = type;
            playingPosition = position;
            return true;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
//        Log.e(TAG, "--------------------------------------------onStart-----" );
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "--------------------------------------------onResume-------");
    }

    @Override
    protected void onPause() {
        super.onPause();
//        Log.e(TAG, "--------------------------------------------onPause----");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        Log.e(TAG, "--------------------------------------------onStop: -----");
    }
}
