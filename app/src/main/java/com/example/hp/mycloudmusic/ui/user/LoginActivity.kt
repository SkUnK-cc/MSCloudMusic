package com.example.hp.mycloudmusic.ui.user

import android.view.View
import android.widget.Toast
import com.example.hp.mycloudmusic.R
import com.example.hp.mycloudmusic.api.RetrofitFactory
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter
import com.example.hp.mycloudmusic.mvp.view.IBaseView
import com.example.hp.mycloudmusic.ui.BaseActivity
import com.example.hp.mycloudmusic.userinfo.LoginInfo
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<BasePresenter<IBaseView>>(), View.OnClickListener {
    override fun getContentView(): Int {
        return R.layout.activity_login
    }

    override fun initPresenter() {

    }

    override fun initData() {
    }

    override fun initListener() {
        bt_login.setOnClickListener(this)
    }

    override fun initView() {
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.bt_login -> loginPost()
        }
    }

    fun loginPost(){
        var phonenum:String = et_login_phone_num.text.toString()
        var password:String = et_login_password.text.toString()
        if(phonenum==null||phonenum.equals("") || password==null||password.equals("")){
            Toast.makeText(this,"手机号或密码不能为空！",Toast.LENGTH_SHORT).show()
            return
        }
        RetrofitFactory.provideCloudMusicApi()
                .login("",password,phonenum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: Observer<LoginInfo>{
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                    }

                    override fun onNext(t: LoginInfo) {
                        if(t!=null && t.code==0){
                            Toast.makeText(this@LoginActivity,"登陆成功",Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@LoginActivity,"登录失败",Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onError(e: Throwable) {
                    }

                })
    }




}
