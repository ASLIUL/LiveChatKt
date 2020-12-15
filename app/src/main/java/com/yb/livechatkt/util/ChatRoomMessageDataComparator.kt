package com.yb.livechatkt.util

import com.yb.livechatkt.bean.ChatRoomTextMessage

class ChatRoomMessageDataComparator : Comparator<ChatRoomTextMessage> {
    override fun compare(p0: ChatRoomTextMessage?, p1: ChatRoomTextMessage?): Int {
        return p0?.time!!.compareTo(p1?.time!!)
    }
}