package com.example.hp.mycloudmusic.fragment.instance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.hp.mycloudmusic.R;
import com.example.hp.mycloudmusic.custom.imageview.RoundImageView;
import com.example.hp.mycloudmusic.ui.user.LoginActivity;
import com.example.hp.mycloudmusic.util.Util;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

public class MeFragment extends BaseFragment implements View.OnClickListener {

//    @Override
//    protected void setupActivityComponent(AppComponent appComponent) {
//        DaggerActivityComponent.builder()
//                .appComponent(appComponent)
//                .build()
//                .inject(this);
//    }
    @Bind(R.id.img_myself_head)
    RoundImageView ivHead;
    @Bind(R.id.tv_nickname)
    TextView tvNickName;

    private String mAppid;
    Tencent mTencent ;

    @Override
    protected void initData() {
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initView() {
        mAppid = "1108137922";
        if(mTencent==null && !TextUtils.isEmpty(mAppid)) {
            mTencent = Tencent.createInstance(mAppid, getContext());
        }
        ivHead.setOnClickListener(this);
        ivHead.setImageResource(R.drawable.test_img);
    }

    @Override
    protected int getContentView() {
        return R.layout.me_fragment;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.img_myself_head:
                //onQQLogin();
                onClickLogin();
                break;
            default:
                break;
        }
    }

    private void onClickLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void onQQLogin() {
        if(!mTencent.isSessionValid()){
            mTencent.login(getActivity(),"all",loginListener,true);
        }else{
            mTencent.logout(getContext());
            updateUserInfo();
        }
    }

    IUiListener loginListener = new BaseUiListener(){
        @Override
        protected void doComplete(JSONObject values) {
            super.doComplete(values);
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };

    private void updateUserInfo() {
        if(mTencent!=null && mTencent.isSessionValid()){
            IUiListener listener = new IUiListener() {
                @Override
                public void onComplete(Object res) {
                    Util.showResultDialog(getContext(),res.toString(),"information");
                    Message msg = new Message();
                    msg.obj = res;
                    msg.what = 0;
                    mHandler.sendMessage(msg);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject json = (JSONObject) res;
                            if(json.has("figureurl")){
                                Bitmap bitmap = null;
                                try {
                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.obj = bitmap;
                                msg.what = 1;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }).start();
                }

                @Override
                public void onError(UiError uiError) {
                }

                @Override
                public void onCancel() {

                }
            };
//            mTencent.requestAsync();
            UserInfo info = new UserInfo(getContext(),mTencent.getQQToken());
            info.getUserInfo(listener);
            //mTencent.initSessionCache(mTencent.loadSession("222222"));
        }else{
            //无效，无法获得数据
        }
    }



    private void initOpenidAndToken(JSONObject jsonObject){
        try {
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if(!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)){
                mTencent.setAccessToken(token,expires);
                mTencent.setOpenId(openId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class BaseUiListener implements IUiListener {
        @Override
        public void onComplete(Object response) {
            if(null == response){
                Util.showResultDialog(getContext(),"返回为空","登录失败");
                return;
            }
            JSONObject jsonResp = (JSONObject) response;
            if(jsonResp != null && jsonResp.length() == 0){
                Util.showResultDialog(getContext(),"返回为空","登录失败");
                return;
            }
            //Util.showResultDialog(getContext(),response.toString(),"登录成功");
            Util.toastMessage(getActivity(),"Login success.");
            doComplete((JSONObject) response);
        }

        @Override
        public void onError(UiError e) {
            Util.toastMessage(getActivity(), "onError: " + e.errorDetail);
            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
            Util.toastMessage(getActivity(),"onCancel");
            Util.dismissDialog();
        }

        protected void doComplete(JSONObject values) {

        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 0){
                JSONObject response = (JSONObject) msg.obj;
                if(response.has("nickname")){
                    try{
                        String nickname = response.getString("nickname");
                        Log.e("QQ Login", "handleMessage: name = "+nickname);
                        tvNickName.setText(nickname);
                    }catch (JSONException e ){
                        e.printStackTrace();
                    }
                }
            }else if (msg.what == 1){
                Bitmap originalBitmap = (Bitmap) msg.obj;
                Log.e("size", originalBitmap.getWidth()+"  "+originalBitmap.getHeight()+"\n"+ivHead.getWidth()+"  "+ivHead.getHeight());
                Bitmap scaleBitmap = setImgSize(originalBitmap,ivHead.getMeasuredWidth(),ivHead.getMeasuredHeight());
                Log.e("size", scaleBitmap.getWidth()+"  "+scaleBitmap.getHeight()+"\n"+ivHead.getWidth()+"  "+ivHead.getHeight());
                ivHead.setImageBitmap(scaleBitmap);
            }
        }
    };

    public Bitmap setImgSize(Bitmap bm,int newWidth,int newHeight){
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scale = (newWidth * 1.0f)/Math.min(width,height);
        Matrix matrix = new Matrix();
        matrix.postScale(scale,scale);
        Bitmap bitmap = Bitmap.createBitmap(bm,0,0,width,height,matrix,true);
        return bitmap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTencent.onActivityResult(requestCode,resultCode,data);
    }
}
