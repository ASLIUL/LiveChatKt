package com.yb.livechatkt.bean

data class LiveShareMessageBean(
    val code:Int,
    val msg:String,
    val sendUserId:String,
    val liveCover:String,
    val liveTitle:String,
    val liveId:String,
    val receiveUserId:String,
    val liveName:String
) {
}