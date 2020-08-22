package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder
import com.netease.nimlib.sdk.chatroom.ChatRoomService
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData
import com.yb.livechatkt.bean.ChatRoomCustomMsg
import com.yb.livechatkt.bean.ChatRoomPerson
import com.yb.livechatkt.bean.LiveRoomBean
import com.yb.livechatkt.bean.MsgData
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.showToast

class LivePlayViewModel(application: Application) : BaseViewModel(application) {

    var liveDataLiveData = MutableLiveData<LiveRoomBean>()
    var chatRoomMessageLiveData = MutableLiveData<MutableList<ChatRoomCustomMsg>>()
    val liveExitRoomLiveData = MutableLiveData<Boolean>()
    //var chatMsgConnect = MutableLiveData<String>()
    var roomPersonLiveData = MutableLiveData<MutableList<ChatRoomPerson>>()

    init {
        chatRoomMessageLiveData.value = ArrayList()
        liveExitRoomLiveData.value = false
    }

    fun searchLiveByLiveId(isShare:Boolean,liveId:Int,shareUserId:Int = 0){

        if (isShare){
            launch({userApi.getLiveRoomDataByIdNoShare(liveId)},liveDataLiveData)
        }else{
            launch({userApi.getLiveRoomDataByIdAndShare(liveId,shareUserId)},liveDataLiveData)
        }
    }
    //加入聊天室
    fun joinLiveRoom(){
        val enterChatRoom = EnterChatRoomData(liveDataLiveData.value?.room_id.toString())
        NIMClient.getService(ChatRoomService::class.java).enterChatRoomEx(enterChatRoom,3).setCallback(
            object : RequestCallback<EnterChatRoomResultData> {
                override fun onSuccess(param: EnterChatRoomResultData?) {
                    isObserverMessage(true)
                    Log.d(TAG, "onSuccess: ${param.toString()}")
                }
                override fun onFailed(code: Int) {
                    Log.d(TAG, "onFailed: $code")
                }

                override fun onException(exception: Throwable?) {
                    Log.d(TAG, "onException: ${exception?.message}")
                }

            })
    }
    //退出聊天室
    fun exitLiveRoom(){
        isObserverMessage(false)
        NIMClient.getService(ChatRoomService::class.java).exitChatRoom(liveDataLiveData.value?.room_id.toString())
    }
    //是否开始监听消息
    fun isObserverMessage(isStart:Boolean){
        NIMClient.getService(ChatRoomServiceObserver::class.java)
            .observeReceiveMessage({
                if (it.isNotEmpty()){
                    it.forEach {chatMsg ->
                        val chatMessage:ChatRoomCustomMsg = Gson().fromJson(chatMsg.content,ChatRoomCustomMsg::class.java)
                        if (chatMessage.code == NetConstant.responseSuccessCode){
                            chatRoomMessageLiveData.value?.add(chatMessage)
                        }else if (chatMessage.code == NetConstant.EXIT_LIVE){
                            liveExitRoomLiveData.value = true
                        }
                    }
                }
            },isStart)
    }
    //发送文本信息
    fun sendTextMessage(connect:String){
        if (connect.isNotEmpty()!!){
           NetConstant.NOT_INPUT_SOMETHING.showToast()
        }
        val msgData = MsgData(connect,SaveUserData.get().username,SaveUserData.get().id.toString(),SaveUserData.get().accId,SaveUserData.get().role,SaveUserData.get().role)
        var chatRoomCustomMsg = ChatRoomCustomMsg(NetConstant.responseSuccessCode,NetConstant.reponseDefaultMsg,msgData)
        val chatRoomMsg = ChatRoomMessageBuilder.createChatRoomTextMessage(liveDataLiveData.value?.room_id.toString(),Gson().toJson(chatRoomCustomMsg))
        NIMClient.getService(ChatRoomService::class.java).sendMessage(chatRoomMsg,false).setCallback(
            object : RequestCallback<Void> {
                override fun onSuccess(param: Void?) {

                }

                override fun onFailed(code: Int) {
                    Log.d(TAG, "onFailed: $code")
                }

                override fun onException(exception: Throwable?) {
                    exception?.printStackTrace()
                }
            })
    }
    //获取直播间人员列表
    fun getChatRoomPerson(){
        NIMClient.getService(ChatRoomService::class.java).fetchRoomMembers(liveDataLiveData.value?.room_id.toString(),MemberQueryType.ONLINE_NORMAL,0,20).setCallback(
            object : RequestCallback<MutableList<ChatRoomMember>> {
                override fun onSuccess(param: MutableList<ChatRoomMember>?) {
                    var personList:MutableList<ChatRoomPerson> = ArrayList()
                    if (param?.isNotEmpty()!!){
                        param.forEach {
                            personList.add(ChatRoomPerson(it.account,it.nick,LiveChatUrl.headerBaseUrl+it.account))
                        }
                        roomPersonLiveData.value = personList
                    }
                }

                override fun onFailed(code: Int) {
                    Log.d(TAG, "onFailed: $code")
                }

                override fun onException(exception: Throwable?) {
                    exception?.printStackTrace()
                }

            })
    }





}













