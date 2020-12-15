package com.yb.livechatkt.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder
import com.netease.nimlib.sdk.chatroom.ChatRoomService
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver
import com.netease.nimlib.sdk.chatroom.constant.MemberQueryType
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomData
import com.netease.nimlib.sdk.chatroom.model.EnterChatRoomResultData
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum
import com.yb.livechatkt.bean.*
import com.yb.livechatkt.net.LiveChatUrl
import com.yb.livechatkt.util.NetConstant
import com.yb.livechatkt.util.SaveUserData
import com.yb.livechatkt.util.showToast


class LivePlayViewModel(application: Application) : BaseViewModel(application) {

    val liveDataLiveData = MutableLiveData<LiveRoomBean>()
    val chatRoomMessageLiveData = MutableLiveData<MutableList<ChatRoomTextMessage>>()
    val liveExitRoomLiveData = MutableLiveData<Boolean>()
    val roomPersonLiveData = MutableLiveData<MutableList<ChatRoomPerson>>()
    val chatRoomMessagesLiveData = MutableLiveData<ChatRoomTextMessage>()
    val chatRoomGiftMsgLiveData = MutableLiveData<ChatRoomGiftMessage>()
    val giftTypeLiveData = MutableLiveData<List<GiftData>>()
    val giftItemDataLiveData = MutableLiveData<List<GiftItemData>>()
    val pageNo = MutableLiveData<Int>()

    init {
        chatRoomMessageLiveData.value = ArrayList()
        liveExitRoomLiveData.value = false
    }


    fun getGiftAllType(){
        launch({userApi.getALlGiftTypeData()},giftTypeLiveData)
    }
    fun getGiftDataByType(type:Int,isFirst:Boolean){
        if (isFirst){
            pageNo.value = 0
        }else{
            pageNo.value = pageNo.value?.plus(1)
        }
        launch({userApi.getALlGiftByType(type= type,pageNo = pageNo.value!!)},giftItemDataLiveData)
    }


    fun searchLiveByLiveId(isShare: Boolean, liveId: Int, shareUserId: Int = 0){

        if (isShare){
            launch({ userApi.getLiveRoomDataByIdNoShare(liveId) }, liveDataLiveData)
        }else{
            launch({ userApi.getLiveRoomDataByIdAndShare(liveId, shareUserId) }, liveDataLiveData)
        }
    }
    //加入聊天室
    fun joinLiveRoom(){
        val enterChatRoom = EnterChatRoomData(liveDataLiveData.value?.room_id.toString())
        NIMClient.getService(ChatRoomService::class.java).enterChatRoomEx(enterChatRoom, 3).setCallback(
            object : RequestCallback<EnterChatRoomResultData> {
                override fun onSuccess(param: EnterChatRoomResultData?) {
                    isObserverMessage(true)
                    getChatRoomPerson()
                    getChatRoomHistoryMessage()
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
    fun isObserverMessage(isStart: Boolean){
        NIMClient.getService(ChatRoomServiceObserver::class.java)
            .observeReceiveMessage({
                if (it.isNotEmpty()) {
                    it.forEach { chatMsg ->
                        val msg = JsonObject().getAsJsonObject(chatMsg.content)
                        when(msg.get("code").asInt){
                            NetConstant.responseSuccessCode -> {
                                val chatMessage = Gson().fromJson<ChatRoomTextMessage>(chatMsg.content,ChatRoomTextMessage::class.java)
                                chatRoomMessagesLiveData.value = chatMessage
                            }
                            NetConstant.EXIT_LIVE -> {
                                liveExitRoomLiveData.value = true
                            }
                            NetConstant.SEND_GIFT_LIVE_MSG ->{
                                val chatMessage = Gson().fromJson(chatMsg.content,ChatRoomGiftMessage::class.java)
                                chatRoomGiftMsgLiveData.value = chatMessage
                            }
                        }
                    }
                }
            }, isStart)
    }
    //发送文本信息
    fun sendTextMessage(connect: String){
        if (connect.isEmpty()){
           NetConstant.NOT_INPUT_SOMETHING.showToast()
        }
        val msgData = RoomTextMessage(
            connect,
            SaveUserData.get().username,
            SaveUserData.get().id.toString(),
            SaveUserData.get().accId,
            SaveUserData.get().role,
            SaveUserData.get().role
        )
        var chatRoomCustomMsg = ChatRoomTextMessage(
            NetConstant.responseSuccessCode,
            NetConstant.reponseDefaultMsg,
            System.currentTimeMillis(),
            msgData
        )
        val chatRoomMsg = ChatRoomMessageBuilder.createChatRoomTextMessage(
            liveDataLiveData.value?.room_id.toString(), Gson().toJson(
                chatRoomCustomMsg
            )
        )
        NIMClient.getService(ChatRoomService::class.java).sendMessage(chatRoomMsg, false).setCallback(
            object : RequestCallback<Void> {
                override fun onSuccess(param: Void?) {
                    chatRoomMessagesLiveData.value = chatRoomCustomMsg
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
        NIMClient.getService(ChatRoomService::class.java).fetchRoomMembers(
            liveDataLiveData.value?.room_id.toString(),
            MemberQueryType.GUEST,
            0,
            20
        ).setCallback(
            object : RequestCallback<MutableList<ChatRoomMember>> {
                override fun onSuccess(param: MutableList<ChatRoomMember>?) {
                    var personList: MutableList<ChatRoomPerson> = ArrayList()
                    if (param?.isNotEmpty()!!) {
                        param.forEach {
                            personList.add(
                                ChatRoomPerson(
                                    it.account,
                                    it.nick,
                                    LiveChatUrl.headerBaseUrl + it.account
                                )
                            )
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

    //获取历史消息
    fun getChatRoomHistoryMessage(){

        NIMClient.getService(ChatRoomService::class.java)
            .pullMessageHistoryEx(liveDataLiveData.value?.room_id.toString(), System.currentTimeMillis(), 50, QueryDirectionEnum.QUERY_OLD).setCallback(
                object : RequestCallback<MutableList<ChatRoomMessage>> {
                    override fun onSuccess(param: MutableList<ChatRoomMessage>?) {
                        if (!param.isNullOrEmpty()) {
                            param.forEach {
                                Log.d(TAG, "onSuccess1: ${it.content}")
                                if (!it.content.startsWith("{") && !it.content.endsWith("}")) {
                                    return@forEach
                                }
                                Log.d(TAG, "onSuccess2: ${it.content}")
                                val message = Gson().fromJson<ChatRoomTextMessage>(
                                    it.content,
                                    ChatRoomTextMessage::class.java
                                )
                                if (message.code == NetConstant.responseSuccessCode) {
                                    message.time = it.time
                                    chatRoomMessagesLiveData.value = message
                                } else if (message.code == NetConstant.EXIT_LIVE) {
                                    liveExitRoomLiveData.value = true
                                }
                            }
                        }
                    }

                    override fun onFailed(code: Int) {

                    }

                    override fun onException(exception: Throwable?) {

                    }
                })
    }





}













