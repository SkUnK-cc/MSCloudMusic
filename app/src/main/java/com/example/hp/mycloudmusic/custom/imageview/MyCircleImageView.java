package com.example.hp.mycloudmusic.custom.imageview;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

public class MyCircleImageView extends android.support.v7.widget.AppCompatImageView {

    private Paint mPaint;
    private int mRadius;
    private float mScale;
    private Matrix matrix;
    private Bitmap bitmap;
    public MyCircleImageView(Context context) {
        super(context);
    }

    public MyCircleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyCircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = Math.min(getMeasuredWidth(),getMeasuredHeight());
        mRadius = size/2;
        setMeasuredDimension(size,size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        mPaint = new Paint();
        //获取bitmap
        bitmap = drawableToBitmap(getDrawable());
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mScale = (mRadius * 2.0f) / Math.min(bitmap.getHeight(),bitmap.getWidth());
        matrix = new Matrix();
        matrix.setScale(mScale,mScale);
        bitmapShader.setLocalMatrix(matrix);
        mPaint.setShader(bitmapShader);
        canvas.drawCircle(mRadius,mRadius,mRadius,mPaint);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable instanceof BitmapDrawable){
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        //创建一个bitmap
        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        //将drawable画到bitmap上，并返回
        drawable.draw(canvas);
        return bitmap;
    }
}
