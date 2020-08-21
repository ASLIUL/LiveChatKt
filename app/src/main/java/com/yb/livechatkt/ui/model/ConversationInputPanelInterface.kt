package com.yb.livechatkt.ui.model

import com.netease.nimlib.sdk.msg.model.IMMessage
import com.yb.livechatkt.bean.GroupMessageBean

interface ConversationInputPanelInterface {

    fun sendTextMessage(textMessage: IMMessage)

    fun sendImageMessage(imageMessage: IMMessage)

    fun sendVideoMessage(videoMessage: IMMessage)

    fun sendVoiceMessage(voiceMessage: IMMessage)

    fun getToAccIds():String



}