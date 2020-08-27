package com.yb.livechatkt.util

import com.yb.livechatkt.bean.GroupMessageBean

class GroupMessageDataComparator : Comparator<GroupMessageBean> {
    override fun compare(p0: GroupMessageBean?, p1: GroupMessageBean?): Int {
        return p0?.uuid!!.compareTo(p1?.uuid!!)
    }
}