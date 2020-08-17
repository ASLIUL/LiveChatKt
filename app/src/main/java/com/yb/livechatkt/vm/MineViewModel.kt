package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yb.livechatkt.bean.Me
import com.yb.livechatkt.util.SaveUserData

class MineViewModel(application: Application) : BaseViewModel(application) {

    var meDataLiveData = MutableLiveData<Me>()

    fun getMineData(){
        launch({userApi.getMineData()},meDataLiveData)
    }


    fun exitLogin(){
        Log.d(TAG, "exitLogin: 退出登录")
        launchNoT({userApi.exitLogin()})
    }

}