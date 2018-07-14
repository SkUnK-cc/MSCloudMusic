package com.example.hp.mycloudmusic.fragment.instance;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.fragment.factory.FragmentFactory;
import com.example.hp.mycloudmusic.util.InputMethodUtils;

import butterknife.Bind;

public class SearchFragment extends BaseFragment implements View.OnClickListener {

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

    FragmentManager fragmentManager;
    MergeFragment merge_fragment;

//    @Override
//    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerActivityComponent.builder()
//                .appComponent(appComponent)
//                .build()
//                .inject(this);
//    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        fragmentManager = getActivity().getSupportFragmentManager();
        etSearch.clearFocus();
//        etSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e(TAG, "edittext is click!!");
//            }
//        });
        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    Log.e(TAG, "editText is focus.");
                }else{
                    Log.e(TAG, "edittext is not focus.");
                }
            }
        });
        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e(TAG, "edittext is touch");
                if(!isEditChanged) {
                    performEditAnimate(v);
                    showCancel();
                }
                return false;
            }
        });
        etSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
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
            }
        });
        tvCancel.setOnClickListener(this);
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
            Log.e(TAG, "all=" + screenWidth + "\niv=" + iv + "barPad=" + barPad + "\noriginal=" + etOriginalWidth + "\nchange=" + changeWidth);
        }
        isEditChanged = true;
        ValueAnimator widthAnimator = ValueAnimator.ofInt(etOriginalWidth,changeWidth);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                editText.getLayoutParams().width = width;
                editText.requestLayout();
            }
        });
        ValueAnimator padAnimator = ValueAnimator.ofInt(etOriginalPadding,50);
        padAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int padLeft = (int) animation.getAnimatedValue();
                editText.setPadding(padLeft,editText.getPaddingTop(),editText.getPaddingRight(),editText.getPaddingBottom());
                editText.requestLayout();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(widthAnimator).with(padAnimator);
        set.setDuration(300);
        set.start();
    }

    /**
     * 按下enter键，到搜索页面
     * @param text
     */
    private void search(String text) {
//        Intent intent  = new Intent(getContext(), OnlineSearchActivity.class);
//        intent.putExtra(ConstantValue.SEARCH_WORD,text);
//        startActivity(intent);
        if(merge_fragment == null){
            merge_fragment = FragmentFactory.getInstance(null).getmMergeFragment(text);
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(R.id.searchfragment_framelayout, merge_fragment);
            transaction.commit();
        }else{
            Bundle bundle = merge_fragment.getArguments();
            bundle.putString(MergeFragment.SEARCH_WORD,text);
            merge_fragment.setArguments(bundle);
            if(merge_fragment.isAdded()){
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.show(merge_fragment);
                transaction.commit();
            }else{
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.add(R.id.searchfragment_framelayout,merge_fragment);
                transaction.commit();
            }
            merge_fragment.updateData();
        }
        closeKeyboard(etSearch);
    }

    @Override
    protected int getContentView() {
        return R.layout.search_fragment;
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
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.hide(merge_fragment);
                transaction.commit();
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
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue();
                editText.getLayoutParams().width = width;
                editText.requestLayout();
            }
        });
        ValueAnimator padAnimator = ValueAnimator.ofInt(changePad,etOriginalPadding);
        padAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int padLeft = (int) animation.getAnimatedValue();
                editText.setPadding(padLeft,editText.getPaddingTop(),editText.getPaddingRight(),editText.getPaddingBottom());
                editText.requestLayout();
            }
        });
        AnimatorSet set = new AnimatorSet();
        set.play(widthAnimator).with(padAnimator);
        set.setDuration(300);
        set.start();
    }
}
