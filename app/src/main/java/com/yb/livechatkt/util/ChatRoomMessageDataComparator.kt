package com.yb.livechatkt.util

import com.yb.livechatkt.bean.ChatRoomCustomMsg

class ChatRoomMessageDataComparator : Comparator<ChatRoomCustomMsg> {
    override fun compare(p0: ChatRoomCustomMsg?, p1: ChatRoomCustomMsg?): Int {
        return p0?.time!!.compareTo(p1?.time!!)
    }
}