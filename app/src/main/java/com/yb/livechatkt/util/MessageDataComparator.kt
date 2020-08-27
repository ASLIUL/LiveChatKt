package com.yb.livechatkt.util

import com.netease.nimlib.sdk.msg.model.IMMessage

class MessageDataComparator : Comparator<IMMessage> {
    override fun compare(p0: IMMessage?, p1: IMMessage?): Int {
        return p0?.time!!.compareTo(p1?.time!!)
    }
}