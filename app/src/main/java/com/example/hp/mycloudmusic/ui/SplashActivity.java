package com.example.hp.mycloudmusic.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hp.mycloudmusic.R;

public class SplashActivity extends BaseActivity {

    private ImageView ivIcon;
    private LinearLayout llTexts;

    private Boolean hasFocus = false;
    private long delayTime = 700;
    private long animDuration = 1000;

    @Override
    protected void initView() {
        ivIcon = findViewById(R.id.iv_splash_icon);
        llTexts = findViewById(R.id.ll_splash);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(this.hasFocus){
            return;
        }
        this.hasFocus = true;
        initAnimator();
    }

    private void initAnimator() {
        ViewPropertyAnimator iconAnimator = ivIcon.animate();
        iconAnimator.setStartDelay(delayTime)
                .translationYBy(-(ivIcon.getHeight()/3))
                .setDuration(animDuration)
                .start();
        ViewPropertyAnimator llAnimator = llTexts.animate();
        llAnimator.setStartDelay(delayTime+100)
                .translationYBy(ivIcon.getHeight()/4)
                .alpha(1)
                .setDuration(animDuration)
                .start();
        llAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                llTexts.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },800);
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }
    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }
}
