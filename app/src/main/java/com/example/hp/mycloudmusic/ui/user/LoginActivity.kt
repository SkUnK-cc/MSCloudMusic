package com.example.hp.mycloudmusic.ui.user

import com.example.hp.mycloudmusic.R
import com.example.hp.mycloudmusic.mvp.presenter.BasePresenter
import com.example.hp.mycloudmusic.mvp.view.IBaseView
import com.example.hp.mycloudmusic.ui.BaseActivity

class LoginActivity : BaseActivity<BasePresenter<IBaseView>>() {

    override fun getContentView(): Int {
        return R.layout.activity_login
    }

    override fun initPresenter() {
    }

    override fun initData() {
    }

    override fun initListener() {
    }

    override fun initView() {
    }




}
