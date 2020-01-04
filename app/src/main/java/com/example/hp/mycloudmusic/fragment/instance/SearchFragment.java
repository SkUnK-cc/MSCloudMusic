package com.example.hp.mycloudmusic.fragment.instance;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.adapter.recyclerview.search_fragment.NoScrollGridLayoutManager;
import com.example.hp.mycloudmusic.adapter.recyclerview.search_fragment.SearchAdapter;
import com.example.hp.mycloudmusic.fragment.factory.FragmentFactory;
import com.example.hp.mycloudmusic.fragment.presenter.SearchPresenter;
import com.example.hp.mycloudmusic.fragment.view.ISearchView;
import com.example.hp.mycloudmusic.musicInfo.mv.FirstMvList;
import com.example.hp.mycloudmusic.util.InputMethodUtils;

import org.jetbrains.annotations.NotNull;

import butterknife.Bind;

public class SearchFragment extends BaseFragment<SearchPresenter> implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener,ISearchView{

    public static final String TAG = "SearchFragment";
    private int etOriginalWidth = 0;
    private int etOriginalPadding = 0;
    private int changeWidth = 0;
    private int screenWidth = 0;
    private static final int changePad = 50;
    private boolean isEditChanged = false;

    @Bind(R.id.rl_search_bar)
    RelativeLayout rlSearchBar;
    @Bind(R.id.search_edit)
    EditText etSearch;
    @Bind(R.id.the_music_playing)
    ImageView ivPlaying;
    @Bind(R.id.cancel)
    TextView tvCancel;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    @Bind(R.id.id_search_fragment_recyclerview)
    RecyclerView mRecyclerView;

    private SearchAdapter mSearchAdapter;

    FragmentManager fragmentManager;
    MergeFragment merge_fragment;

    @Override
    protected int getContentView() {
        return R.layout.search_fragment;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mPresenter = new SearchPresenter();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initData() {
        mPresenter.getFirstMvs();
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {

        setEtWidth();

        mSwipeLayout.setColorSchemeColors(Color.parseColor("#9C27B0"));
        mSwipeLayout.setDistanceToTriggerSync(300);
        mSwipeLayout.setProgressBackgroundColorSchemeColor(Color.WHITE);
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE);
        mSwipeLayout.setOnRefreshListener(this);

        fragmentManager = getActivity().getSupportFragmentManager();
//        etSearch.clearFocus();
        etSearch.setOnTouchListener((v, event) -> {
            Log.e(TAG, "edittext is touch");
            if(!isEditChanged) {
                performEditAnimate(v);
                showCancel();
            }
            return false;
        });
        etSearch.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                String text = etSearch.getText().toString().trim();
                if(!text.equals("")) {
                    search(text);
                }else{
                    Toast.makeText(getContext(), "please input the words!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
        tvCancel.setOnClickListener(this);
        ivPlaying.setOnClickListener(this);

        NoScrollGridLayoutManager layoutManager = new NoScrollGridLayoutManager(getContext(),6);
        layoutManager.setScrollEnable(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mSearchAdapter = new SearchAdapter(getContext(),R.layout.item_search_two);
        RecyclerView.RecycledViewPool pool = mRecyclerView.getRecycledViewPool();
        pool.setMaxRecycledViews(0,10);
        mRecyclerView.setRecycledViewPool(pool);
        mRecyclerView.setAdapter(mSearchAdapter);
        mSearchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
            }
            @Override
            public void onTitleClick(RecyclerView parent, View view, int position) {
            }
            @Override
            public void onItemThreeClick(RecyclerView parent, View view, int position) {
                Toast.makeText(getContext(), "three item per line!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemTwoClick(RecyclerView parent, View view, int position) {
                Toast.makeText(getContext(), "two item per line!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEtWidth() {
        int etOw = etSearch.getLayoutParams().width;
        int etOp = etSearch.getPaddingLeft();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int sW = wm.getDefaultDisplay().getWidth();
        int iv = ivPlaying.getLayoutParams().width;
        int barPad = rlSearchBar.getPaddingLeft();
        int cW = screenWidth - iv - barPad;

        int w = sW-2*iv-barPad;
        etSearch.getLayoutParams().width = w;
        etSearch.requestLayout();
    }

    public void onRefresh(){
        new Handler().postDelayed(() -> mSwipeLayout.setRefreshing(false),3000);
    }

    private void showCancel() {
        tvCancel.setVisibility(View.VISIBLE);
        ivPlaying.setVisibility(View.INVISIBLE);
    }

    private void hideCancel(){
        tvCancel.setVisibility(View.INVISIBLE);
        ivPlaying.setVisibility(View.VISIBLE);
    }

    private void performEditAnimate(final View editText) {
        if(etOriginalWidth==0 && etOriginalPadding==0 && screenWidth==0 && changeWidth==0) {
            etOriginalWidth = editText.getLayoutParams().width;
            etOriginalPadding = editText.getPaddingLeft();
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            screenWidth = wm.getDefaultDisplay().getWidth();
            int iv = ivPlaying.getLayoutParams().width;
            int barPad = rlSearchBar.getPaddingLeft();
            changeWidth = screenWidth - iv - barPad;
        }
        isEditChanged = true;
        ValueAnimator widthAnimator = ValueAnimator.ofInt(etOriginalWidth,changeWidth);
        widthAnimator.addUpdateListener(animation -> {
            editText.getLayoutParams().width = (int) animation.getAnimatedValue();
            editText.requestLayout();
        });
        ValueAnimator padAnimator = ValueAnimator.ofInt(etOriginalPadding,50);
        padAnimator.addUpdateListener(animation -> {
            int padLeft = (int) animation.getAnimatedValue();
            editText.setPadding(padLeft,editText.getPaddingTop(),editText.getPaddingRight(),editText.getPaddingBottom());
            editText.requestLayout();
        });
        AnimatorSet set = new AnimatorSet();
        set.play(widthAnimator).with(padAnimator);
        set.setDuration(300);
        set.start();
    }

    /**
     * 按下enter键，到搜索页面
     */
    private void search(String text) {
        if(merge_fragment == null){
            merge_fragment = FragmentFactory.getInstance(null).getmMergeFragment(text);
        }else{
            Bundle bundle = merge_fragment.getArguments();
            if (bundle != null) {
                bundle.putString(MergeFragment.SEARCH_WORD,text);
            }
            merge_fragment.setArguments(bundle);
        }
        if(merge_fragment.isAdded()){
            addOrShowFragmentOnFragment(R.id.searchfragment_framelayout,merge_fragment,0);
            merge_fragment.updateData();
        }else{
            addOrShowFragmentOnFragment(R.id.searchfragment_framelayout,merge_fragment,0);
        }
        closeKeyboard(etSearch);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                Log.e(TAG, "cancel is click!!!");
                restoreEditAnimate(etSearch);
                closeKeyboard(etSearch);
                clearEdit();
                etSearch.clearFocus();
                hideCancel();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                if(merge_fragment!=null && merge_fragment.isAdded()){
                    Log.e(TAG, "onClick: remove from activity");
                    transaction.remove(merge_fragment);
                    transaction.commit();
                }
                break;
            case R.id.the_music_playing:
                Log.e(TAG, "onClick");
                PlayMusicFragment playFragment = FragmentFactory.getInstance(null).getmPlayMusicFragment();
//                FragmentTransaction transaction1 = fragmentManager.beginTransaction();
//                if(!playFragment.isAdded()){
//                    Log.e(TAG, "onClick: add");
//                    transaction1.add(android.R.id.content,playFragment);
//                }else if(playFragment.isHidden()){
//                    Log.e(TAG, "onClick: show");
//                    transaction1.show(playFragment);
//                }
//                transaction1.commitAllowingStateLoss();
                addOrShowFragmentOnActivity(android.R.id.content,playFragment,R.anim.fragment_slide_from_right);
                break;
            default:
                break;
        }
    }

    private void clearEdit() {
        etSearch.setText("");
    }

    private void closeKeyboard(View view) {
        InputMethodUtils.closeSoftKeyboard(view);
    }

    private void restoreEditAnimate(final EditText editText) {
        isEditChanged = false;
        ValueAnimator widthAnimator = ValueAnimator.ofInt(changeWidth,etOriginalWidth);
        widthAnimator.addUpdateListener(animation -> {
            editText.getLayoutParams().width = (int) animation.getAnimatedValue();
            editText.requestLayout();
        });
        ValueAnimator padAnimator = ValueAnimator.ofInt(changePad,etOriginalPadding);
        padAnimator.addUpdateListener(animation -> {
            int padLeft = (int) animation.getAnimatedValue();
            editText.setPadding(padLeft,editText.getPaddingTop(),editText.getPaddingRight(),editText.getPaddingBottom());
            editText.requestLayout();
        });
        AnimatorSet set = new AnimatorSet();
        set.play(widthAnimator).with(padAnimator);
        set.setDuration(300);
        set.start();
    }

    @Override
    public void showMvs(@NotNull FirstMvList data) {
        mSearchAdapter.setmDatas(data.getData());
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void loadMvsFail(int code) {
        Log.e(TAG, "loadMvsFail: faild");
    }
}
