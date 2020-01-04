package com.example.hp.mycloudmusic.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;

import com.example.hp.mycloudmusic.util.DisplayUtil;

public class ArtistDetailScrollView extends ScrollView {
    public static final String TAG = "ArtistDetailScrollView";

    private LinearLayout worksInfo;
    private RadioGroup radioGroup;
    private ViewPager vpWorksList;
    private MyOnScrollListener listener;

    private float x;
    private float y=-1;
    private float newX;
    private float newY;
    private float deltaY;
    private int tmWorksInfo = -1;
    private int topWorksInfo = -1;
    private int height = 1;
    private boolean isTop = false;

    public ArtistDetailScrollView(Context context) {
        super(context);
    }

    public ArtistDetailScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArtistDetailScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 布局改变,如固定radiogroup
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
//        if(y>0 && y<DisplayUtil.dip2px(getContext(),220)){
//            float alpha = y/DisplayUtil.dip2px(getContext(),220);
//            listener.setAlpha(alpha);
//        }
        if(t >= worksInfo.getTop()- DisplayUtil.dip2px(getContext(),65)){
            scrollTo(0,worksInfo.getTop()-DisplayUtil.dip2px(getContext(),65));
        }
        if(t == worksInfo.getTop()-DisplayUtil.dip2px(getContext(),65)){
            isTop = true;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        worksInfo = (LinearLayout) getChildAt(0);
        radioGroup = (RadioGroup) worksInfo.getChildAt(0);
        vpWorksList = (ViewPager) worksInfo.getChildAt(2);
        setOverScrollMode(OVER_SCROLL_NEVER);
    }


    /**
     * 本身带有scroll
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch(ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "down!");
                y = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, "move!");
                ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) worksInfo.getLayoutParams();
                if(tmWorksInfo == -1) {
                    tmWorksInfo = layoutParams.topMargin;
                    topWorksInfo = worksInfo.getTop();
                }
                newY = ev.getY();
                deltaY = newY-y;
                float moveY = (float) Math.sqrt(Math.abs(deltaY)*2);
                if(newY > y) {
                    if(getScrollY()<=0) {
                        worksInfo.layout(worksInfo.getLeft(),(int)(worksInfo.getTop()+moveY),worksInfo.getRight(),(int)(worksInfo.getBottom()+moveY));
                        //此处无需加65dp，getTop相对于父布局已经包含该65dp
                        listener.imageScale(worksInfo.getTop());
                    }
                }else if(newY < y){
                    if(isTop){
                        return false;
                    }
                    if(getScrollY()>=worksInfo.getTop()- DisplayUtil.dip2px(getContext(),65)){
                        return false;
                    }
                    if(topWorksInfo<worksInfo.getTop()) {
                        worksInfo.layout(worksInfo.getLeft(), (int) (worksInfo.getTop() - moveY), worksInfo.getRight(), (int) (worksInfo.getBottom() - moveY));
                        listener.imageScale(worksInfo.getTop());
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                if(worksInfo.getTop()>DisplayUtil.dip2px(getContext(),220)) {
                    worksInfo.layout(worksInfo.getLeft(), topWorksInfo, worksInfo.getRight(), topWorksInfo + worksInfo.getHeight());
                    listener.imageScale(topWorksInfo);
                }
                break;
        }
        y = newY;
        /**
         * （1）当在过度滚动时(如持续往下拉)，scrollView的onTouchEvent返回false(因为无法滚动),scrollView的滚动被关闭
         * （2）当过度滚动后往上拉，scrollView的onTouchEvent返回true(因为可以滚动)，scrollView的滚动开启
         * 这里的判断是在第二种情况下，关闭scrollView的滚动
         */
        if(worksInfo.getTop() > DisplayUtil.dip2px(getContext(),220)&&getScrollY()==0){
            return false;
        }
        return super.onTouchEvent(ev);
    }


    public void setScrollListener(MyOnScrollListener myOnScrollListener){
        this.listener = myOnScrollListener;
    }

    public interface MyOnScrollListener{

        void llMoveTo(float margin);

        void imageScale(float v);

        void setAlpha(float alpha);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_DOWN){
            y = ev.getY();
            return super.onInterceptTouchEvent(ev);
        }else{
            return true;
        }
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        return 0;
    }

    public boolean getIsTop(){
        return isTop;
    }
}
