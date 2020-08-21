package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yb.livechatkt.bean.Me
import com.yb.livechatkt.bean.ServicePerson
import com.yb.livechatkt.util.SaveUserData
import kotlinx.coroutines.launch

class MineViewModel(application: Application) : BaseViewModel(application) {

    var meDataLiveData = MutableLiveData<Me>()
    var isShowService = MutableLiveData<Boolean>()
    var exitLiveData = MutableLiveData<Boolean>()
    var updateStatusLiveData = MutableLiveData<Boolean>()

    init {
        when(SaveUserData.get().role){
            1,2 -> {
                isShowService.value = true
            }
            3,4 ->{
                isShowService.value = false
            }
        }
    }

    fun getMineData(){
        launch({userApi.getMineData()},meDataLiveData)
    }


    fun serviceState(status:String){
        launchAny({userApi.updateServiceState(status)},updateStatusLiveData)
    }


    fun exitLogin(){
        Log.d(TAG, "exitLogin: 退出登录")
        launchAny({userApi.exitLogin()},exitLiveData)
    }

}