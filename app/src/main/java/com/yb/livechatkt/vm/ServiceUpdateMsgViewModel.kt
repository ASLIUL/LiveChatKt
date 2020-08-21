package com.yb.livechatkt.vm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.yb.livechatkt.bean.ServiceHiMsg
import com.yb.livechatkt.net.RetrofitUtil
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.showToast

class ServiceUpdateMsgViewModel(application: Application) :BaseViewModel(application) {

    var hiMsgLiveData = MutableLiveData<List<ServiceHiMsg>>()
    var msgLiveData = MutableLiveData<String>()
    var updateHiMsgLiveData = MutableLiveData<Boolean>()

    fun getMyHiMessage(){
        launch({userApi.getServiceHiMessage()},hiMsgLiveData)
    }
    fun submitData(){
        if (msgLiveData.value?.isEmpty()!!){
            NetConstant.PLEASE_INPUT_CONNECT.showToast()
            return
        }
        var hashMap:MutableMap<String,Any> = HashMap()
        hashMap.put("id", hiMsgLiveData?.value?.get(0)?.id!!)
        hashMap.put("message",msgLiveData?.value!!)
        launchAny({userApi.updateServiceHiMessage(RetrofitUtil.get(getApplication()).createJsonRequest(hashMap))},updateHiMsgLiveData)
    }

}