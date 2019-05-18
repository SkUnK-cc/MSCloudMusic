package com.example.hp.mycloudmusic.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
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
import com.example.hp.mycloudmusic.fragment.instance.ArtistDetailFragment;
import com.example.hp.mycloudmusic.fragment.instance.LocalFragment;
import com.example.hp.mycloudmusic.fragment.instance.MeFragment;
import com.example.hp.mycloudmusic.fragment.instance.MusicFragment;
import com.example.hp.mycloudmusic.fragment.instance.PlayMusicFragment;
import com.example.hp.mycloudmusic.fragment.instance.SearchFragment;
import com.example.hp.mycloudmusic.musicInfo.AbstractMusic;
import com.example.hp.mycloudmusic.mvp.presenter.MainPresenter;
import com.example.hp.mycloudmusic.mvp.view.IMainView;
import com.example.hp.mycloudmusic.service.PlayService;

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
    protected int getContentView() { return R.layout.activity_main; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            bundle = savedInstanceState;
        }
        permissionCheck();
        android.os.Debug.startMethodTracing("methodTracingFile");
        checkPlayService();
        android.os.Debug.stopMethodTracing();
    }

    @Override
    protected void initPresenter() {

    }

    public void checkPlayService() {
        if(BaseAppHelper.get().getPlayService() == null){
            Intent intent = new Intent(this, PlayService.class);
            isBind = bindService(intent,playConnection, Context.BIND_AUTO_CREATE);
        }
    }

    private ServiceConnection playConnection = new ServiceConnection() {
        /**
         * 在 onServiceconnected中为service设置listener，
         * 防止service未创建，出现空指针
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService playService = ((PlayService.PlayBinder) service).getPlayService();
            BaseAppHelper.get().setPlayService(playService);
            initPlayServiceListener();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.e(TAG, "onServiceDisconnected");
        }
    };
    private void initPlayServiceListener() {
        if(getPlayService() == null){
            return;
        }
    }

//    Handler handler  = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case WAIT_PLAYFRAGMENT_ADD:
//                    if(playMusicFragment.isAdded()){
//                        Log.e(TAG, "调用framgnet方法onchange");
//                        if(msg.obj!=null && msg.obj instanceof AudioBean){
//                            AudioBean music = (AudioBean) msg.obj;
//                            playMusicFragment.onchange(music);
//                        }
//                    }else{
//                        Message message = Message.obtain();
//                        message.obj = msg.obj;
//                        message.what = WAIT_PLAYFRAGMENT_ADD;
//                        handler.sendMessageDelayed(message,300);
//                    }
//                    break;
//            }
//        }
//    };

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
        Log.e(TAG, "addOrShowFragment: test");
        if(!fragment.isAdded()){
            Log.e(TAG, "!fragment.isAdded()");
            transaction.add(R.id.framelayout,fragment).commit();
        }else{
            Log.e(TAG, "fragment.isAdded()");
            transaction.show(fragment).commit();
        }
        currentFragment = fragment;
    }

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
        transaction.setCustomAnimations(0,R.anim.fragment_slide_out_right);
        transaction.hide(playMusicFragment);
        transaction.commitAllowingStateLoss();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        PlayMusicFragment fragment = FragmentFactory.getInstance(this).getmPlayMusicFragment();
//        if(fragment.isAdded()){
//            transaction.remove(fragment);
//        }
//        transaction.commitAllowingStateLoss();
    }

    private void showPlayingFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_from_right,0);
        if(playMusicFragment == null){
            Log.e(TAG, "showPlayingFragment: add");
            playMusicFragment = FragmentFactory.getInstance(this).getmPlayMusicFragment();
            transaction.add(android.R.id.content,playMusicFragment);
        }else if(playMusicFragment.isAdded()){
            Log.e(TAG, "showPlayingFragment: show");
            transaction.show(playMusicFragment);
        }
        isShowPlayFragment = true;
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        Log.e(TAG, "onBackPressed: activity");
        playMusicFragment = FragmentFactory.getInstance(this).mPlayMusicFragment;
//        boolean bool = playMusicFragment == null;
//        Log.e(TAG, "onBackPressed: playmusic is null?"+bool);
//        Log.e(TAG, "onBackPressed: is added?"+playMusicFragment.isAdded());
//        Log.e(TAG, "onBackPressed: is hidden?"+playMusicFragment.isHidden());
        if(playMusicFragment != null && playMusicFragment.isAdded() && !playMusicFragment.isHidden()){
            Log.e(TAG, "onBackPressed: hide playmusic fragment!!!");
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
            List<AbstractMusic> musicList = BaseAppHelper.get().getMusicList();
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
    protected void onStop() {
//        Log.e(TAG, "--------------------------------------------onStop: -----");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PlayMusicFragment fragment = FragmentFactory.getInstance(this).getmPlayMusicFragment();
        if(fragment != null && fragment.isAdded()){
            transaction.remove(fragment);
        }
        transaction.commitAllowingStateLoss();
        super.onStop();
    }

    public void showTest() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_from_right, R.anim.fragment_slide_out_left);
        playMusicFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment();
        ArtistDetailFragment detailFragment = FragmentFactory.getInstance(null).getArtistDetailFragment(null);
        if (playMusicFragment != null && !playMusicFragment.isAdded()) {
            Log.e("click: ","add");
            transaction.add(android.R.id.content, playMusicFragment);
//            transaction.replace(android.R.id.content,playMusicFragment)
        } else if (playMusicFragment.isAdded()) {
            Log.e("click: ","show");
//            transaction.show(playMusicFragment)
            transaction.show(playMusicFragment).hide(detailFragment);
        }
        transaction.commitAllowingStateLoss();
    }
}
