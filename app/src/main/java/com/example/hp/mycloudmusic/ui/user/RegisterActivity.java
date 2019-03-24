package com.example.hp.mycloudmusic.ui.user;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.api.RetrofitFactory;
import com.example.hp.mycloudmusic.ui.BaseActivity;
import com.example.hp.mycloudmusic.userinfo.LoginInfo;
import com.example.hp.mycloudmusic.util.DensityUtil;
import com.example.hp.mycloudmusic.util.Util;

import butterknife.Bind;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    public static final int REQUEST_CODE_REGISTER_TWO = 14;
    public static final int REGISTER_SUCCESS=3;
    public static final int REGISTER_FAIL = 4;
    private int requestCode ;
    @Bind(R.id.iv_register_back)
    ImageView ivBack;
    @Bind(R.id.et_register_phone_num)
    EditText etPhonenum;
    @Bind(R.id.et_register_username)
    EditText etUsername;
    @Bind(R.id.et_register_password)
    EditText etPassword;
    @Bind(R.id.et_register_password_againt)
    EditText etPsdAgaint;
    @Bind(R.id.bt_register_now)
    Button btRegister;
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
        btRegister.setOnClickListener(this);

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
            case R.id.bt_register_now:
                register();
                break;
            case R.id.iv_register_back:
                finish();
                break;
        }
    }

    private void register() {
        String username = etUsername.getText().toString();
        String phonenum = etPhonenum.getText().toString();
        String password = etPassword.getText().toString();
        String password2 = etPsdAgaint.getText().toString();
        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(phonenum) ||
                TextUtils.isEmpty(password) || TextUtils.isEmpty(password2)){
            Util.toastMessage(this,"用户名、手机号或密码不能为空!");
            return;
        }
        if(!isValidPhone(phonenum)){
            Util.toastMessage(this,"手机号码格式错误!");
        }
        if(!checkPassword(password,password2))return;
        RetrofitFactory.provideCloudMusicApi()
                .register(username,phonenum,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }
                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        if(loginInfo==null)return;
                        Log.e(TAG, "onNext: "+loginInfo.toString());
                        if(loginInfo.getCode()==0){
                            Toast.makeText(RegisterActivity.this,loginInfo.getMsg(),Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.putExtra("user",loginInfo.getData());
                            setResult(REGISTER_SUCCESS,intent);
                            finish();
                        }else if(loginInfo.getCode()==2){
                            //账号已存在
                            Toast.makeText(RegisterActivity.this,loginInfo.getMsg(),Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RegisterActivity.this, loginInfo.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RegisterActivity.this,"网络出错，请重试",Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onComplete() {
                    }
                });
    }

    private boolean isValidPhone(String phone) {
        String telRegex = "^((13[0-9])|(15[^4])|(18[0-9])|(17[0-8])|(147,145))\\d{8}$";
        return !TextUtils.isEmpty(phone) && phone.matches(telRegex);
    }

    private boolean checkPassword(String password1,String password2){
        if(password1.equals(password2))return true;
        else return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_CODE_REGISTER_TWO:

                break;
        }
    }
}
