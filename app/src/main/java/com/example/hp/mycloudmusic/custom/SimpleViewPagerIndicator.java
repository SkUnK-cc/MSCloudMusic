package com.example.hp.mycloudmusic.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SimpleViewPagerIndicator extends LinearLayout {

    private String[] mTitles ;
    private int COLOR_NORMAL = Color.BLACK;
    private int COLOR_CHECK = Color.parseColor("#9C27B0");
    private int mTabCount;
    private int mTabWidth;
    private float mTranslationX;
    private Paint mPaint = new Paint();
    private SparseArray<TextView> tvList = new SparseArray<>();

    private IndicatorClickListener listener;

    public SimpleViewPagerIndicator(Context context) {
        this(context,null);
    }

    public SimpleViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(COLOR_CHECK);
        mPaint.setStrokeWidth(9.0F);
    }

    public void setTitles(String[] titles){
        mTitles = titles;
        mTabCount = mTitles.length;
        generateTitleView();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTabWidth = w/mTabCount;
    }

    /**
     * View组件的绘制会调用draw(Canvas canvas)方法，draw过程中主要是先画Drawable背景，
     * 对drawable调用setBounds()然后是draw(Canvas c)方法.注意的是背景drawable的实际大小会影响view组件的大小
     * drawable的实际大小通过getIntrinsicWidth()和getIntrinsicHeight()获取，
     * 当背景比较大时view组件大小等于背景drawable的大小
     * 画完背景后，draw过程会调用onDraw(Canvas canvas)方法，然后就是dispatchDraw(Canvas canvas)方法,
     * dispatchDraw()主要是分发给子组件进行绘制，我们通常定制组件的时候重写的是onDraw()方法。
     * 值得注意的是ViewGroup容器组件的绘制，当它没有背景时直接调用的是dispatchDraw()方法, 而绕过了draw()方法
     * 当它有背景的时候就调用draw()方法，而draw()方法里包含了dispatchDraw()方法的调用。
     * 因此要在ViewGroup上绘制东西的时候往往重写的是dispatchDraw()方法而不是onDraw()方法，
     * 或者自定制一个Drawable，重写它的draw(Canvas c)和 getIntrinsicWidth(),
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.save();
        canvas.translate(mTranslationX,getHeight()-2);
        canvas.drawLine(0,0,mTabWidth,0,mPaint);
        canvas.restore();       //回到原位置,否则translate将发生位移
    }

    public void scroll(int position, float positionOffset) {
        mTranslationX = getWidth()/mTabCount * (position+positionOffset);
        //invalidate方法会执行draw过程，重绘View树。View（非容器类）调用invalidate方法只会重绘自身，ViewGroup调用则会重绘整个View树。
        invalidate();       //重新dispatchDraw
    }

    private void generateTitleView() {
        if(getChildCount() > 0){
            this.removeAllViews();
        }
        int count = mTitles.length;

        setWeightSum(count);
        for(int i=0;i<count;i++){
            TextView tv = new TextView(getContext());
            LayoutParams lp = new LayoutParams(0,LayoutParams.MATCH_PARENT);
            lp.weight = 1;
            tv.setGravity(Gravity.CENTER);
            if(i==0){
                tv.setTextColor(COLOR_CHECK);
            }else {
                tv.setTextColor(COLOR_NORMAL);
            }
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            tv.setText(mTitles[i]);
            tv.setLayoutParams(lp);
            tv.setTag(i);
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int k = (int) v.getTag();
                    listener.onClickItem(k);
                    refreshTextColor(k);
                }
            });
            addView(tv);
            tvList.put(i,tv);
        }
    }

    private void refreshTextColor(int k) {
        for(int i=0;i<mTabCount;i++){
            TextView textView = tvList.get(i);
            if(i!=k) {
                textView.setTextColor(COLOR_NORMAL);
            }else{
                textView.setTextColor(COLOR_CHECK);
            }
        }
        invalidate();
    }

    public void setIndicatorClickListener(IndicatorClickListener indicatorClickListener) {
        this.listener = indicatorClickListener;
    }

    public interface IndicatorClickListener{
        void onClickItem(int i);
    }
}
