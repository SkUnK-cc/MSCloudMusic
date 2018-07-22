package com.example.hp.mycloudmusic.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.util.DisplayUtil;

public class StickNavLayout extends LinearLayout implements NestedScrollingParent {
    public static final String TAG = "StickNavLayout";

    private View mTop;
    private View mNav;
    private ViewPager mViewPager;

    private OverScroller mScroller;
    private VelocityTracker mVelocityTracker;
    private ValueAnimator mOffsetAnimator;
    private Interpolator mInterpolator;
    /**
     * 表示滑动的时候，手的移动要大于这个距离才开始移动控件。如果小于这个距离就不触发移动控件
     * 区别用户是点击还是拖拽
     */
    private int mTouchSlop;

    private int mMaximumVelocity,mMinimumVelocity;  //开始fling的最大和最小速度
    private int mTopViewHeight;
    private MyStickyListener listener;
    private int barheight;

    //scroll表示手指滑动多少距离，界面跟着显示多少距离，而fling是根据你的滑动方向与轻重，还会自动滑动一段距离。

    public StickNavLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mScroller = new OverScroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMaximumVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        mMinimumVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        barheight = DisplayUtil.dip2px(getContext(),65);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        mTop = findViewById(R.id.id_stickynavlayout_avatar);
        mNav = findViewById(R.id.id_stickynavlayout_indicator);
        View view = findViewById(R.id.id_stickynavlayout_viewpager);
        if(!(view instanceof ViewPager)){
            throw new RuntimeException("id_stickynavlayout_viewpager should used by ViewPager!");
        }
        mViewPager = (ViewPager) view;
    }

    /**
     * 在onFinishInflate之后调用
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //不限制顶部的高度
//        getChildAt(0).measure(widthMeasureSpec,MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
//        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
//        params.height = getMeasuredHeight()-mNav.getMeasuredHeight();
        setMeasuredDimension(getMeasuredWidth(),mNav.getMeasuredHeight()+mViewPager.getMeasuredHeight());
    }

    /**
     * 在onMeasure之后调用
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        mTopViewHeight = mTop.getMeasuredHeight();
    }

    /**
     * 只有在onStartNestedScroll返回true的时候才会接着调用onNestedScrollAccepted，
     * 这个判断是需要我们自己来处理的，
     * 不是直接的父子关系一样可以正常进行
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.e(TAG, "onStartNestedScroll");
        return true;
    }
    /**
     * 字面意思可以理解出来父View接受了子View的邀请，可以在此方法中做一些初始化的操作。
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.e(TAG, "onNestedScrollAccepted");
    }
    /**
     * 每次子View在滑动前都需要将滑动细节传递给父View，
     * 一般情况下是在ACTION_MOVE中调用
     * public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow)，
     * dispatchNestedPreScroll在ScrollView、ListView的Action_Move中被调用
     * 然后父View就会被回调public void onNestedPreScroll(View target, int dx, int dy, int[] consumed)。
     */
    private int mNavTop = -1;
    private int mViewPagerTop = -1;
    private int avatar_height = -1;
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        Log.e(TAG, "onNestedPreScroll is call");
        //dy:鼠标往上走是正，往下走是负
        //方法一
        if(mNavTop == -1){
            mNavTop = mNav.getTop();
        }
        if(mViewPagerTop == -1){
            mViewPagerTop = mViewPager.getTop();
        }
        int moveY = (int) Math.sqrt(Math.abs(dy)*2);
        if(dy < 0){
            //往下拉
            if(getScrollY() == 0) {
                if (mNav.getTop() >= mNavTop) {
                    mNav.layout(mNav.getLeft(), mNav.getTop() + moveY, mNav.getRight(), mNav.getBottom() + moveY);
                    mViewPager.layout(mViewPager.getLeft(), mViewPager.getTop() + moveY, mViewPager.getRight(), mViewPager.getBottom() + moveY);
                    listener.imageScale(mNav.getTop());
                    consumed[1] = dy;
                }
            }else if(getScrollY() > 0 && !ViewCompat.canScrollVertically(target,-1)){
                if(getScrollY()+dy<0){
                    scrollTo(0,0);
                }else {
                    scrollTo(0, getScrollY() + dy);
                    consumed[1] = dy;
                }
            }
        }else if(dy > 0){
//            if(mNav.getTop() > mNavTop){
//                if(mNav.getTop()-moveY < mNavTop){
//                    mNav.layout(mNav.getLeft(),mNavTop,mNav.getRight(),mNavTop+mNav.getHeight());
//                    mViewPager.layout(mViewPager.getLeft(),mViewPagerTop,mViewPager.getRight(),mViewPagerTop+mViewPager.getHeight());
//                    listener.imageScale(mNavTop);
//                    consumed[1] = dy;
//                }else {
//                    mNav.layout(mNav.getLeft(), mNav.getTop() - moveY, mNav.getRight(), mNav.getBottom() - moveY);
//                    mViewPager.layout(mViewPager.getLeft(), mViewPager.getTop() - moveY, mViewPager.getRight(), mViewPager.getBottom() - moveY);
//                    listener.imageScale(mNav.getTop());
//                    consumed[1] = dy;
//                }
//            }
            if(mNav.getTop() > mNavTop){
                if(mNav.getTop()-moveY < mNavTop){
                    mNav.layout(mNav.getLeft(),mNavTop,mNav.getRight(),mNavTop+mNav.getHeight());
                    mViewPager.layout(mViewPager.getLeft(),mViewPagerTop,mViewPager.getRight(),mViewPagerTop+mViewPager.getHeight());
                    listener.imageScale(mNavTop);
                    consumed[1] = dy;
                }else {
                    mNav.layout(mNav.getLeft(), mNav.getTop() - moveY, mNav.getRight(), mNav.getBottom() - moveY);
                    mViewPager.layout(mViewPager.getLeft(), mViewPager.getTop() - moveY, mViewPager.getRight(), mViewPager.getBottom() - moveY);
                    listener.imageScale(mNav.getTop());
                    consumed[1] = dy;
                }
            }else{
                if(getScrollY()<DisplayUtil.dip2px(getContext(),155)){
                    if(getScrollY()+dy>DisplayUtil.dip2px(getContext(),155)){
                        scrollTo(0,DisplayUtil.dip2px(getContext(),155));
                        consumed[1] = dy;
                    }else {
                        scrollTo(0, getScrollY() + dy);
                        consumed[1] = dy;
                    }
                }
            }
        }
        //方法二
//        if(avatar_height == -1){
//            avatar_height = mTop.getHeight();
//        }
//        int moveY = (int) Math.sqrt(Math.abs(dy)*2);
//        if(dy < 0){
//            if(mTop.getHeight() >= avatar_height){
//                LayoutParams layoutParams = (LayoutParams) mTop.getLayoutParams();
//                layoutParams.height = layoutParams.height + moveY;
//                mTop.setLayoutParams(layoutParams);
//                consumed[1] = dy;
//                Log.e(TAG, "onNestedPreScroll: mTop.height = "+mTop.getHeight());
//            }
//        }
    }
    /**
     * 接下来子View就要进自己的滑动操作了，滑动完成后子View还需要调用
     * public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow)
     * 将自己的滑动结果再次传递给父View，父View对应的会被回调
     * public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed)，
     * 但这步操作有一个前提，就是父View没有将滑动值全部消耗掉，因为父View全部消耗掉，子View就不应该再进行滑动了
     * 子View进行自己的滑动操作时也是可以不全部消耗掉这些滑动值的，剩余的可以再次传递给父View，
     * 使父View在子View滑动结束后还可以根据子View剩余的值再次执行某些操作。
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {

    }
    /**
     * ACTION_UP或者ACTION_CANCEL的到来，
     * 子View需要调用public void stopNestedScroll()来告知父View本次NestedScrollig结束，
     * 父View对应的会被回调public void onStopNestedScroll(View target)，
     */
    @Override
    public void onStopNestedScroll(View child) {
        if(mNav.getTop() != mNavTop) {
            mNav.layout(mNav.getLeft(),mNavTop,mNav.getRight(),mNavTop+mNav.getHeight());
            mViewPager.layout(mViewPager.getLeft(),mViewPagerTop,mViewPager.getRight(),mViewPagerTop+mViewPager.getHeight());
            listener.imageScale(mNavTop);
        }
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        //鼠标向下拉，velocityY为负
        if(target instanceof RecyclerView && velocityY < 0){
            final RecyclerView recyclerView = (RecyclerView) target;
            final View firstChild = recyclerView.getChildAt(0);
            final int childAdapterPosition = recyclerView.getChildAdapterPosition(firstChild);
            consumed = childAdapterPosition > 3;
        }
        if(!consumed){
            animateScroll(velocityY,computeDuration(0),consumed);
        }else{
            animateScroll(velocityY,computeDuration(velocityY),consumed);

        }
        return true;
    }

    private int computeDuration(float velocityY) {
        final int distance;
        if(velocityY > 0){
            //鼠标往上
            distance = Math.abs(mNav.getTop() - getScrollY());
        }else{
            //鼠标往下
            distance = Math.abs(getScrollY());
        }

        final int duration;
        velocityY = Math.abs(velocityY);
        if(velocityY > 0){
            duration = 3 * Math.round(1000 * (distance / velocityY));
        }else{
            final float distanceRadtio = distance/getHeight();
            duration = (int) ((distanceRadtio+1)*150);
        }
        return duration;
    }

    private void animateScroll(float velocityY, int duration, boolean consumed) {
        final int currentOffset = getScrollY();
        final int topHeight = mNav.getTop();
        if(mOffsetAnimator == null){
            mOffsetAnimator = new ValueAnimator();
            mOffsetAnimator.setInterpolator(mInterpolator);
            mOffsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if(animation.getAnimatedValue() instanceof Integer){
                        scrollTo(0, (Integer) animation.getAnimatedValue());
                    }
                }
            });
        }else{
            mOffsetAnimator.cancel();
        }
        mOffsetAnimator.setDuration(Math.min(duration,600));

        if(velocityY >= 0){
            mOffsetAnimator.setIntValues(currentOffset,mNav.getTop());
            mOffsetAnimator.start();
        }else{
            if(!consumed){
                mOffsetAnimator.setIntValues(currentOffset,0);
                mOffsetAnimator.start();
            }
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return 0;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_UP:
                if(mNav.getTop()>mNavTop){
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                mNav.layout(mNav.getLeft(),mNavTop,mNav.getRight(),mNavTop+mNav.getHeight());
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setScrollListener(MyStickyListener myOnScrollListener){
        this.listener = myOnScrollListener;
    }

    public interface MyStickyListener{

        void imageScale(float v);

        void setAlpha(float alpha);
    }
}

