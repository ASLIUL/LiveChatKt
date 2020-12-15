package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.yb.livechatkt.bean.HomeLiveBean

class LiveViewModel(application: Application) : BaseViewModel(application) {

    //页码
    var pageNum = MutableLiveData<Int>()
    //每页数目
    var pageSize = MutableLiveData<Int>()

    var homeLiveLiveData = MutableLiveData<HomeLiveBean>()

    init {
        pageNum.value = 1
        pageSize.value = 50
    }

    fun getHomeLive(){
        var hashMap = HashMap<String,Any>()
        hashMap["channel_id"] = 1
        hashMap["pageNo"] = pageNum.value.toString()
        hashMap["pageSize"] = pageSize.value.toString()
        launch({userApi.getHomeLive(hashMap)},homeLiveLiveData)
    }




}