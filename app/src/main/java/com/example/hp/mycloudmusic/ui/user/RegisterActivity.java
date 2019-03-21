package com.example.hp.mycloudmusic.ui.user;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.ui.BaseActivity;
import com.example.hp.mycloudmusic.util.DensityUtil;

import butterknife.Bind;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_REGISTER_TWO = 14;
    @Bind(R.id.iv_register_back)
    ImageView ivBack;
    @Bind(R.id.bt_register_next)
    Button btNextStep;
    @Bind(R.id.tv_register_agreement)
    TextView tvAgreement;

    @Override
    protected void doBeforeContentView() {
        DensityUtil.setCustomDensity(this,getApplication(),375);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        ivBack.setOnClickListener(this);
        btNextStep.setOnClickListener(this);

        SpannableStringBuilder spannableString = new SpannableStringBuilder("注册即表示同意《用户使用条款及服务协议》");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.alpha_10_black)),7,spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(20),7,spannableString.length(),Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        tvAgreement.setText(spannableString);       //此处不能加对spannableString使用toString
        tvAgreement.setOnClickListener(this);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_register_next:

                break;
            case R.id.iv_register_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_REGISTER_TWO:

                break;
        }
    }
}
