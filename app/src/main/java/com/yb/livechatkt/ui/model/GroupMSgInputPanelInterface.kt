package com.yb.livechatkt.ui.model

import com.yb.livechatkt.bean.GroupMessageBean

interface GroupMSgInputPanelInterface {

    fun sendTextMessage(textMessage: GroupMessageBean)

    fun sendImageMessage(imageMessage: GroupMessageBean)

    fun sendVideoMessage(videoMessage: GroupMessageBean)

    fun sendVoiceMessage(voiceMessage: GroupMessageBean)

    fun getToAccIds():String



}