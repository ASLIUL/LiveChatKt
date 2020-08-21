package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.yb.livechatkt.bean.ServicePerson

class ConversationViewModel(application: Application) : BaseViewModel(application)  {


    var serviceData = MutableLiveData<ServicePerson>()

    var messageMapLiveData = MutableLiveData<MutableMap<String,IMMessage>>()
    init {

        messageMapLiveData.value = HashMap()
    }

    fun sendMessage(message: IMMessage){
        NIMClient.getService(MsgService::class.java).sendMessage(message,false).setCallback(object :
            RequestCallback<Void> {
            override fun onSuccess(param: Void?) {
                var hashMap:MutableMap<String,IMMessage> = HashMap()
                hashMap[message.uuid] = message
                messageMapLiveData.value = hashMap

            }

            override fun onFailed(code: Int) {

            }

            override fun onException(exception: Throwable?) {
                exception?.printStackTrace()
            }

        })


    }

    fun searchHyServicePerson(){
        launch({userApi.getServiceChatAccount()},serviceData)
    }

}