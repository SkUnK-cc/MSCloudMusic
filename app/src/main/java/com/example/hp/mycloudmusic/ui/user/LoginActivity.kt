package com.example.hp.mycloudmusic.ui.user

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.hp.mycloudmusic.R
import com.example.hp.mycloudmusic.api.RetrofitFactory
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter
import com.example.hp.mycloudmusic.mvp.view.IBaseView
import com.example.hp.mycloudmusic.rx.BaseObserver
import com.example.hp.mycloudmusic.ui.BaseActivity
import com.example.hp.mycloudmusic.userinfo.LoginInfo
import com.example.hp.mycloudmusic.userinfo.User
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.register_bar.*

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
        bt_register.setOnClickListener(this)
        iv_register_back.setOnClickListener(this)
    }

    override fun initView() {
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.bt_login -> loginPost()
            R.id.bt_register -> toRegister()
            R.id.iv_register_back -> finish()
        }
    }

    private fun toRegister() {
        val intent = Intent(this,RegisterActivity::class.java)
        startActivityForResult(intent,6)
    }

    private fun loginPost(){
        val phonenum:String = et_login_phone_num.text.toString()
        val password:String = et_login_password.text.toString()
        if(phonenum == "" || password == ""){
            Toast.makeText(this,"手机号或密码不能为空！",Toast.LENGTH_SHORT).show()
            return
        }
        RetrofitFactory.provideCloudMusicApi()
                .login(phonenum,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object: BaseObserver<LoginInfo>() {
                    override fun onNext(resp: LoginInfo) {
                        if( resp.code==0){
                            Toast.makeText(this@LoginActivity,resp.msg,Toast.LENGTH_SHORT).show()
                            Log.e("resp",resp.toString())
                        }else if(resp.code==1){
                            Toast.makeText(this@LoginActivity,resp.msg,Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onError(throwable: Throwable) {
                        super.onError(throwable)
                        Toast.makeText(this@LoginActivity,"网络出错，请重试",Toast.LENGTH_SHORT).show()
                    }
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode){
            RegisterActivity.REGISTER_SUCCESS -> {
                val user: User = data?.getParcelableExtra<User>("user") ?: return
                et_login_phone_num.setText(user.phonenum)
                et_login_password.setText(user.password)
            }
        }
    }
}
