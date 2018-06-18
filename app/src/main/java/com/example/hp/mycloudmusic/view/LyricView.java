package com.example.hp.mycloudmusic.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class LyricView extends RelativeLayout implements View.OnTouchListener {
    public static final String TAG = "LyricView";

    private View mEmptyView;
    private ScrollView scrollView;
    private TextView textView;
    private float downY = 0;
    private int resetType = 1;
    private int textPaddingValue = 0;
    private int mPosition = 0;
    private boolean userTouch = false;
    private final int MSG_USER_TOUCH = 0x349;

    public LyricView(Context context) {
        this(context,null);
        Log.e(TAG, "LyricView: 构造函数1");
    }

    public LyricView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        Log.e(TAG, "LyricView: 构造函数2");
    }

    public LyricView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.e(TAG, "LyricView: 构造函数3");
        initWithContext(context);
    }

    /**
     * 想获得scrollView的高必须使用addOnGlobalLayoutListener监听事件
     * 否则可能为0
     * @param context
     */
    private void initWithContext(Context context) {
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textPaddingValue = (getHeight() - getPaddingTop() - getPaddingBottom())/2;
                if(textView != null && scrollView != null){
                    textView.setPadding(0, textPaddingValue,0, textPaddingValue);
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    LyricView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }else{
                    LyricView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    /**
     * 在构造函数之后调用，一般使用第二个构造函数
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.e(TAG, "onFinishInflate: ");
        scrollView = new ScrollView(getContext());
        LayoutParams scrollViewParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        scrollView.setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.setLayoutParams(scrollViewParams);
        scrollView.setVerticalFadingEdgeEnabled(true);
        scrollView.setFadingEdgeLength(220);
        scrollView.setOnTouchListener(this);

        //textView的高仍然占满scrollView，只是textView的顶部和底部多了padding（半个scrollView)
        textView = new TextView(getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16.0f);
        textView.setLineSpacing(6,1.5f);
        textView.setPadding(0, textPaddingValue,0, textPaddingValue);
        textView.setText("1\n2\n3\n4\n5\n6\n7\n8\n9\n0\n-\n1\n2\n3\n4\n5");

        scrollView.addView(textView,new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        this.addView(scrollView);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                down(event);
                break;
            case MotionEvent.ACTION_MOVE:
                move(event);
                break;
            case MotionEvent.ACTION_UP:
                up(event);
                break;
            default:
                break;
        }
        return false;
    }

    private void down(MotionEvent event) {
        downY = event.getY();
        userTouch = true;
    }

    private void move(MotionEvent event) {
        float moveY = Math.abs(event.getY() - downY);
        if(scrollView.getScrollY() <= 0 && (event.getY()-downY) > 0){
            //scrollView 已滑到顶部
            resetType = 1;
            textView.setPadding(textView.getPaddingLeft(), (int) (textView.getPaddingTop()+moveY/1.8f),textView.getPaddingRight(),textView.getPaddingBottom());
        }
        if(scrollView.getScrollY()>=(textView.getHeight()-scrollView.getHeight()) && (event.getY()-downY)<0){
            //scrollView 已滑到底部
            resetType = -1;
            textView.setPadding(textView.getPaddingLeft(),textView.getPaddingTop(),textView.getPaddingRight(), (int) (textView.getPaddingBottom()+moveY/1.2f));
        }
        downY = event.getY();
    }

    private void up(MotionEvent event) {
        resetViewHeight();
    }

    private void resetViewHeight() {
        if(textView.getPaddingTop()> textPaddingValue || textView.getPaddingBottom()> textPaddingValue){
            reset();
        }else{
            handler.sendEmptyMessageDelayed(MSG_USER_TOUCH,1200);
        }
    }

    private void reset() {
        ValueAnimator valueAnimator = null;
        if(resetType == 1){
            //向上恢复
            valueAnimator = ValueAnimator.ofFloat(textView.getPaddingTop(), textPaddingValue);
            valueAnimator.setDuration(400);
            valueAnimator.setInterpolator(new OvershootInterpolator(0.7f));
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    textView.setPadding(textView.getPaddingLeft(), (int) value,textView.getPaddingRight(),textView.getPaddingBottom());
//                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
            });
        }else{
            valueAnimator = ValueAnimator.ofFloat(textView.getPaddingBottom(), textPaddingValue);
            valueAnimator.setInterpolator(new OvershootInterpolator(0.7f));
            valueAnimator.setDuration(400);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    textView.setPadding(textView.getPaddingLeft(),textView.getPaddingTop(),textView.getPaddingRight(), (int) value);
//                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                handler.sendEmptyMessageDelayed(MSG_USER_TOUCH,1200);
            }
        });
        valueAnimator.start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_USER_TOUCH:
                    userTouch = false;
                    break;
            }
        }
    };

    public void setEmptyView(View emptyView) {
        mEmptyView = emptyView;
        if(mEmptyView != null && textView != null){
            if(textView.getText().toString()==null || TextUtils.isEmpty(textView.getText().toString().trim())){
                mEmptyView.setVisibility(View.VISIBLE);
                Log.e(TAG, "emptyview is visible" );
            }else{
                mEmptyView.setVisibility(View.GONE);
                Log.e(TAG, "emptyview is gone");
            }
        }
    }

    /**
     * 先判断textView是否为空
     * @param charSequence
     */
    public void setText(CharSequence charSequence) {
        if(textView != null){
            textView.setText(charSequence);
            if(charSequence == null || TextUtils.isEmpty(charSequence.toString().trim())){
                if(mEmptyView!=null){
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }else{
                if(mEmptyView!=null){
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        }
    }

    public void setCurrentPosition(int position) {
        if(mPosition != position){
            mPosition = position;
            if(userTouch!=true){
                doScroll(position);
            }
        }
    }

    private void doScroll(int position) {
        if(scrollView!=null){
            if(!userTouch){
                Animator animator = setupScroll(position);
                animator.start();
            }
        }
    }

    private Animator setupScroll(int position) {
        int start = scrollView.getScrollY();
        int end = textView.getPaddingTop()+textView.getLineHeight()*position-scrollView.getHeight()/2-textView.getLineHeight()/2;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(start,end);
        valueAnimator.setDuration(600);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollView.smoothScrollTo(0,value);
            }
        });
        return valueAnimator;
    }
}
