package com.yb.livechatkt.ui.model

import com.yb.livechatkt.bean.GroupMessageBean
import com.yb.livechatkt.bean.VoiceMessage

interface InputPanelInterface {

    fun sendTextMessage(textMessage: GroupMessageBean)

    fun sendImageMessage(imageMessage: GroupMessageBean)

    fun sendVideoMessage(videoMessage: GroupMessageBean)

    fun sendVoiceMessage(voiceMessage: GroupMessageBean)

    fun getToAccIds():String

}