package com.example.hp.mycloudmusic.fragment.factory;

import com.example.hp.mycloudmusic.fragment.callback.ClickListener;
import com.example.hp.mycloudmusic.fragment.instance.LocalFragment;
import com.example.hp.mycloudmusic.fragment.instance.MeFragment;
import com.example.hp.mycloudmusic.fragment.instance.MergeFragment;
import com.example.hp.mycloudmusic.fragment.instance.MusicFragment;
import com.example.hp.mycloudmusic.fragment.instance.PlayMusicFragment;
import com.example.hp.mycloudmusic.fragment.instance.SearchFragment;

public class FragmentFactory {
    private static FragmentFactory mFragmentFactory;
    private SearchFragment mSearchFragment;
    private MusicFragment mMusicFragment;
    private MeFragment mMeFragment;
    private LocalFragment mLocalFragment;
    private PlayMusicFragment mPlayMusicFragment;
    private MergeFragment mMergeFragment;

    private ClickListener listener;

    /**
     * 静态方法是属于类的，而普通方法是属于对象的。
     * 属于类的静态方法在对象不存在时就可以访问到，而普通方法需要先new一个对象后才能访问
     * 当我们访问静态方法时，这个时候没有对象创建，只能访问类的静态变量
     * @return
     */
    public static FragmentFactory getInstance(ClickListener listener){
        if(mFragmentFactory==null){
            synchronized(FragmentFactory.class){
                if(mFragmentFactory == null) {
                    mFragmentFactory = new FragmentFactory(listener);
                }
            }
        }
        return mFragmentFactory;
    }

    //不添加对外的构造函数
    private FragmentFactory(ClickListener listener){
        this.listener = listener;
    }

    public SearchFragment getmSearchFragment() {
        if(mSearchFragment == null){
            synchronized (FragmentFactory.class){
                if(mSearchFragment == null){
                    mSearchFragment = new SearchFragment();
                    mSearchFragment.setClickListener(listener);
                }
            }
        }
        return mSearchFragment;
    }

    public MusicFragment getmMusicFragment() {
        if(mMusicFragment == null){
            synchronized (FragmentFactory.class){
                if(mMusicFragment == null){
                    mMusicFragment = new MusicFragment();
                    mMusicFragment.setClickListener(listener);
                }
            }
        }
        return mMusicFragment;
    }

    public MeFragment getmMeFragment() {
        if(mMeFragment == null){
            synchronized (FragmentFactory.class){
                if(mMeFragment == null){
                    mMeFragment = new MeFragment();
                    mMeFragment.setClickListener(listener);
                }
            }
        }
        return mMeFragment;
    }

    public LocalFragment getmLocalFragment() {
        if(mLocalFragment == null){
            synchronized (FragmentFactory.class){
                if(mLocalFragment == null){
                    mLocalFragment = new LocalFragment();
                    mLocalFragment.setClickListener(listener);
                }
            }
        }
        return mLocalFragment;
    }

    public PlayMusicFragment getmPlayMusicFragment() {
        if(mPlayMusicFragment == null){
            synchronized (FragmentFactory.class){
                if(mPlayMusicFragment == null){
                    mPlayMusicFragment = new PlayMusicFragment();
                    mPlayMusicFragment.setClickListener(listener);
                }
            }
        }
        return mPlayMusicFragment;
    }

    public MergeFragment getmMergeFragment(String search_word){
        if(mMergeFragment == null){
            synchronized(FragmentFactory.class){
                if(mMergeFragment == null){
                    mMergeFragment = MergeFragment.newInstance(search_word);
                }
            }
        }
        return mMergeFragment;
    }
}
