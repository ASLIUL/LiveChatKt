package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.yb.livechatkt.bean.ServicePerson
import com.yb.livechatkt.bean.Session
import com.yb.livechatkt.db.DBManager
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.util.getConnect
import kotlinx.coroutines.launch

class ConversationViewModel(application: Application) : BaseViewModel(application)  {


    var serviceData = MutableLiveData<ServicePerson>()
    var historyMessageLiveData = MutableLiveData<List<IMMessage>>()
    val serviceIsOnLine = MutableLiveData<Boolean>()
    private val sessionDao = DBManager.getDBInstance().getSessionDao()
    var contactName:String = ""

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
                insertOneSession(message)
            }
            override fun onFailed(code: Int) {
            }
            override fun onException(exception: Throwable?) {
                exception?.printStackTrace()
            }
        })
    }

    fun getHistoryMessage(accid:String,sessionType:SessionTypeEnum){
        val messagea = MessageBuilder.createEmptyMessage(accid,sessionType,System.currentTimeMillis())
        NIMClient.getService(MsgService::class.java).pullMessageHistory(messagea,100,true).setCallback(object : RequestCallback<List<IMMessage>>{
            override fun onSuccess(param: List<IMMessage>?) {
                if (!param.isNullOrEmpty()){
                    historyMessageLiveData.value = param
                }
            }

            override fun onFailed(code: Int) {

            }

            override fun onException(exception: Throwable?) {

            }
        })
    }

    fun searchHyServicePerson(){
        launch({userApi.getServiceChatAccount()},serviceData)
    }

    fun judgeServiceIsOnLine(accid: String){
        launchAny({userApi.judgeServiceIsOnLine(accid)},serviceIsOnLine)
    }

    fun insertOneSession(imMessage: IMMessage){
        viewModelScope.launch {
            val session = sessionDao.selectOneSessionByAccId(imMessage.sessionId)
            if (session != null){
                sessionDao.updateSession(imMessage.sessionId, getConnect(imMessage),imMessage.time)
            }else{
                val sessionType = if (imMessage.sessionType == SessionTypeEnum.P2P) 1 else 2
                val session = Session(imMessage.sessionId,contactName, getConnect(imMessage),imMessage.time,1,LiveChatUrl.headerBaseUrl+imMessage.sessionId,sessionType)
                sessionDao.insertOneSession(session)
            }
        }
    }

}