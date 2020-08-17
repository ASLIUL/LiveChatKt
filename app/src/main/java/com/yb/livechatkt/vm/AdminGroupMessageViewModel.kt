package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.yb.livechatkt.bean.GroupMessageBean
import com.yb.livechatkt.bean.LiveEnum
import com.yb.livechatkt.bean.ServiceGroup
import com.yb.livechatkt.net.RetrofitUtil

class AdminGroupMessageViewModel(application: Application) : BaseViewModel(application) {

    var messageStatueLiveData = MutableLiveData<MutableMap<String,LiveEnum>>()
    init {
        messageStatueLiveData.value = HashMap()
    }

    fun sendMessage(bean: GroupMessageBean){
        Log.d(TAG, "sendMessage: 正在发送文本消息")

        messageStatueLiveData.value?.put(bean.uuid,LiveEnum.sending)

        launchHashMap({userApi.adminSendMessage(RetrofitUtil.get(getApplication()).createJsonRequest(bean))},messageStatueLiveData,bean.uuid)

    }
    fun uploadFileCreteMessage(messageKey: String,bean: GroupMessageBean){
        when(bean.type){
            1 -> {

            }
        }
    }
}